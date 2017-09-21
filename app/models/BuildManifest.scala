package models

import com.github.andr83.scalaconfig._
import com.typesafe.config.{Config, ConfigObject}
import play.api.libs.json.{JsNull, JsValue, Json}

import scala.collection.JavaConverters._


case class BuildManifest(buildImage: String, tasks: List[BuildTask])

case class BuildTask(name: String, instructions: List[String])

object BuildManifest {
  def fromConfiguration(obj: ConfigObject): BuildManifest = {
    val config = obj.toConfig
    val tasks = config.asUnsafe[List[Config]]("tasks") flatMap { task =>
      task.entrySet().asScala.map { a =>
        val key = a.getKey
        val value = a.getValue
        BuildTask(key, value.unwrapped().asInstanceOf[String].split("\n").toList)
      }
    }
    BuildManifest(config.asUnsafe[String]("image"), tasks)
  }

  def asJson(bm: BuildManifest): JsValue = {
    val tasks = bm.tasks map { t =>
      Json.obj(t.name -> t.instructions.mkString("\n"))
    }
    Json.obj(
      "image" -> bm.buildImage,
      "tasks" -> Json.arr(tasks)
    )
  }
}
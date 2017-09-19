package controllers

import javax.inject._

import scala.collection.JavaConversions._
import play.api.Configuration
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

import scala.collection.mutable.ListBuffer

case class BuildData(name: String, age: Int)

case class BuildPlatform(name: String, os: String, arch: String, arm: Option[String])

@Singleton
class HomeController @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val platforms = List.empty ++ config.underlying.getObjectList("wedge.buildPlatforms") map { i =>
    BuildPlatform(i.get("name").unwrapped().toString, i.get("goos").unwrapped().toString, i.get("goarch").unwrapped().toString, Option(i.unwrapped().get("goarm").asInstanceOf[String]))
  }

  val buildForm = Form(
    mapping(
      "name" -> text,
      "age" -> number
    )(BuildData.apply)(BuildData.unapply)
  )

  def index() = Action { implicit request: Request[AnyContent] =>

    val asTuples = platforms.zipWithIndex.map { case (element, index) =>
      (index.toString, element.name)
    }

    Ok(views.html.index(buildForm, asTuples))
  }
}

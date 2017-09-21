package controllers

import javax.inject._

import play.api.Configuration
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

import scala.collection.JavaConverters._

case class BuildData(platformIndex: Int)

case class BuildPlatform(name: String, os: String, arch: String, arm: Option[String])

@Singleton
class HomeController @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val platforms = List.empty ++ config.underlying.getObjectList("wedge.buildPlatforms").asScala map { i =>
    BuildPlatform(i.get("name").unwrapped().toString, i.get("goos").unwrapped().toString, i.get("goarch").unwrapped().toString, Option(i.unwrapped().get("goarm").asInstanceOf[String]))
  }

  val platformsAsTuples = platforms.zipWithIndex.map { case (element, index) =>
    (index.toString, element.name)
  }

  val buildForm = Form(
    mapping(
      "platformIndex" -> number.verifying("platformIndex", fields => fields match {
        case data => platforms.isDefinedAt(data)
      })
    )(BuildData.apply)(BuildData.unapply)

  )

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(buildForm, platformsAsTuples))
  }

  def build() = Action { implicit request: Request[AnyContent] =>
    buildForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.index(formWithErrors, platformsAsTuples))
      },
      data => {
        Ok(views.html.building())
      }
    )
  }
}

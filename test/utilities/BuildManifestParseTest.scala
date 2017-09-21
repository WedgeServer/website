package utilities

import com.typesafe.config.{ConfigFactory, ConfigObject}
import models.BuildManifest
import org.scalatest.TestData
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Injecting
import play.api.{Application, Configuration}

class BuildManifestParseTest extends PlaySpec with GuiceOneAppPerTest with Injecting {

  implicit override def newAppForTest(testData: TestData): Application =
    new GuiceApplicationBuilder().loadConfig(Configuration(ConfigFactory.load("testing.conf"))).build()

  "BuildManifest companion object" should {
    "successfully convert a ConfigObject into a case class" in {
      val configObj = inject[Configuration].get[ConfigObject]("wedge.manifestTemplate")
      val manifest = BuildManifest.fromConfiguration(configObj)


      manifest.buildImage mustBe "debian/buster"
      manifest.tasks.length mustBe 2
      manifest.tasks.map(k => k.name) must contain ("build")
      manifest.tasks.map(k => k.name) must contain ("setup")

      val asMap = manifest.tasks.map(t => t.name -> t.instructions).toMap
      asMap.get("build").isDefined mustBe true
      asMap.get("build").get mustBe List("build one")

      asMap.get("setup").isDefined mustBe true
      asMap.get("setup").get mustBe List("setup one", "setup two")
    }
  }

}

package utilities

import com.typesafe.config.{ConfigFactory, ConfigObject}
import models.BuildManifest
import org.scalatest.TestData
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Injecting
import play.api.{Application, Configuration}

class BuildManifestSerialiseTest extends PlaySpec with GuiceOneAppPerTest with Injecting {

  implicit override def newAppForTest(testData: TestData): Application =
    new GuiceApplicationBuilder().loadConfig(Configuration(ConfigFactory.load("testing.conf"))).build()

  "BuildManifest companion object" should {
    "successfully convert a BuildManifest to JSON" in {
      val configObj = inject[Configuration].get[ConfigObject]("wedge.manifestTemplate")
      val manifest = BuildManifest.fromConfiguration(configObj)
      val asJson = BuildManifest.asJson(manifest).toString()
      asJson mustBe "{\"image\":\"debian/buster\",\"tasks\":[[{\"setup\":\"setup one\\nsetup two\"},{\"build\":\"build one\"}]]}"
    }
  }

}

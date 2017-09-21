package controllers

import com.typesafe.config.ConfigFactory
import org.scalatest.TestData
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Configuration}
import play.api.test._
import play.api.test.Helpers._

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  implicit override def newAppForTest(testData: TestData): Application =
    new GuiceApplicationBuilder().loadConfig(Configuration(ConfigFactory.load("testing.conf"))).build()

  "HomeController GET" should {
    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Wedge")
      contentAsString(home) must include ("MS-DOS")
    }
  }
}

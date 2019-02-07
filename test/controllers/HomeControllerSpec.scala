package controllers

import org.scalatest.GivenWhenThen
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._


class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with GivenWhenThen {

  "HomeController GET" should {
    "render the index page" in {
      Given("GET HTTP request on / path")
      val request = FakeRequest(GET, "/")

      When("request is sent to app")
      val home = route(app, request).get

      Then("response should be valid")
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Enter json")
    }
  }
}

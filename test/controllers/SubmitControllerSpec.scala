package controllers

import model.Node
import org.scalatest.GivenWhenThen
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers.{POST, contentAsString, contentType, route, status, _}
import play.api.test.{FakeRequest, Injecting}

class SubmitControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with GivenWhenThen {

  "SubmitController POST" should {
    "render the submit response page" in {
      Given("valid POST HTTP request on /submit path")
      val request = FakeRequest(POST, "/submit")
        .withFormUrlEncodedBody("json" -> """[{"id":1, "name": "node"}]""")

      When("request is sent to app")
      val result = route(app, request).get

      Then("response should be valid")
      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("1")
    }

    "render error message on invalid json" in {
      Given("request with invalid json")
      val wrongRequest = FakeRequest(POST, "/submit")
        .withFormUrlEncodedBody("json" -> "invalid json")

      When("input passed to submit controller")
      val result = route(app, wrongRequest).get

      Then("error should be shown")
      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustEqual SubmitController.JSON_PARSE_ERROR_MESSAGE
    }

    "render error message on valid json but not of List[Node] type" in {
      Given("request with some improper json")
      val wrongRequest = FakeRequest(POST, "/submit")
        .withFormUrlEncodedBody("json" -> """[{"not": "list[node]"}]""")

      When("input passed to submit controller")
      val result = route(app, wrongRequest).get

      Then("error should be shown")
      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustEqual SubmitController.JSON_PARSE_ERROR_MESSAGE
    }
  }

  "SubmitController" should {
    "generate proper tree structure" in {
      Given("valid input")
      val controller = app.injector.instanceOf[SubmitController]
      val input = List(Node(1, "A", Some(List(Node(2, "AA", None)))), Node(3, "B", None))

      When("input passed to submit controller")
      val html = controller.convertToHtml(input)
      print(html.toString())
      Then("proper tree structure should be generated")
      html.toString() mustEqual
        """|<ul class="tree"><li><a href=#>1</a>
           |<ul>
           |<li>id: 1</li>
           |<li>name: A</li><li><a href=#>nodes:</a><ul><li><a href=#>1</a>
           |<ul>
           |<li>id: 2</li>
           |<li>name: AA</li></ul></li></ul></li></ul></li><li><a href=#>2</a>
           |<ul>
           |<li>id: 3</li>
           |<li>name: B</li></ul></li></ul>""".stripMargin

    }
  }
}

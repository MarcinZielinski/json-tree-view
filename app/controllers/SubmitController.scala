package controllers

import controllers.SubmitController.JSON_PARSE_ERROR_MESSAGE
import javax.inject.Inject
import model.{JsonForm, Node}
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.twirl.api.Html

import scala.util.Try

class SubmitController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {
  def submit(): Action[AnyContent] = Action { implicit request =>
    val formData: JsonForm = JsonForm.form.bindFromRequest.get

    Try(Json.parse(formData.json).validate[List[Node]]
      .fold(
        errors => BadRequest(JSON_PARSE_ERROR_MESSAGE),
        nodesList => Ok(views.html.submit(convertToHtml(nodesList)))
      )).getOrElse(BadRequest(JSON_PARSE_ERROR_MESSAGE))
  }

  def convertToHtml(nodes: List[Node]): Html = {
    def buildTree: String = {
      "<ul class=\"tree\">" + commonPart(nodes) + "</ul>"
    }

    def commonPart(currUlNodes: List[Node]): String = {
      var res = ""
      var index = 1
      for (currNode <- currUlNodes) {
        res += "<li><a href=#>" + index + "</a>"
        res += "<ul>"
        res += "<li>id: " + currNode.id + "</li>"
        res += "<li>name: " + currNode.name + "</li>"
        if (currNode.nodes.isDefined) {
          res += "<li><a href=#>nodes:" + addChildren(currNode.nodes.get) + "</a></li>"
        }
        res += "</ul></li>"
        index += 1
      }
      res
    }

    def addChildren(currUlNodes: List[Node]): String = {
      var res = "<ul>"
      res += commonPart(currUlNodes)
      res + "</ul>"
    }

    Html(buildTree)
  }
}

object SubmitController {
  val JSON_PARSE_ERROR_MESSAGE = "Invalid json"
}
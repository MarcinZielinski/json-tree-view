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
    def buildTree: String = s"""<ul class="tree">${addNodes(nodes)}</ul>"""

    def addNodes(currUlNodes: List[Node]): String = {
      currUlNodes.zipWithIndex.map {
        case (currNode, index) =>
          var res =
            s"""|<li><a href=#>${index + 1}</a>
                |<ul>
                |<li>id: ${currNode.id}</li>
                |<li>name: ${currNode.name}</li>""".stripMargin
          currNode.nodes.foreach(_ => res += s"<li><a href=#>nodes:</a>${addChildrenNodes(currNode.nodes.get)}</li>")
          res += "</ul></li>"
          res
      }.mkString
    }

    def addChildrenNodes(currUlNodes: List[Node]): String = s"""<ul>${addNodes(currUlNodes)}</ul>"""

    Html(buildTree)
  }
}

object SubmitController {
  val JSON_PARSE_ERROR_MESSAGE = "Invalid json"
}
package controllers

import javax.inject._
import model.JsonForm
import play.api.i18n.I18nSupport
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport  {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(JsonForm.form))
  }
}

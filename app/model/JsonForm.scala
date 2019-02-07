package model

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text

case class JsonForm(json: String)

object JsonForm {
  val form = Form(
    mapping(
      "json" -> text
    )(JsonForm.apply)(JsonForm.unapply)
  )
}
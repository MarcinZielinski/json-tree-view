package model

import play.api.libs.json.{Json, OFormat}

case class Node(id: Int, name: String, nodes: Option[List[Node]])

object Node {
  implicit val fmt: OFormat[Node] = Json.format[Node]
}
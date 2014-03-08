package narwhal.native.tables

import narwhal.model._

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.ProvenShape
import scala.slick.ast.TypedType

class Nodes(tag: Tag) extends Table[Node](tag, "nodes") {
  def id = column[String]("node_id", O.PrimaryKey, O.DBType("text"))
  def parent = column[String]("parent_node_id", O.Nullable, O.DBType("text"))
  def * : ProvenShape[Node] = (id, parent.?) <> (tupled, unapply)
  def tupled(node: (String, Option[String])): Node = Node(NodeId(node._1), node._2.map(NodeId.apply))
  def unapply(node: Node): Option[(String, Option[String])] = Some((node.id.self, node.parent.map(_.self)))
}

class Aliases(tag: Tag) extends Table[Alias](tag, "aliases") {
  def id = column[String]("alias_id", O.PrimaryKey, O.DBType("text"))
  def node = column[String]("node_id", O.DBType("text"))
  def * : ProvenShape[Alias] = (id, node) <> (tupled, unapply)
  def tupled(x: (String, String)): Alias = Alias(AliasId(x._1), NodeId(x._2))
  def unapply(x: Alias): Option[(String, String)] = Some((x.id.self, x.node.self))
}

class Links(tag: Tag) extends Table[Link](tag, "links") {
  def agent = column[String]("agent", O.DBType("text"))
  def left = column[String]("left_node_id", O.DBType("text"))
  def right = column[String]("right_node_id", O.DBType("text"))
  val id = primaryKey("primary_key", (agent, left, right))
  def * : ProvenShape[Link] = (agent, left, right) <> (tupled, unapply)
  def tupled(x: (String, String, String)): Link = Link(Agent(x._1), NodeId(x._2), NodeId(x._3))
  def unapply(x: Link): Option[(String, String, String)] = Some((x.agent.self, x.left.self, x.right.self))
}

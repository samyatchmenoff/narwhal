package narwhal.native.tables

import narwhal.model._

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.{Column, TableQuery, Query}
import scala.slick.lifted.ProvenShape
import scala.slick.ast.{Type, ScalaType, TypedType}

object columns {
  implicit val `MappedColumnType[AliasId, String]` = MappedColumnType.base[AliasId, String](_.self, AliasId)
  implicit val `MappedColumnType[NodeId, String]` = MappedColumnType.base[NodeId, String](_.self, NodeId)
  implicit val `MappedColumnType[Agent, String]` = MappedColumnType.base[Agent, String](_.self, Agent)
}

object tables {
  val nodes = TableQuery[Nodes]
  val aliases = TableQuery[Aliases]
  val links = TableQuery[Links]
  val ddl = nodes.ddl ++ aliases.ddl ++ links.ddl
}

import columns._

class Nodes(tag: Tag) extends Table[Node](tag, "nodes") {
  def id = column[NodeId]("node_id", O.PrimaryKey, O.DBType("text"))
  def parent = column[Option[NodeId]]("parent_node_id", O.Nullable, O.DBType("text"))
  def * : ProvenShape[Node] = (id, parent) <> (Node.tupled, Node.unapply)
}

class Aliases(tag: Tag) extends Table[Alias](tag, "aliases") {
  def id = column[AliasId]("alias_id", O.PrimaryKey, O.DBType("text"))
  def node = column[NodeId]("node_id", O.DBType("text"))
  def * : ProvenShape[Alias] = (id, node) <> (Alias.tupled, Alias.unapply)
}

class Links(tag: Tag) extends Table[Link](tag, "links") {
  def agent = column[Agent]("agent", O.DBType("text"))
  def left = column[NodeId]("left_node_id", O.DBType("text"))
  def right = column[NodeId]("right_node_id", O.DBType("text"))
  val id = primaryKey("primary_key", (agent, left, right))
  def * : ProvenShape[Link] = (agent, left, right) <> (Link.tupled, Link.unapply)
}

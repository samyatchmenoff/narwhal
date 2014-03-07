package narwhal.model

sealed trait Id[A <: Model] extends Any
sealed trait Model
sealed trait Query[A]
sealed trait Resource extends Any

class NodeId(val self: String)
  extends AnyVal
  with Id[Node]
  with Resource

class Agent(val self: String)
  extends AnyVal

class AliasId(val self: String)
  extends AnyVal
  with Id[Alias]
  with Resource

case class Node
  ( id: NodeId
  , parent: Option[NodeId]
  ) extends Model

case class Alias
  ( id: AliasId
  , nodeId: NodeId
  ) extends Model

case class Link
  ( agent: Agent
  , left: NodeId
  , right: NodeId
  ) extends Model
    with Id[Link]

case class Basic
  ( agent: Agent
  , left: Resource
  , right: Resource
  ) extends Query[Boolean]

package narwhal.native

import scala.concurrent._

import narwhal.model._
import narwhal.native.tables._
import scala.slick.lifted.{Column, TableQuery}
import narwhal.model.Query
import scala.slick.driver.PostgresDriver.simple._
import narwhal.native.tables.columns._
import scala.slick.driver.PostgresDriver

class Narwhal
  ( db: PostgresDriver.backend.type#DatabaseDef
  , executionContext: ExecutionContext
  ) extends narwhal.Narwhal {

  private type Session = PostgresDriver.backend.type#Session 
  private implicit val ExecutionContext = executionContext

  private val nodes = TableQuery[Nodes]
  private val aliases = TableQuery[Aliases]
  private val links = TableQuery[Links]

  private def withSession[A](body: Session => A):Future[A] =
    future(db.withSession(body))

  def get[A <: Model](id: Id[A]): Future[Option[A]] = withSession { implicit session =>
      id match {

        case x:Link =>
          links
            .where(_.agent === x.agent)
            .where(_.left === x.left)
            .where(_.right === x.right)
            .firstOption

        case x:NodeId =>
          nodes
            .where(_.id === x)
            .firstOption

        case x:AliasId =>
          aliases
            .where(_.id === x)
            .firstOption

    }
  }


  def delete[A <: Model](id: Id[A]): Future[Option[A]] =
    withSession { implicit session =>
      (id match {

        case x:Link =>
          links
            .where(_.agent === x.agent)
            .where(_.left === x.left)
            .where(_.right === x.right)
            .delete

        case x:NodeId =>
          nodes
            .where(_.id === x)
            .delete

        case x:AliasId =>
          aliases
            .where(_.id === x)
            .delete

      }) match {

        case _ => None

      }
    }

  def put[A <: Model](model: A): Future[Option[A]] = withSession { implicit session =>
    model match {

      case x:Node =>
        nodes += x
        Some(model)

    }
  }

  def query[A <: Query[B], B](q: A): Future[B] = ???

}

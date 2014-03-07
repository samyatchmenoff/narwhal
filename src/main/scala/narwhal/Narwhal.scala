package narwhal

import scala.concurrent.Future

import narwhal.model._

trait Narwhal {
  def get[A <: Model](id: Id[A]): Future[Option[A]]
  def delete[A <: Model](id: Id[A]): Future[Option[A]]
  def put[A <: Model](model: A): Future[Option[A]]
  def query[A <: Query[B], B](q: A): Future[B]
}

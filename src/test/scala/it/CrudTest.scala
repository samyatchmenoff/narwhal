package test.narwhal

import org.scalatest._
import org.scalatest.Matchers._
import org.scalatest.concurrent.ScalaFutures._
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import narwhal.native.Narwhal
import narwhal.model._
import scala.slick.driver.PostgresDriver.simple._
import narwhal.native.tables._
import scala.util.Random
import scala.slick.jdbc.{StaticQuery => Q}

class CrudTest extends TestHarness {

  dbTest("Create Node") { deps =>
    import deps.implicits._
    val node = Node(NodeId("a"), Some(NodeId("b")))
    tables.nodes += node
  }

  dbTest("Create Link") { deps =>
    import deps.implicits._
    val link = Link(Agent("a"), NodeId("a"), NodeId("b"))
    tables.links += link
  }

  dbTest("Create Alias") { deps =>
    import deps.implicits._
    val alias = Alias(AliasId("a"), NodeId("a"))
    tables.aliases += alias
  }

}


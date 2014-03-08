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

trait TestHarness extends FunSuite with BeforeAndAfterEach with OneInstancePerTest {

  private val dbName = s"narwhal_test_${Math.abs(Random.nextInt())}"

  case class Dependencies
    ( session: Session
    , context: ExecutionContext
    ) {
      object implicits {
        implicit val _session = session
        implicit val _context = context
      }
    }

  private lazy val db = Database.forURL(
    url = "jdbc:postgresql://127.0.0.1:5432/template1",
    driver = "org.postgresql.Driver"
  )

  private lazy val testDb = Database.forURL(
    url = s"jdbc:postgresql://127.0.0.1:5432/$dbName",
    driver = "org.postgresql.Driver"
  )

  override def beforeEach() {
    db.withSession(implicit s => Q.updateNA(s"CREATE DATABASE $dbName").execute())
    testDb.withSession(implicit s => tables.ddl.create)
  }

  override def afterEach() {
    db.withSession(implicit s => Q.updateNA(s"DROP DATABASE $dbName").execute())
  }

  def dbTest[A](name: String)(body: Dependencies => Unit):Unit = {
    val context = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
    test(name) {
      testDb.withSession(s => body(Dependencies(s, context)))
    }
  }

}


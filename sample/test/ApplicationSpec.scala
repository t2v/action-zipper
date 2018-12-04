import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends PlaySpec with GuiceOneAppPerTest {

  "Application" should {

    "send 404 on a bad request" in {
      val boum = route(app, FakeRequest(GET, "/boum")).get
      status(boum) must equal(NOT_FOUND)
    }

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      assert(status(home) === OK)
      assert(contentType(home) === Some("text/html"))
      assert(contentAsString(home).contains("Hello Action Zipper"))
    }
  }
}

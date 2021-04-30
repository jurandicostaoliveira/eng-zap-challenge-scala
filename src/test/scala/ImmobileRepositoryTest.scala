import br.com.zap.repository.ImmobileRepository
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ImmobileRepositoryTest extends AnyFlatSpec with Matchers {

  val repository = new ImmobileRepository()

  "The Collection" should "Cannot be empty" in {
    repository.all().count().toInt should be > 0
  }

}

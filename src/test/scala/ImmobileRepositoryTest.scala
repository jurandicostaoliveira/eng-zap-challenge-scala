import br.com.zap.domain.Immobile
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import collection.mutable.Stack
import br.com.zap.repository.ImmobileRepository
import org.apache.spark.rdd.RDD

class ImmobileRepositoryTest extends AnyFlatSpec with Matchers {

  val repository = new ImmobileRepository()

  "The Collection" should "Cannot be empty" in {
    repository.all().count().toInt should be > 0
  }

}

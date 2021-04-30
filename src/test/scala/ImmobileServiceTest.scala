import br.com.zap.service.{VivaService, ZapService}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ImmobileServiceTest extends AnyFlatSpec with Matchers {

  val zapService = new ZapService()
  val vivaService = new VivaService()

  // Zap

  it should "Zap Data Filter - throw NullPointerException if NULL value informed" in {
    a[NullPointerException] should be thrownBy {
      zapService.dataFilter(null)
    }
  }

  it should "Zap Data Map - throw NullPointerException if NULL value informed" in {
    a[NullPointerException] should be thrownBy {
      zapService.dataMap(null)
    }
  }

  // Viva

  it should "Viva Data Filter - throw NullPointerException if NULL value informed" in {
    a[NullPointerException] should be thrownBy {
      vivaService.dataFilter(null)
    }
  }

  it should "Viva Data Map - throw NullPointerException if NULL value informed" in {
    a[NullPointerException] should be thrownBy {
      vivaService.dataMap(null)
    }
  }

}

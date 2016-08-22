/**
 * Created by chrischivers on 22/08/16.
 */
import org.scalatest._

class UnitTest extends FlatSpec {

  val pricingRule = new PricingRule()

  val checkout = new Checkout(new PricingRule {})

}
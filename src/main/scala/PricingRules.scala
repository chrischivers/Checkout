/**
 * Created by chrischivers on 22/08/16.
 */
class PricingRules {

  private val pricingRules:Map[String,PricingRule] = ???

  def setNewPricingRule = ???
  def getPricingRule = ???
  def getAllPricingRules = ???
}


case class PricingRule(item:Item, unitPrice:Int, specialPrice:Option[SpecialMultiPrice])

case class SpecialMultiPrice(n:Int, y:Int)


/**
 * Created by chrischivers on 22/08/16.
 */
class PricingRules {

  private val pricingRules:Map[Item,PricingRule] = ???

  def setNewPricingRule(item:Item, pricingRule:PricingRule):Unit = ???
  def getPricingRule(item:Item):PricingRule = ???
  def getAllPricingRules:Map[Item,PricingRule] = ???
}

case class PricingRule(unitPrice:Int, specialPrice:Option[SpecialMultiPrice])

case class SpecialMultiPrice(n:Int, y:Int)


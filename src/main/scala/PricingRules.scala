/**
 * Created by chrischivers on 22/08/16.
 */
class PricingRules {

  private var pricingRules:Map[Item,PricingRule] = Map()

  def setNewPricingRule(item:Item, pricingRule:PricingRule):Unit = ???
  def getPricingRule(item:Item):PricingRule = ???
  def getAllPricingRules:Map[Item,PricingRule] = ???
}


trait PricingRule {
  val unitPrice:Int
}

case class NormalPricingRule(unitPrice:Int) extends PricingRule

case class SpecialMultiPriceRule(unitPrice:Int, n:Int, y:Int) extends PricingRule


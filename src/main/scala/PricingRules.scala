class PricingRules {

  private var pricingRules:Map[Item,PricingRule] = Map()

  def setNewPricingRule(item:Item, pricingRule:PricingRule):Unit = pricingRules += (item -> pricingRule)
  def getPricingRule(item:Item):Option[PricingRule] = pricingRules.get(item)
  def getAllPricingRules:Map[Item,PricingRule] = pricingRules
}


trait PricingRule {
  val unitPrice:Int
}

case class NormalPricingRule(unitPrice:Int) extends PricingRule

case class SpecialMultiPriceRule(unitPrice:Int, n:Int, y:Int) extends PricingRule {
  if (unitPrice > y) throw new IllegalArgumentException("the unit price cannot be more than the price of the special multibuy price")
}


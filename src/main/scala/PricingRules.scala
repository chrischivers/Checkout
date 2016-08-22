/**
 * Holds pricing rules for items in the supermarket
 * Items can only have one pricing rule attached
 */
class PricingRules {
  private var pricingRules:Map[Item,PricingRule] = Map()
  def setNewPricingRule(item:Item, pricingRule:PricingRule):Unit = pricingRules += (item -> pricingRule)
  def getPricingRule(item:Item):Option[PricingRule] = pricingRules.get(item)
  def getAllPricingRules:Map[Item,PricingRule] = pricingRules
}


trait PricingRule {
  val unitPrice:Int
}

/**
 * For pricing without any special multi buy prices
 * @param unitPrice The price of the item
 */
case class NormalPricingRule(unitPrice:Int) extends PricingRule

/**
 * For pricing with special multi buy offers attached
 * @param unitPrice The price of the item (without multi buy)
 * @param n The number needed to be purchased to qualify for special price
 * @param y The special price for n items
 */
case class SpecialMultiPriceRule(unitPrice:Int, n:Int, y:Int) extends PricingRule {
  if (unitPrice > y) throw new IllegalArgumentException("the unit price cannot be more than the price of the special multibuy price")
}


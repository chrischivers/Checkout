import scala.collection.mutable.ListBuffer

class Checkout(pricingRules: PricingRules) {

  // Buffer containing scanned items for this checkout
  private var scannedItems: ListBuffer[Item] = ListBuffer()

  /**
   * Handles the scanning of items. If no price rule exists for an item, it will be rejected.
   * Scanned items are added to the scannedItems List
   * @param item The item to be scanned
   * @return No return
   */
  def scanItem(item: Item) = {
    if (pricingRules.getPricingRule(item).isDefined) {
      println("Item " + item.sku + " scanned successfully")
      scannedItems += item
    }
    else throw new IllegalArgumentException("No pricing rule defined for item. Unable to scan.")
  }

  /**
   * Calculates the total for all the scanned item
   * @return The total amount
   */
  def getTotal: Int = scannedItems.groupBy(identity).mapValues(_.size)   // Map unique items against their count
     .foldLeft(0)((acc,keyValue) => pricingRules.getPricingRule(keyValue._1).get match {
       case npr:NormalPricingRule => (npr.unitPrice * keyValue._2) + acc
       case smpr:SpecialMultiPriceRule => (((keyValue._2/smpr.n) * smpr.y) + ((keyValue._2 % smpr.n) * smpr.unitPrice)) + acc
     })

}


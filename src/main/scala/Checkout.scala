import scala.collection.mutable.ListBuffer

class Checkout(pricingRules:PricingRules) {
  
  private var scannedItems:ListBuffer[Item] = ListBuffer()

  def scanItem(item:Item) = {
    if (pricingRules.getPricingRule(item).isDefined) {
      println("Item " + item.sku + " scanned successfully")
      scannedItems += item
    }
    else throw new IllegalArgumentException("No pricing rule defined for item. Unable to scan.")
  }

   def getTotal:Int = {
     scannedItems.groupBy(identity).mapValues(_.size) //Map unique items against their count
     .foldLeft(0)((acc,keyValue) => pricingRules.getPricingRule(keyValue._1).get match {
       case npr:NormalPricingRule => (npr.unitPrice * keyValue._2) + acc
       case smpr:SpecialMultiPriceRule => (((keyValue._2/smpr.n) * smpr.y) + ((keyValue._2 % smpr.n) * smpr.unitPrice)) + acc
     })
   }

}


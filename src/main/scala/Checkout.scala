/**
 * Created by chrischivers on 22/08/16.
 */

protected class Checkout(pricingRules:PricingRules) {
  
  private var scannedItems:List[Item] = List()

  def scanItem(item:Item) = scannedItems = scannedItems :+ item
  def getTotal:Int = ???


}


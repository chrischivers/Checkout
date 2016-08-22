import scala.collection.mutable.ListBuffer

/**
 * Created by chrischivers on 22/08/16.
 */

protected class Checkout(pricingRules:PricingRules) {
  
  private var scannedItems:ListBuffer[Item] = ListBuffer()

  def scanItem(item:Item) = scannedItems += item

  def getTotal:Int = ???


}


/**
 * Created by chrischivers on 22/08/16.
 */

object Supermarket {
  def getNewCheckout(pricingRules: PricingRules) = new Checkout(pricingRules)
}

protected class Checkout(pricingRules:PricingRules) {

  def readItem(item:Item) = ???
  def getTotal:Int = ???


}


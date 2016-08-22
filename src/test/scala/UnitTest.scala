/**
 * Created by chrischivers on 22/08/16.
 */
import org.scalatest._

class UnitTest extends FlatSpec with Matchers {

  "Items" should "have unique SKUs" in {
    new Item("A")

    a[IllegalStateException] should be thrownBy {
      new Item("A")
    }
  }

  "Items" should "have an SKU which is a single letter of the alphabet (capitalised)" in {
    a[IllegalArgumentException] should be thrownBy {
      new Item("b")
    }

    a[IllegalArgumentException] should be thrownBy {
      new Item("Abc")
    }

    a[IllegalArgumentException] should be thrownBy {
      new Item("123")
    }
  }
  "Pricing rules" should "accept aspecialPrice if available" in {
    val itemA = new Item("A")
    val multiPrice = new SpecialMultiPrice(3, 100) //3 for 100
    val pricingRuleItemA = new PricingRule(60, Some(multiPrice))
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)
  }

  "Pricing rules" should "accept a None for specialPrice if no special price is available" in {
    new PricingRule(50,None)
  }

  "Special Multi Price Discounts" should "be be priced higher than the unit cost of a single item" in {
    val itemA = new Item("A")
    val multiPrice = new SpecialMultiPrice(3, 100) //3 for 100

    a[IllegalStateException] should be thrownBy {
      val pricingRule = new PricingRule(150, Some(multiPrice))
    }
  }

  "Pricing Rules" should "accept the price rule for an item and allow it to be retrieved" in {
    val itemA = new Item("A")
    val pricingRuleItemA = new PricingRule(50,None)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)
    pricingRules.getPricingRule(itemA) should be (pricingRuleItemA)
  }

  "Pricing Rules" should "accept the price rule for an item and include it in complete set" in {
    val itemA = new Item("A")
    val pricingRuleItemA = new PricingRule(50,None)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)
    pricingRules.getAllPricingRules(itemA) should be (pricingRuleItemA)
  }

  "Pricing Rules" should "have one entry per item only" in {
    val itemA = new Item("A")
    val pricingRuleItemA = new PricingRule(50,None)
    val pricingRuleItemADuplicate = new PricingRule(10,None)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)

    a[IllegalStateException] should be thrownBy {
      pricingRules.setNewPricingRule(itemA, pricingRuleItemADuplicate)
    }
  }

  "Checkout" should "provide the correct total based on the pricing rules (no special offers)" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val pricingRuleItemA = new PricingRule(50,None)
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)

    val itemB = new Item("B")
    val pricingRuleItemB = new PricingRule(30,None)
    pricingRules.setNewPricingRule(itemB, pricingRuleItemB)

    val checkout = Supermarket.getNewCheckout(pricingRules)
    checkout.readItem(itemA)
    checkout.readItem(itemB)
    checkout.getTotal should be (80)
  }

  "Checkout" should "accept multiple items without any special offer and total them correctly" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val pricingRuleItemA = new PricingRule(50,None)
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)


    val checkout = Supermarket.getNewCheckout(pricingRules)
    checkout.readItem(itemA)
    checkout.readItem(itemA)
    checkout.readItem(itemA)
    checkout.getTotal should be (150)
  }
}
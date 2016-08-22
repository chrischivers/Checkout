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
  "Pricing rules" should "accept a specialPrice if available" in {
    val itemA = new Item("A")
    val pricingRuleItemA = new SpecialMultiPriceRule(unitPrice = 60, n = 3, y = 100) //3 for 100
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)
  }

  "Pricing rules" should "accept a normal price if special price not available" in {
    val itemA = new Item("A")
    val pricingRuleItemA = new NormalPricingRule(50)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)
  }

  "Special Multi Price Discounts" should "be be priced higher than the unit cost of a single item" in {
    val itemA = new Item("A")

    a[IllegalStateException] should be thrownBy {
      val multiPrice = new SpecialMultiPriceRule(unitPrice = 150, n = 3, y = 100) //150 each, 3 for 100
    }
  }

  "Pricing Rules" should "accept the price rule for an item and allow it to be retrieved" in {
    val itemA = new Item("A")
    val pricingRuleItemA = new NormalPricingRule(50)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)
    pricingRules.getPricingRule(itemA) should be (pricingRuleItemA)
  }

  "Pricing Rules" should "accept the price rule for an item and include it in complete set" in {
    val itemA = new Item("A")
    val pricingRuleItemA = new NormalPricingRule(50)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)
    pricingRules.getAllPricingRules(itemA) should be (pricingRuleItemA)
  }

  "Pricing Rules" should "have one entry per item only" in {
    val itemA = new Item("A")
    val pricingRuleItemA = new NormalPricingRule(50)
    val pricingRuleItemADuplicate = new NormalPricingRule(10)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)

    a[IllegalStateException] should be thrownBy {
      pricingRules.setNewPricingRule(itemA, pricingRuleItemADuplicate)
    }
  }

  "Checkout" should "provide the correct total based on the pricing rules (no special offers)" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val pricingRuleItemA = new NormalPricingRule(50)
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)

    val itemB = new Item("B")
    val pricingRuleItemB = new NormalPricingRule(30)
    pricingRules.setNewPricingRule(itemB, pricingRuleItemB)

    val checkout = Supermarket.getNewCheckout(pricingRules)
    checkout.readItem(itemA)
    checkout.readItem(itemB)
    checkout.getTotal should be (80)
  }

  "Checkout" should "accept multiple items without any special offer and total them correctly (no special offers)" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val pricingRuleItemA = new NormalPricingRule(50)
    pricingRules.setNewPricingRule(itemA, pricingRuleItemA)


    val checkout = Supermarket.getNewCheckout(pricingRules)
    checkout.readItem(itemA)
    checkout.readItem(itemA)
    checkout.readItem(itemA)
    checkout.getTotal should be (150)
  }

  "Checkout" should "provide the correct total based on the pricing rules (with special offers)" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val SpecialMultiPriceItemA = new SpecialMultiPriceRule(unitPrice = 50, n = 3, y = 130)
    pricingRules.setNewPricingRule(itemA, SpecialMultiPriceItemA)


    val checkout = Supermarket.getNewCheckout(pricingRules)
    checkout.readItem(itemA)
    checkout.readItem(itemA)
    checkout.readItem(itemA)
    checkout.getTotal should be (150)
  }
}
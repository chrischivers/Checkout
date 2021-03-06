import org.scalatest._

/**
 * The unit tests for the Checkout
 */
class UnitTest extends FlatSpec with Matchers {

  override def withFixture(test: NoArgTest) = {

    // This fixture resets the existingItems set between tests
    super.withFixture(test) match {
      case outcome:Outcome => Item.existingItems = Set[Item]()
        outcome
    }
  }

  "Items" should "have unique SKUs" in {
    new Item("A")

    a[IllegalArgumentException] should be thrownBy {
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
    val specialPricingRuleItemA = new SpecialMultiPriceRule(unitPrice = 60, n = 3, y = 100) //3 for 100
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, specialPricingRuleItemA)
  }

  "Pricing rules" should "accept a normal price if special price not available" in {
    val itemA = new Item("A")
    val normalPricingRuleItemA = new NormalPricingRule(50)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, normalPricingRuleItemA)
  }

  "Special Multi Price Discounts" should "be be priced higher than the unit cost of a single item" in {
    val itemA = new Item("A")

    a[IllegalArgumentException] should be thrownBy {
      val multiPrice = new SpecialMultiPriceRule(unitPrice = 150, n = 3, y = 100) //150 each, 3 for 100
    }
  }

  "Pricing Rules" should "accept the price rule for an item and allow it to be retrieved" in {
    val itemA = new Item("A")
    val normalPricingRuleItemA = new NormalPricingRule(50)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, normalPricingRuleItemA)
    pricingRules.getPricingRule(itemA).get should be (normalPricingRuleItemA)
  }

  "Pricing Rules" should "return a None option for items not existing in the set" in {
    val itemA = new Item("A")
    val pricingRules = new PricingRules
    pricingRules.getPricingRule(itemA) should be (None)
  }

  "Pricing Rules" should "accept the price rule for an item and include it in complete set" in {
    val itemA = new Item("A")
    val normalPricingRuleItemA = new NormalPricingRule(50)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, normalPricingRuleItemA)
    pricingRules.getAllPricingRules(itemA) should be (normalPricingRuleItemA)
  }

  "Pricing Rules" should "have one entry per item only. A new entry for an existing item should overwrite the previous rule." in {
    val itemA = new Item("A")
    val normalPricingRuleItemA = new NormalPricingRule(50)
    val pricingRules = new PricingRules
    pricingRules.setNewPricingRule(itemA, normalPricingRuleItemA)
    pricingRules.getPricingRule(itemA).get should be (normalPricingRuleItemA)
    pricingRules.getPricingRule(itemA).get.unitPrice should be (50)

    val newNormalPricingRuleItemA = new NormalPricingRule(10)

    pricingRules.setNewPricingRule(itemA, newNormalPricingRuleItemA)
    pricingRules.getPricingRule(itemA).get should be (newNormalPricingRuleItemA)
    pricingRules.getPricingRule(itemA).get.unitPrice should be (10)

  }

  "Checkout" should "provide the correct total based on the pricing rules (no special offers)" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val normalPricingRuleItemA = new NormalPricingRule(50)
    pricingRules.setNewPricingRule(itemA, normalPricingRuleItemA)

    val itemB = new Item("B")
    val normalPricingRuleItemB = new NormalPricingRule(30)
    pricingRules.setNewPricingRule(itemB, normalPricingRuleItemB)

    val checkout = new Checkout(pricingRules)
    checkout.scanItem(itemA)
    checkout.scanItem(itemB)
    checkout.getTotal should be (80)
  }

  "Checkout" should "accept multiple items without any special offer and total them correctly (no special offers)" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val normalPricingRuleItemA = new NormalPricingRule(50)
    pricingRules.setNewPricingRule(itemA, normalPricingRuleItemA)


    val checkout = new Checkout(pricingRules)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.getTotal should be (150)
  }

  "Checkout" should "provide the correct total based on the pricing rules (with special offers)" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val specialMultiPriceItemA = new SpecialMultiPriceRule(unitPrice = 50, n = 3, y = 130)
    pricingRules.setNewPricingRule(itemA, specialMultiPriceItemA)

    val checkout = new Checkout(pricingRules)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.getTotal should be (130)
  }

  "Checkout" should "provide the correct total based on the pricing rules (with special offers) even if scanned in different order" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val specialMultiPriceItemA = new SpecialMultiPriceRule(unitPrice = 50, n = 3, y = 130)
    pricingRules.setNewPricingRule(itemA, specialMultiPriceItemA)

    val itemB = new Item("B")
    val normalPricingRuleItemB = new NormalPricingRule(40)
    pricingRules.setNewPricingRule(itemB, normalPricingRuleItemB)

    val checkout = new Checkout(pricingRules)
    checkout.scanItem(itemA)
    checkout.scanItem(itemB)
    checkout.scanItem(itemA)
    checkout.scanItem(itemB)
    checkout.scanItem(itemA)
    checkout.getTotal should be (210)
  }

  "Checkout" should "provide the correct total based on multiple Special Prices in one transaction" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val specialMultiPriceItemA = new SpecialMultiPriceRule(unitPrice = 50, n = 3, y = 130)
    pricingRules.setNewPricingRule(itemA, specialMultiPriceItemA)

    val itemB = new Item("B")
    val specialMultiPriceItemB = new SpecialMultiPriceRule(unitPrice = 30, n = 2, y = 45)
    pricingRules.setNewPricingRule(itemB, specialMultiPriceItemB)

    val checkout = new Checkout(pricingRules)
    checkout.scanItem(itemA)
    checkout.scanItem(itemB)
    checkout.scanItem(itemA)
    checkout.scanItem(itemB)
    checkout.scanItem(itemA)
    checkout.getTotal should be (175)
  }

  "Checkout" should "provide the correct total based on purchases of the same item exceeding the special price requirement (special price rule applied only once)" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val specialMultiPriceItemA = new SpecialMultiPriceRule(unitPrice = 50, n = 3, y = 130)
    pricingRules.setNewPricingRule(itemA, specialMultiPriceItemA)

    val checkout = new Checkout(pricingRules)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.getTotal should be (180)
  }

  "Checkout" should "provide the correct total based on purchases of the same item exceeding the special price requirement (special price rule applied more than once)" in {
    val pricingRules = new PricingRules

    val itemA = new Item("A")
    val specialMultiPriceItemA = new SpecialMultiPriceRule(unitPrice = 50, n = 3, y = 130)
    pricingRules.setNewPricingRule(itemA, specialMultiPriceItemA)

    val checkout = new Checkout(pricingRules)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.scanItem(itemA)
    checkout.getTotal should be (260)
  }


  "Checkout" should "not accept a scanned item if a pricing rule does not exist for that item" in {
    val pricingRules = new PricingRules
    val itemA = new Item("A")

    val checkout = new Checkout(pricingRules)
    a[IllegalArgumentException] should be thrownBy {
      checkout.scanItem(itemA)
    }
  }
}
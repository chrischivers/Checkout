object Demo extends App {

  // Create items
  val itemA = new Item("A")
  val itemB = new Item("B")
  val itemC = new Item("C")
  val itemD = new Item("D")


  //Set pricing rules
  val pricingRules = new PricingRules

  val specialMultiPriceItemA = new SpecialMultiPriceRule(unitPrice = 50, n = 3, y = 130)
  pricingRules.setNewPricingRule(itemA, specialMultiPriceItemA)

  val specialMultiPriceItemB = new SpecialMultiPriceRule(unitPrice = 30, n = 2, y = 45)
  pricingRules.setNewPricingRule(itemB, specialMultiPriceItemB)

  val normalPriceItemC = new NormalPricingRule(20)
  pricingRules.setNewPricingRule(itemC, normalPriceItemC)

  val normalPriceItemD = new NormalPricingRule(15)
  pricingRules.setNewPricingRule(itemD, normalPriceItemD)

  // Load checkout and scan items

  val checkout = new Checkout(pricingRules)
  checkout.scanItem(itemD)
  checkout.scanItem(itemA)
  checkout.scanItem(itemB)
  checkout.scanItem(itemC)
  checkout.scanItem(itemA)
  checkout.scanItem(itemB)
  checkout.scanItem(itemA)
  println("Total = " + checkout.getTotal)

  // 3 x Item A = 130 (special price)
  // 2 x Item B = 45 (special price)
  // 1 x Item C = 20
  // 1 x Item D = 15
  // Total = 210
}

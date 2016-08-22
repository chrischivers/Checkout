/**
 * Created by chrischivers on 22/08/16.
 */

case class Item(sku:String) {
  //Checks SKU is in valid format
  if(sku.length > 1 || sku.charAt(0).isLower || !sku.charAt(0).isLetter)
    throw new IllegalArgumentException("Invalid SKU format. SKY must be single capital letter.")

  // Checks item with same SKU does not already exist
  if (Item.existingItems.contains(this)) throw new IllegalArgumentException("Item already exists")
  else Item.existingItems += this
}

object Item {
  var existingItems = Set[Item]()
}
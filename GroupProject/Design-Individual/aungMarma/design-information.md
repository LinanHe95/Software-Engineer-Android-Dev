1. To realize this requirement I added three classes: a) GroceryList, b) ItemType and c) Item.
GroceryList has the aggregrate relationship with ItemType and it has methods add(ItemType), delete(ItemType), changeQuantity(ItemType, newQuantity). 
Item is a subclass of ItemType. So a GroceryList can have multiple items and it can add, delete and change the quantity of items via available methods.

2. To realize this requirement, I added a class DatabaseConnection with methods specify(itemName), create(ItemType) and save(ItemType). It has the composite relationship with ItemType.
    
3. To realize this requirement, I added the inheritence relationship between Item and ItemType. Item has method setQuantity(newQuantity) to specify a quantity.

4. To realize this requirement, I added a method specify(itemName) on class DatabaseConnection. Asking users for the name and looking up on the database does not affect the design directly.

5. Not considered because it does not affect the design directly.

6. To realize this requirement, I added a method checkOff(ItemType) on class GroceryList.

7. Users must also be able to clear all the check-off marks in a list at once.
To realize this requirement, I added a method clearCheckOffMarks() on class GroceryList.

8. Not considered because it does not affect the design directly.

9. To realize this requirement, we have inheritance relationship between ItemType and Item.

10. Not considered because it does not affect directly the design of a system that manages only a grocery list.

11. Not considered because it does not affect the design directly.

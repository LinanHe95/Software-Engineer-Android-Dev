# UML Class Diagram
1. The application has a GroceryList class that aggregates the Item class. The GroceryList class has operations addItem, deleteItem, changeQuantity which the user will be able to call on to do the corresponding actions.
2. The application has a Database that has an association of many Items and many ItemTypes. Both Items and ItemTypes have the attribute name defined by a String to specify it.
3. In order to fulfill this requirement, I simply gave the addItem operation in GroceryList the parameters ItemType, Item, and Integer. Since how this is exactly implemented is not represented by a class diagram, nothing further is shown on the diagram. To specify the quantity, the Item class has a quantity attribute of type int.
4. The GroceryList has a dependency on the Database so that it can search for the item specified. This operation is also specified in GroceryList by the searchAddItem operation with a String parameter
5. This is achieved by the save operation in GroceryList
6. This is achieved by the checkOff operation in GroceryList with a parameter Item
7. This is achieved by the clearChecks operation in GroceryList
8. The implementation of this is achieved by the save operation already specified
9. This is achieved by the group operation in GroceryList. How this works does not have to be specified in the diagram as that is more of a design view.
10. This is represented by the aggregation of GroceryLists by the User. The User has the operations createList, renameList, selectList, and deleteList to do the corresponding actions. To specify each list, GroceryList has the attribute name defined by a String.
11. This does not need to be shown in the class diagram as it is more of a design aspect.
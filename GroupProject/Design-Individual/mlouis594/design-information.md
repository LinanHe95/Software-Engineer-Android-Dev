

Marlon Louis

Assignment 5

Design Information

\1. The design includes several different classes to implement all the functionality of the

Grocery List. The user will interact with the Main class to create, delete, or adjust their

lists. It is from this Main class that the user will be able to interact with the database to

have all auxiliary information at their disposal.

\2. The application must contain a database. To implement this I included a database class

that has collections of all known types and all known products. The database will also

store all the user’s lists and save their states.

\3. Users can see all available types they wish to choose from by interacting with the

database from the Main class. After choosing the desired type, the user can see all

products that are of that specified type and choose the product they wish to add to their

list. After choosing their product they will be able to specify the quantity and add the

product to the list that is in focus in the Main class.

\4. Users will be able to search the database from the Main class. They will be able to search

a product name and be prompted to add the product to their list if it is found. If the

product can’t be found, then the user will be asked to specify the type and the database

will add this product to it's records. This is possible because the database has methods to

search all known products and also has the functionality to update it's record if a search

result is not found.

\5. The Main class has a function, saveList, that will save the state of the list that is in focus

and save this state in the database. This function will automatically be called every time

the user makes some change to their list

\6. To be able to check off products, the Product class has a “checked-off’ attribute. This will

indicate whether a product is checked or not. The user will have the ability to check off

products in their list from the Main class by modifying the list that they have in focus.

\7. The GroceryList class has a function that will allow users to reset the checked attribute of

all products that are in that lst

\8. Once an Item is checked off in a list, the state of the list is automatically saved in the

database. Any change to a list that is in focus will automatically be saved in the database

\9. The GroceryList class stores a collection of Types. Types store collections of products

that belong under that type. Thus when in the GroceryList all products will automatically

be grouped by type. If a new product is added to a GroceryList and it's type is not already

in the list, the Type will be added to the GroceryList, then the product will be added

under the appropriate Type.

\10. Users have the option to create, name, rename, and delete multiple Lists from the Main

class. Those lists are stored in the database. The user will also have the option to swap the

list that is in focus from the Main class. Allowing the user to jump between the different

lists they may have created



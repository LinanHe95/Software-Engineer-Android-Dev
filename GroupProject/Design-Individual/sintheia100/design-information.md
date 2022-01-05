A grocery list consists of items the users want to buy at a grocery store. The application
must allow users to add items to a list, delete items from a list, and change the quantity
of items in the list (e.g., change from one to two pounds of apples).
 
-This is achieved by creating a list of grocery items, where item is an entity in itself. This list is titled in the UML diagram as GroceryList. GroceryList has methods to add, delete and edit grocery items. 

2. The application must contain a databse (DB) of reminders and corresponding data. 
-This is shown in the UML diagram by representing database and what data will be stored and in which tables. 

3. Users must be able to add items to a list by picking them from a hierarchical list, where
the first level is the item type (e.g., cereal), and the second level is the name of the
actual item (e.g., shredded wheat). After adding an item, users must be able to specify a
quantity for that item
-In ItemList, one of the Attributes of the list is the GroceryItemList where we can just have lists of items that are only cereal. The item class also has a "type" and "name" attribute for this purpose. 

4. Users must also be able to specify an item by typing its name. In this case, the
application must look in its DB for items with similar names and ask the users, for each
of them, whether that is the item they intended to add. If a match cannot be found, the
application must ask the user to select a type for the item and then save the new item,
together with its type, in its DB.
-To realize this requirement I have added the tables that may be used in the databases. When the user starts typing the application can search the database if such strings/item list already exist, if so then it can suggest the items and save it thereafter. 

5. Lists must be saved automatically and immediately after they are modified.
-GroceryItemList has a method saveGroceryItem which is part of all of the methods in the list and it saves the list automatically as it is called after every method. 

6. Users must be able to check off items in the list (without deleting them).

--Each object of a items class has an attribute of type boolean that will identify whether its crossed off or not as per the isCheckedOff attribute.

7. Users must also be able to clear all the check-off marks in the grocery list at once.

--There is a method in listItemReminders class that is responsible for obtaining all items in the DB pertaining to the list by identifying every item that shares the value of belongingList attribute in each item object and resets there boolean value of isCheckedOff to flase


8. Check-off marks for the grocery list are persistent and must also be saved immediately.

--isCheckedOff attribute in the GroceryList class is saved and carried with the object to the database as soon as the object is finished being edited via the CRUD for items in DB connection


9. The application must present the items grouped by type.

--Since this is a UI element it won't fully reflect in the UML but each item object has a ItemType attribute that is saved with the groceryList thus making each item to be grouped by their item type when needed


10.The application must support multiple lists at a time (e.g., “weekly grocery list”, “monthly
farmer’s market list”). Therefore, the application must provide the users with the ability to
create, (re)name, select, and delete lists

--The listView class is a collection of objects of type listItems which also lets the users have access to the following methods to create, (re)name, select, and delete items lists
createList(listName)
renameList(selectList, newListName)
deleteList(listName)

11. The User Interface (UI) must be intuitive and responsive.


--There are three attributes that handle this aspect in the GroceryList class. The isAllDay boolean determines whether the items will just be an all day instance and will set the dateTime attribute to null, however if isAllDay is set to false it will ask for a dateTime to be entered and the repeatsItems attribute executes the RepeatsItems class in which a user can set an endingDate for the series as well as how concurrently this items repeats

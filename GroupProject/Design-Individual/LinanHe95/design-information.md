GrocceryListManager
1. A grocery list consists of items the users want to buy at a grocery store. The application
must allow users to add items to a list, delete items from a list, and change the quantity
of items in the list (e.g., change from one to two pounds of apples).
{have a class to create users(), each has addItem(), deleteItems(), ModifyItemAmount()}

2. The application must contain a database (DB) of items and corresponding item types.
{in the database there should be meat, veg, drinks, grains,...}

3. Users must be able to add items to a list by picking them from a hierarchical list, where
the first level is the item type (e.g., cereal), and the second level is the name of the
actual item (e.g., shredded wheat). After adding an item, users must be able to specify a
quantity for that item.
{create hierarchical() for the catgerials, to look into the specific items and create itemAmount(): amount is need to buy }

4. Users must also be able to specify an item by typing its name. In this case, the
application must look in its DB for items with similar names and ask the users, for each
of them, whether that is the item they intended to add. If a match cannot be found, the
application must ask the user to select a type for the item and then save the new item,
together with its type, in its DB.
{create search bar, in that search bar call on searchItemName()}

5. Lists must be saved automatically and immediately after they are modified.
{create autoSave(), after each same user modified anything}

6. Users must be able to check off items in a list (without deleting them).
{create checkItem(), to allow user to check off their list items.}

7. Users must also be able to clear all the check-off marks in a list at once.
{create uncheckItem(), to allow user to uncheck off their list items.}

8. Check-off marks for a list are persistent and must also be saved immediately.
{create saveCheck(), to call on auto save which time user check and uncheck a list.}

9. The application must present the items in a list grouped by type, so as to allow users to
shop for a specific type of products at once (i.e., without having to go back and forth
between aisles).
{create method called sortItems(), it will sort by meat, veg,... and their amount}

10. The application must support multiple lists at a time (e.g., “weekly grocery list”, “monthly
farmer’s market list”). Therefore, the application must provide the users with the ability to
create, (re)name, select, and delete lists.
{create numberList(): to allow to multiply list as much as they need. create nameList(): allow user
 to rename the list litle, create selectList(): to also let user check or uncheck the lists.
 create deleteList():will move the unwanted list to the trash bin}

11. The User Interface (UI) must be intuitive and responsive.
{unneed for diagram and design.}
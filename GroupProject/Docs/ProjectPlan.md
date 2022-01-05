# Project Plan Version 2.0

**Author**: \<Team2\>

## 1 Introduction:
 *GroceryListManager, this app manages grocery lists, allowing users to add items to the list, delete items from the list, and change the quantities of items in the list.*

## 2 Process Description:
*GroceryListManager: An App for Managing Grocery Lists.*

### Create List
_The user must be able to create a list. The user can differentiate between lists by providing a name for the GroceryList._

__Entrance Criteria:__ The user provides a string to be the name of the list. The list may contain spaces but must otherwise be alphanumeric. The name of the list cannot be the same as that of an already existing list.
__Exit Criteria:__ A new list is created and visible to the user.

### Rename List
_The user must be able to rename a list._

__Entrance Criteria:__ The user provides a string to be the new name of the list. The list may contain spaces but must otherwise be alphanumeric. The name of the list cannot be the same as that of an already existing list.
__Exit Criteria:__ A list with the same contents is now displayed with a different name.

### Delete List
_The user must be able to delete a list._

__Entrance Criteria:__ The user selects a list they wish to be deleted.
__Exit Criteria:__ The list and its contents are deleted from the device and no longer visible to the user.

### Enter List
_The user must be able to see the contents of each list_

__Entrance Criteria:__ The user selects a list they wish to view.
__Exit Criteria:__ The contents of the list which are items associated with the list are visible to the user.

### Add Item Hierarchically
_The user must be able to choose an item from a hierarchical list. The user must choose an ItemType corresponding to the Item they are attempting to add, and then choose the correct Item. The user will then be prompted for a quantity of the item and it will be added to the list._

__Entrance Criteria:__ The user selected an ItemType and then an associated Item that the user would like to add. The user then provides and integer for quantity.
__Exit Criteria:__ The list will show the item that was just added under its associated category on the list.

### Add Item By Search
_The user must be able to choose an by searching for it with a string. Results with relevent Items that are currently held in the database are presented. After selecting the Item, the user must provide a number to represent the quantity of that Item that is required._ 

__Entrance Criteria:__ The user provides a string to refine the view of the items available, and then selects the Item they would like to add. The user then provides and integer for quantity.
__Exit Criteria:__ The list will show the item that was just added under its associated category on the list.

### Add Item To Database
_If the user cannot find the Item desired when searching by a string, the user is able to add the Item to the database. The user provides the ItemType for the Item they would like to add. A new entry is made in the database for this Item._
__Entrance Criteria:__ Item is searched, but not found in the database. User will be prompted to add item to the database by providing an ItemType to associate the Item they add with.
__Exit Criteria:__ User adds the item to the database.

### Modify Quantity of Item
_The user must be able to change the quantity of the Items that are contained within a list._ 

__Entrance Criteria:__ The user presses the quantity value next to the Item whose quantity they want to change. The user then provides an integer to be the new quantity.
__Exit Criteria:__ The Item is displayed with a different value for quantity.

### Delete Item
_The user must be able delete an Item from a list._ 

__Entrance Criteria:__ The user pushes the delete button next to the Item they would like to delete
__Exit Criteria:__ The GroceryList no longer shows the Item that was removed

### Check Off
_The user must be able to check off an Item to indicate that they have already picked that Item up. The user taps the checkbox next to the Item of their choice and indicating that they no longer need to search for that Item. In addition to this, the user must also be able to clear the checks off of all the items, leaving the list as if all of the Items are still required._ 

__Entrance Criteria:__ The user taps on the checkbox next to an Item.
__Exit Criteria:__ The Item has a checkmark next to it indicating that it has been picked up.

### Clear Checks
_The user must be able to clear the checks off of all the items, leaving the list as if all of the Items are still required._ 

__Entrance Criteria:__ The user taps on the button labeled Clear Checks.
__Exit Criteria:__ The list contents now have all checkmarks unchecked as if nothing has been picked up yet.

## 3 Team:
**Members:**
Aungkyawcha Marma
Kevin Cardenas
Linan He
Marlon Louis
Sintheia Jerin Chowdhury
Gurjinder (Rocky) Singh

**Roles:**

1. Project Manager - Checks in with all team members to discuss work, analyze progress, and make sure the team is on track to meet deadlines.
2. FullStack: Works with both the front end and back end team to ensure that both are linked successfully to ensure efficient implementation
3. FrontEnd: Makes a connection between the clint and the deliver business solution. So, responsible for proper communication.
4. BackEnd: Implements the requirements into the functionality of the app. Also connects the database to the application.
5. QA/Tester: Creates the test cases and implements them for the program. If a bug is found, the tester reports the bug to the group for patches.

| Team Members| Roles| Description|
|:---|:---|:---|
|Kevin Cardenas | Project Manager|Checks in with all team members to discuss work, analyze progress, and make sure the team is on track to meet deadlines.|
|Aungkyawcha Marma | Full Stack |Works with both the front end and back end team to ensure that both are linked successfully to ensure efficient implementation |
|Sintheia Jerin Chowdhury | Front End | Makes a connection between the clint and the deliver business solution. So, responsible for proper communication.|
|Marlon Louis | Back End | Implements the requirements into the functionality of the app. Also connects the database to the application. |
|Gurjinder Singh | Back End | Implements the requirements into the functionality of the app. Also connects the database to the application.|
|Linan He | QA Tester| Creates the test cases and implements them for the program. If a bug is found, the tester reports the bug to the group for patches.|
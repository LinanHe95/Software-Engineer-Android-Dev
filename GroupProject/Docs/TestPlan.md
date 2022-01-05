# Test Plan Version 2.0

**Author**: \<Team2\>

## 1 Testing Strategy

### 1.1 Overall strategy

- Unit testing will be performed by the QA tester on each function of the application to ensure each unit of the application performs as expected.

- Integration testing will be performed by the QA tester to ensure multiple modules of the application perform together smoothly.

- System testing will be performed by running the application on an android device to ensure the application as a whole performs according to the user's requirements.

- Regression testing will be performed if needed by developers to ensure new code does not have a negative impact on the existing code.

### 1.2 Test Selection

- We will test using both black & white box techniques.

- White-box testing will be used on the unit, going through the code to see if each unit works as intended.

- Black-box testing will be used on the system and regression, including if the program executes or not, choosing valid and invalid inputs to check if the program works correctly, see if the selected function shows the expected output, etc.

 - Most testing will be done manually.

### 1.3 Adequacy Criterion

- To ensure a list of good quality test cases, we will select cases that cover every function of the application, i.e. every structure must execute at least once.

### 1.4 Bug Tracking

- We will be using GitHub for issue tracking. Each issue will be ticketed and assigned to a developer with additional information such as statuses, resolutions, and priorities.
- We will use the LogCat and Runtime Stack provided by Android Studio.

### 1.5 Technology

Most testing will be done manually.


## 2 Test Cases
 
| Test Case| Purpose| Steps| Expected Result | Actual Result | Pass/Fail| Additional Information|
|:---|:---|:---|:---|:---|:---|:---|
|1 | Test if program opens as expected and without crashing | Open the APP| The app should display without crashing| The app opens successfully| Pass| There were many times where this test case failed but in reading the errors given by Android Studio, we were able to fulfill this test case|
|2 | Test if user can add a new list| Push the + button and provide a name for the new list| New list should be created with the provided name | A new list is successfully created as intended| Pass| This was a very simple implementation and so passed at all times|
|3 | Test if user can delete existing list | Tap on the list that the user wants to remove. Push the delete button| Selected list was removed from main menu | The list was successfully removed| Pass| This was also simple to implement and so passed at all times|
|4 | Test if the list can be renamed| Tap on the list that the user wants to rename. Push the rename button. Provide a new name for the list| The list should appear using its new name| The list was sucessfully renamed under the condition that the name was alphanumeric| Fail| The requirements do not specify what the list of the name should be|
|5 | Test if the list can be selected| The user taps on the list. The user then pushes the view button| The list should be selected| The user is presented the items that are on the list that was chosen| Pass| The user is able to successfully view the items on a list and is also able to modify it and delete it upon selection|
|6 | Test if user can add item to the list | Once viewing the contents of the list push the + button. Then, select one of the two ways to add an item to the list. Select the item that is to be added| The selected item will be added into the list| The user is given the choice of two options for adding to a list. The user then can add an item hierarchically or by searching for it with a string| Pass| The user also provides a quantity for the item here| 
|7 | Test if user is able to delete item from a list| Push the delete button next to the item to be deleted| The item should disappear from the list| The item is removed from the list and does not show up on return to it| Pass| The only malfunction here is that when all items of a specific item type are removed from a list, the empty category is still displayed|
|8 | Test if user can change quantity of an item on a list | Press on the quantity of the item. Provide a new quantity for the item.| The quantity of the item changes when enter a new quantity in the pop up menu | A pop up menu appears requesting a number for the item| Pass| The only setback faced here is that it may not be clear to the user how to change the quantity| 
|9 | Test if user can add an item by picking it from a hierarchical list | Press the search by type button when adding an item. Pick an item type. Pick an item of the available ones of that item type|The selected item should appear in the list | The user is able to select the item and corresponding item type and is then able to provide a quantity for it. The item is then added to the list with this quantity| Pass| This was one of the more difficult activities to implement and so did not pass this test case very often until the end.| 
|10| Test if an item can be found by its type and name| Select an item type when adding hierarchically. Check to make sure all items available are shown|The user should be able to find what they are looking for| All items corresponding to an item type are shown| Pass| This was a rather simple implementation and so almost always passed this test case|
|11| Test if user can find item by typing its name| Type in the name of the item that is being searched. See if related items are shown| The item that user is looking for should appear and possible to be add in to the list| As the user types searching for items, the list of items that are available gets refined and displays possible matches for the user| Pass| This was one of the final implementations and fit in very nicely and so always passed the test case.|
|12| Test if an item which is not listed can be added| Search for an item that is not in the list. Push the new item button. Provide a item type. The new item should be in the list| The item should be add into the list and database| The item is added into the database and also the hierarchical list, as well as the full list of all items| Pass| Implementing this activity took a very long time and some reworking of other code and so fulfillment of this test case at times caused other test cases to fail|
|13| Test if the list is saved automatically| Modify a list. Exit the app. Return to the list. Check if all changes are saved|The list should appear as same as when it was closed| All changes are always saved| Pass| All modifications to a list are instantly written to the database so that it is saved|
|14| Test if item can be checked off| Push the checkmark next to an item. Exit the app. Return and check to see if it is still checked off|The box should be checked off and should be persistent| The checks are persistent every time the app is opened| Pass| This activity had to be reworked multiple times to ensure that the checks were persistent. Ultimately the checked status was saved in the database|
|15| Test if user can clear all the check off marks at once| Check off at least one item. Press the Clear Checks button. Check to make sure all checks are removed| All the checked checkbox can be clear out using the clear all button| The checks are all successfully set to unchecked| Pass| This was a simple implementatiion and so always passed the test case|
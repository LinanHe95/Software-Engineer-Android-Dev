package edu.qc.seclass.glm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;


public class DbConnection extends SQLiteOpenHelper {

    /**
     * DATABASE MODEL:
     *
     * -Initially 3 tables are created and set up. The table to hold the items, the types, and the table
     * that will keep track of all the user's tables/lists.
     *
     * -User's lists will be represented as tables in the database. This is so we can keep track of its items
     * and save the state of the list in the DB
     *
     * -Methods have been created to perform all (I think) required functionality from the DB.
     * Feel free to add methods if you think of a new feature and ask questions about the documentation if you have any
     *
     * -To use the DB in a java file, create an new instance "new DbConnection(this)"
     * The parameter is the context of the activity and is required when you are instantiating.
     *
     * -Once you have the DbConnection object you can begin calling the query functions to get the data you need
     *
     * -The data will persist across all instantiations: Meaning you can create new connections in any
     * class and it will always refer to the same data
     */

    //Database information
    public final static String DB_Name = "GroceryListDB";
    public final static int DB_Version = 1;

    //name of the initial lists we set up
    public final static String User_List_Table = "UserLists";
    public final static String Item_Types_Table = "ItemTypes";
    public final static String Items_Table = "Items";

    //constructor
    public DbConnection(Context context) {
        super(context, DB_Name, null, DB_Version);
    }


    //This method will be called the very first time the database is instantiated
    //it will never be called again, only if the device's memory has been wiped
    @Override
    public void onCreate(SQLiteDatabase db) {

        //To initially load the types and items into the tables call the "loadDefaultTables" method in the class you are working in ONCE
        //It only needs to be done once. The data will remain even after closing and restarting the app
        loadDefaultTables(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + User_List_Table);
        db.execSQL("DROP TABLE IF EXISTS " + Item_Types_Table);
        db.execSQL("DROP TABLE IF EXISTS " + Items_Table);
        onCreate(db);
    }


    /**
     * Will add a new item to the item table
     * @param cat
     * @param prod
     * @return
     */
    public boolean addItem(String cat, String prod){
        SQLiteDatabase db = this.getWritableDatabase();

        //content values act like maps, they store key, value pairs (col name, data we want there)
        //they are necessary when using the built-in sql functions
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemName", prod);
        contentValues.put("TypeName", cat);

        long result = db.insert(Items_Table, null, contentValues);

        //will only be -1 if there is an error
        return (result==-1) ? false : true;
    }


    /**
     * Will remove the specified item from the item table
     * the user has no use for this method
     * @param prod
     * @return
     */
    public boolean removeItem(String prod){

        SQLiteDatabase db = this.getWritableDatabase();

        //store the number of rows deleted
        long result = db.delete(Items_Table, "ItemName = ?", new String[]{prod});

        //only 1 row should be deleted at a time
        return (result!=1) ? false : true;
    }


    /**
     * This method will add a new type to the type table
     * @param cat -The name of the type we want to add
     * @return
     */
    public boolean addType(String cat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TypeName", cat);

        long result = db.insert(Item_Types_Table, null, contentValues);

        return (result==-1) ? false : true;
    }


    /**
     * Will remove a type from the type table.
     * The user has no use for this method but I thought it would be convenient to have
     * @param cat The type to delete
     * @return
     */
    public boolean removeType(String cat){

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(Item_Types_Table, "TypeName = ?", new String[]{cat});
        return (result!=1) ? false : true;
    }

    /**
     * This will add a new User grocery list
     * It will add the list to the index containing all of the user's lists
     * as well as create a table that will represent this list and its contents in the database
     * @param list
     * @return
     */
    public boolean addList(String list){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", list);

        //Changes the string if the list starts with a number
        if(Character.isDigit(list.charAt(0))){
            list = "N_ " + list;
        }
        //Makes all the spaces in the list name underscores
        list = list.replaceAll("\\s", "_");

        long result = db.insert(User_List_Table, null, contentValues);

        if(result!=-1) {
            //each user list will be stored in the DB as a table
            //check if this list already exists in the main activity
            db.execSQL("CREATE TABLE " + list.replaceAll("\\s", "_") + "(ID INTEGER PRIMARY KEY, ItemName TEXT, TypeName TEXT, QTY INTEGER, CHK INTEGER DEFAULT 0)");

            return true;
        } else{
            return false;
        }

    }

    /**
     * Will rename the User's specified list to the new name passed in
     * @param list
     * @param newName
     */
    public void renameList(String list, String newName){

        SQLiteDatabase db = this.getWritableDatabase();
        String rename = String.format("UPDATE %s SET ListName = '%s' WHERE ListName = '%s'", User_List_Table, newName, list);
        db.execSQL(rename);
        rename = "ALTER TABLE " + list.replaceAll("\\s", "_") + " RENAME TO " + newName.replaceAll("\\s", "_");
        db.execSQL(rename);
    }

    /**
     * This method will delete the User's specified grocery list. Method will also drop that table
     * @param list
     * @return Whether operation was successful
     */
    public boolean removeList(String list){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(User_List_Table, "ListName = ?", new String[]{list});
        db.execSQL("DROP TABLE " + list.replaceAll("\\s", "_"));
        if(result!=1)
            return true;
        else
            return false;
    }


    /**
     * Will add an item to the specified list
     * @param userListName
     * @param prod
     * @param quant
     * @return Whether operation was successful
     */
    public boolean addToUserList(String userListName, String prod, String type, int quant){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = {"ItemName", "TypeName"};
        long result = 1;

        //querying the uerList to see if an item already exists
        String sqlStr = "SELECT ItemName FROM " + userListName.replaceAll("\\s", "_") + " WHERE ItemName = \"" + prod + "\" AND TypeName = \"" + type + "\"";
        Cursor dupChk = db.rawQuery(sqlStr, null);

        //we will insert the new record if there was not an existing product
        if(dupChk.getCount()==0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ItemName", prod);
            contentValues.put("TypeName", type);
            contentValues.put("QTY", quant);

            result = db.insert(userListName.replaceAll("\\s", "_"), null, contentValues);
        }
        //will only be -1 if there is an error
        return (result==-1) ? false : true;
    }


    /**
     * Will remove a specified item from a specified list
     * @param userListName
     * @param prod
     * @return Whether the operation was successful
     */
    public boolean removeFromUserList(String userListName, String type, String prod){

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(userListName.replaceAll("\\s", "_"), "ItemName = ? AND TypeName = ?", new String[]{prod, type});
        return (result!=1) ? false:true;
    }


    /**
     * This will update a specified item in the specified list.
     * THIS METHOD HAS NOT BEEN THOROUGHLY TESTED
     * @param userListName
     * @param prod
     * @param quant
     * @return
     */
    public boolean updateUserList(String userListName, String prod, int quant){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("QTY", quant);
        long result = db.update(userListName.replaceAll("\\s", "_"), contentValues, "ItemName = ?", new String[]{prod});
        return (result!=1) ? false : true;
        //return false;
    }


    /**
     * This will return all cols from the specified table. Avoid using this method
     * @param tableName
     * @return Cursor to the result set of the query
     */
    public Cursor getTable(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();

        //still not returning distinct values
        return db.query(true, tableName, null, null, null, null, null, null, null);
        //return db.query(tableName, null, null, null, null, null, null);
    }


    /**
     * Will return an array list of all known types
     * @return List of all known types
     */
    public ArrayList<String> getTypes(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor temp = db.rawQuery("SELECT TypeName FROM ItemTypes ORDER BY TypeName ASC", null);
        ArrayList<String> ret = new ArrayList<String>();
        while(temp.moveToNext()) ret.add(temp.getString(0));
        return ret;
    }

    /**
     * Will return an array list of items of the specified type
     * @param type name
     * @return List of those items
     */
    public ArrayList<String> getItemsOfType(String type){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStr = "SELECT ItemName FROM Items WHERE TypeName = \"" + type + "\" ORDER BY ItemName ASC";
        Cursor temp = db.rawQuery(sqlStr, null);
        ArrayList<String> ret = new ArrayList<String>();ret.add("");
        while(temp.moveToNext()) ret.add(temp.getString(0));
        return ret;
    }


    /**
     * Will return all the types on a given list
     * @param listName
     * @return
     */
    public ArrayList<String> getTypesFromUsrList(String listName){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> ret = new ArrayList<String>();
        String sqlStr = "SELECT TypeName FROM " + listName.replaceAll("\\s", "_") + " GROUP BY TypeName";
        Cursor temp = db.rawQuery(sqlStr, null);
        while(temp.moveToNext()) ret.add(temp.getString(0));
        return ret;
    }


    /**
     * Will get all the items of a given type on a given list
     * @param listName
     * @param typeName
     * @return
     */
    public ArrayList<String> getItemsOfTypeFromUsrList(String listName, String typeName){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> ret = new ArrayList<String>();
        String sqlStr = "SELECT ItemName FROM " + listName.replaceAll("\\s", "_") + " WHERE TypeName = \"" + typeName + "\"";
        Cursor temp = db.rawQuery(sqlStr, null);
        while(temp.moveToNext()) ret.add(temp.getString(0));
        return ret;
    }

    /**
     * Will return whether an item in a given list is checked
     * @param listName
     * @param item
     * @return
     */
    public int getCheck(String listName, String item){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStr = "SELECT CHK FROM " + listName.replaceAll("\\s", "_") + " WHERE ItemName = \"" + item + "\"";
        Cursor temp = db.rawQuery(sqlStr, null);
        temp.moveToNext();
        return temp.getInt(0);
    }


    /**
     * This will set the checked value for an item on a given list
     * @param list
     * @param item
     * @param chk
     */
    public void setCheck(String list, String item, int chk){
        SQLiteDatabase db = this.getReadableDatabase();
        String alt = String.format("UPDATE %s SET CHK = '%s' WHERE ItemName = '%s'", list.replaceAll("\\s", "_"), chk, item);
        db.execSQL(alt);
    }


    public void clearChecks(String list){
        SQLiteDatabase db = this.getReadableDatabase();
        String alt = String.format("UPDATE %s SET CHK = 0", list.replaceAll("\\s", "_"));
        db.execSQL(alt);
    }


    /**
     * Will query the quantity of a given item on a given list
     * @param listName
     * @param item
     * @return
     */
    public String getQuant(String listName, String item){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStr = "SELECT QTY FROM " + listName.replaceAll("\\s", "_") + " WHERE ItemName = \"" + item + "\"";
        Cursor cus = db.rawQuery(sqlStr, null);
        cus.moveToNext();
        int num = cus.getInt(0);
        return String.valueOf(num);
    }


    /**
     * This method is to add some items and item types to the table
     * so we have something to work with when demoing
     */
    public void loadDefaultTables(SQLiteDatabase db){

        String userTable = "CREATE TABLE " + User_List_Table + " (ID INTEGER PRIMARY KEY, ListName TEXT, ListTag TEXT)";
        String typeTable = "CREATE TABLE " + Item_Types_Table + " (ID INTEGER PRIMARY KEY, TypeName TEXT)";
        String itemTable = "CREATE TABLE " + Items_Table + " (ID INTEGER PRIMARY KEY, ItemName TEXT, TypeName TEXT)";

        //executing the sql commands
        db.execSQL(userTable);
        db.execSQL(typeTable);
        db.execSQL(itemTable);

        // Items tables
        // Type: Produce
        ContentValues contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Lettuce");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Tomato");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Onion");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Avocado");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Spinach");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Eggplant");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Cucumbers");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Cabbage");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Bananas");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Strawberries");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Blueberries");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Grapes");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Oranges");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Watermelon");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Lemons");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Limes");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Peaches");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Pineapple");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Cantaloupe");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Cherries");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Pears");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Mangoes");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Raspberries");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Blackberries");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Plums");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Apple");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Nectarines");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Potatoes");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Carrots");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Broccoli");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Bell Peppers");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Celery");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Corn");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Garlic");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Mushrooms");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Sweet Potatoes");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Green beans");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Cauliflower");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Green Onion");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Asparagus");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        contentValues.put("ItemName", "Artichoke");
        db.insert(Items_Table, null, contentValues);

        // Type: Meat
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Meat");
        contentValues.put("ItemName", "Beef");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Meat");
        contentValues.put("ItemName", "Pork");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Meat");
        contentValues.put("ItemName", "Chicken");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Meat");
        contentValues.put("ItemName", "Shrimp");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Meat");
        contentValues.put("ItemName", "Salmon");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Meat");
        contentValues.put("ItemName", "Cod");
        db.insert(Items_Table, null, contentValues);

        // Type: Dairy
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Dairy");
        contentValues.put("ItemName", "Milk");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Dairy");
        contentValues.put("ItemName", "Egg");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Dairy");
        contentValues.put("ItemName", "Butter");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Dairy");
        contentValues.put("ItemName", "Cheese");
        db.insert(Items_Table, null, contentValues);

        // Type: Grain
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Grain");
        contentValues.put("ItemName", "Bread");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Grain");
        contentValues.put("ItemName", "White Rice");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Grain");
        contentValues.put("ItemName", "Brown Rice");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Grain");
        contentValues.put("ItemName", "Oats");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Grain");
        contentValues.put("ItemName", "Pasta");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Grain");
        contentValues.put("ItemName", "Beans");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Grain");
        contentValues.put("ItemName", "Lentil");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Grain");
        contentValues.put("ItemName", "Cereal");
        db.insert(Items_Table, null, contentValues);

        // Type: Drinks or Beverages
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Drinks");
        contentValues.put("ItemName", "Beer");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Drinks");
        contentValues.put("ItemName", "Soda");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Drinks");
        contentValues.put("ItemName", "Seltzer Water");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Drinks");
        contentValues.put("ItemName", "White wine");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Drinks");
        contentValues.put("ItemName", "Red wine");
        db.insert(Items_Table, null, contentValues);

        // Type: Canned/Jarred Goods
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Canned/Jarred Goods");
        contentValues.put("ItemName", "Tomato Sauce");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Canned/Jarred Goods");
        contentValues.put("ItemName", "Olives");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Canned/Jarred Goods");
        contentValues.put("ItemName", "Pickles");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Canned/Jarred Goods");
        contentValues.put("ItemName", "Beans");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Canned/Jarred Goods");
        contentValues.put("ItemName", "Tuna");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Canned/Jarred Goods");
        contentValues.put("ItemName", "Corn");
        db.insert(Items_Table, null, contentValues);

        // Type: Snacks
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Snacks");
        contentValues.put("ItemName", "Cookies");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Snacks");
        contentValues.put("ItemName", "Candy");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Snacks");
        contentValues.put("ItemName", "Gum");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Snacks");
        contentValues.put("ItemName", "Chips");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Snacks");
        contentValues.put("ItemName", "Roasted peanuts");
        db.insert(Items_Table, null, contentValues);

        // Type: Condiments
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Condiments");
        contentValues.put("ItemName", "Ketchup");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Condiments");
        contentValues.put("ItemName", "Mustard");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Condiments");
        contentValues.put("ItemName", "Mayonnaise");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Condiments");
        contentValues.put("ItemName", "Ranch");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Condiments");
        contentValues.put("ItemName", "Blue Cheese");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Condiments");
        contentValues.put("ItemName", "Horseradish");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Condiments");
        contentValues.put("ItemName", "Hot Sauce");
        db.insert(Items_Table, null, contentValues);

        // Type: Spices/Baking Needs
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Salts");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Pepper");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Baking Powder");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Crushed Parsley");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Crushed Oregano");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Turmeric");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Crushed Cilantro");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Coriander");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Cloves");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Garlic Powder");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Onion Powder");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Cayenne Powder");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Chili Powder");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Anise");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Sugar");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Flour");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        contentValues.put("ItemName", "Vanilla Extract");
        db.insert(Items_Table, null, contentValues);

        // Type: Frozen Food
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Frozen Food");
        contentValues.put("ItemName", "Peas");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Frozen Food");
        contentValues.put("ItemName", "Carrots");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Frozen Food");
        contentValues.put("ItemName", "Broccoli");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Frozen Food");
        contentValues.put("ItemName", "Berries");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Frozen Food");
        contentValues.put("ItemName", "Mango");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Frozen Food");
        contentValues.put("ItemName", "Pizza");
        db.insert(Items_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Frozen Food");
        contentValues.put("ItemName", "Ice Cream");
        db.insert(Items_Table, null, contentValues);

        // Item Type Tables
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Produce");
        db.insert(Item_Types_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Meat");
        db.insert(Item_Types_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Dairy");
        db.insert(Item_Types_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Grain");
        db.insert(Item_Types_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Drinks");
        db.insert(Item_Types_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Canned/Jarred Goods");
        db.insert(Item_Types_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Snacks");
        db.insert(Item_Types_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Condiments");
        db.insert(Item_Types_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Spices/Baking Needs");
        db.insert(Item_Types_Table, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("TypeName", "Frozen Food");
        db.insert(Item_Types_Table, null, contentValues);

    }
    //getting the items from the DB and list view them to the search by string.
    public ArrayList<String> getItems(){
        ArrayList<String> holditems= new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor temp= db.rawQuery("SELECT DISTINCT ItemName FROM Items ORDER BY ItemName ASC", null);
        while(temp.moveToNext()){
            holditems.add(temp.getString(0));
        }
        return holditems;
    }

    public boolean itemExists(String type, String item){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor temp= db.rawQuery("SELECT ItemName FROM Items WHERE TypeName = '" + type + "' AND lower(ItemName) = '" + item + "'", null);
        return temp.moveToNext();
    }

    public void addToDB(String type, String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TypeName", type);
        contentValues.put("ItemName", item);
        db.insert(Items_Table, null, contentValues);
    }

    public ArrayList<String> getTypesFor(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> types = new ArrayList<>();
        Cursor temp = db.rawQuery("SELECT TypeName FROM Items WHERE ItemName = '" + item + "'", null);
        while(temp.moveToNext()){
            types.add(temp.getString(0));
        }
        return types;
    }


    /**
     * -add method to return all types: Can maybe display this in a spinner for the user to select
     * --then onSelect we will display the items of that type for the user
     * ---make sure user is able to specify quant after selecting the item
     *
     * -add a search method
     * --this should be done when I figure out how to handle lists in focus
     *
     * -still getting duplicates in DB
     *
     * -do unit testing before moving on to the UI
     *
     * -checking off items will be a front-end component
     *
     */


}


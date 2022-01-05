package edu.qc.seclass.glm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListContent extends AppCompatActivity {

    ArrayList<String> typeList;
    ArrayList<String> itemList;
    Map<String, ArrayList<String>> groupedItems;
    ExpandableListView expandableListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_content_options, menu);
        return true;
    }
    


    MyExpandableListAdapter expandableListAdapter;
    DbConnection db;
    String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get a db connection and get the name of the list that was selected
        db = new DbConnection(this);
        listName = getIntent().getStringExtra("LIST_NAME");

        setTitle(WordUtils.capitalize(listName));

        //call this function to loop through all items in the list and group them by type
        createGrouping();

        expandableListView = findViewById(R.id.groceryList_ListView);
        expandableListAdapter = new MyExpandableListAdapter(this, typeList, groupedItems, listName);
        expandableListView.setAdapter(expandableListAdapter);
        /*expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandPos=-1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandPos != -1 && i != lastExpandPos){
                    expandableListView.collapseGroup(lastExpandPos);
                }
                lastExpandPos=i;
            }
        });

        I got rid of this so that we can have more than one group expanded

         */

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selectedItem = expandableListAdapter.getChild(i, i1).toString(); //name of the item that was selected from the list
                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListContent.this, SearchOption.class);
                intent.putExtra("LIST_NAME", listName); //passing the name of the list so we know which list to reference
                startActivity(intent);
            }
        });

        int groups = expandableListView.getAdapter().getCount();
        for(int i = 0; i < groups; i++){
            expandableListView.expandGroup(i);
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.backToHome1:
                Intent intent = new Intent(ListContent.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            case R.id.clearChecks:
                //clear them checks
                db.clearChecks(listName);
                expandableListAdapter.event();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Will group all items by item type into a hashmap
     */
    public void createGrouping(){
        typeList = db.getTypesFromUsrList(listName);
        groupedItems = new HashMap<String, ArrayList<String>>();
        for(String type : typeList){
            itemList = db.getItemsOfTypeFromUsrList(listName, type);
            groupedItems.put(type, itemList);
        }
    }

}
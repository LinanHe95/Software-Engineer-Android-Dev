package edu.qc.seclass.glm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.WordUtils;

public class MainActivity extends AppCompatActivity {

    DbConnection db;
    Dialog listOptions;
    public FloatingActionButton newListBtn;
    ArrayList<String> buttonsSaved = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Grocery Manager");

        //Make the database connection
        db = new DbConnection(this);

        //Get the table of the lists the user has from the database
        Cursor temp = db.getTable("UserLists");

        //Load the lists that the user has created
        while(temp.moveToNext()){
            buttonsSaved.add(temp.getString(1));
        }
        makeSavedButtons();

        //The following code adds functionality to the button that is used to make a new list
        newListBtn = (FloatingActionButton) findViewById(R.id.newListBtn);
        newListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This creates the popup that asks the user for a name for the list
                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.new_list_input, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setView(v);

                //This grabs what the user entered for the name of the new list
                final EditText newListName = (EditText) v.findViewById(R.id.newItemName);

                //This defines what happens when the user presses the button that says done after giving a name for the list
                alert.setCancelable(true).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //This checks if the name given is an empty string, if it is empty we do nothing, otherwise we check for other conditions
                        //This first condition defines what happens if the list name given is not empty
                        View v;
                        AlertDialog.Builder alert;
                        if(!(newListName.getText().toString().trim().isEmpty())) {
                            //This defines what happens if a list with the provided name already exists
                            if(buttonsSaved.contains(newListName.getText().toString().trim().toLowerCase())){
                                //The following code creates an error message that tells the user that a list of that name already exists
                                v = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_already_exists, null);
                                alert = new AlertDialog.Builder(MainActivity.this);
                                alert.setView(v);
                                alert.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                Dialog d = alert.create();
                                d.show();
                            }
                            if(!(newListName.getText().toString().trim().toLowerCase().replaceAll("\\s", "").matches("^[a-zA-Z0-9]*$"))){
                                v = LayoutInflater.from(MainActivity.this).inflate(R.layout.invalid_list_name, null);
                                alert = new AlertDialog.Builder(MainActivity.this);
                                alert.setView(v);
                                alert.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                Dialog d = alert.create();
                                d.show();
                            }
                            //If the list name is unique, we go to the function that adds the list to the database and makes the button to be displayed
                            else
                                newList(newListName.getText().toString().trim().toLowerCase());
                        }
                    }
                });
                Dialog dialog = alert.create();
                dialog.show();
            }
        });
        //code for the edit button (I did not make this yet but I can if we still want it)
    }
    //setting up the search by string


    //This function makes the buttons and adds the lists to the database
    public void newList(String listName){
        db.addList(listName);
        Button filler = new Button(this);
        LinearLayout groceryLists = (LinearLayout) findViewById(R.id.listHolder);
        buttonsSaved.add(listName);
        filler.setTag(listName);
        filler.setText(listName);
        filler.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_border));
        int i = buttonsSaved.size() - 1;
        filler.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterList(v, listName, i);
            }
        });
        groceryLists.addView(filler);
    }

    //This is the function that is called when the user taps on a list, it brings them to the screen that allows them to rename, delete or view the list
    public void enterList(View v, String list, int index){
        //This sets the view of the screen
        listOptions = new Dialog(this);
        listOptions.setContentView(R.layout.list_options);
        listOptions.getWindow().setBackgroundDrawable(getDrawable(R.drawable.list_options_background));
        listOptions.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listOptions.setCancelable(true);
        TextView opTitle = listOptions.findViewById(R.id.listNameInOption);
        opTitle.setText(WordUtils.capitalize(list));
        listOptions.show();

        //Here we are grabbing the buttons to modify them
        Button delBtn, viewBtn, reNameBtn, cancelBtn;
        delBtn = listOptions.findViewById(R.id.deleteListBtn);
        viewBtn = listOptions.findViewById(R.id.viewListBtn);
        reNameBtn = listOptions.findViewById(R.id.renameListBtn);
        cancelBtn = listOptions.findViewById(R.id.cancelOptionsBtn);

        //The following onClickListeners are set up to provide functionality to the buttons
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Would you like to remove this list?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        ArrayList<String> copy = buttonsSaved;
                        copy.remove(index);
                        buttonsSaved = new ArrayList<String>(copy);
                        db.removeList((String) v.getTag());
                        ((ViewGroup) v.getParent()).removeAllViews();
                        listOptions.dismiss();
                        makeSavedButtons();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                android.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOptions.dismiss();
                Intent intent = new Intent(MainActivity.this, ListContent.class);
                intent.putExtra("LIST_NAME", list); //passing the name of the list so we know which list to load
                startActivity(intent);
            }
        });
        reNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Dismisses the current alert and then makes the new alert that asks the user for the new name
                listOptions.dismiss();
                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.new_list_name, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setView(v);
                final EditText newListName = (EditText) v.findViewById(R.id.newListName);
                //This is similar to when we first make a list, where there is error checking for a list that is already made
                alert.setCancelable(true).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newName = newListName.getText().toString().trim().toLowerCase();
                        if(!(newName.isEmpty())) {
                            //This defines what happens if a list with the provided name already exists
                            if(buttonsSaved.contains(newName)){
                                //The following code creates an error message that tells the user that a list of that name already exists
                                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_already_exists, null);
                                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                alert.setView(v);
                                alert.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                Dialog d = alert.create();
                                d.show();
                            }
                            //If the list name is unique the following code executes
                            else{
                                //The list is renamed in the database
                                db.renameList(list, newName);
                                //The array of the saved buttons changes to reflect the change in name of the list
                                buttonsSaved.set(index, newListName.getText().toString().trim().toLowerCase());
                                //We change the button so that it has a new tag with the new list name and so that it also shows the new list name
                                LinearLayout buttonHolder = findViewById(R.id.listHolder);
                                Button modify = (Button) buttonHolder.findViewWithTag(list);
                                modify.setText(newName);
                                modify.setTag(newName);
                                modify.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        enterList(v, newName, index);
                                    }
                                });
                            }
                        }
                    }
                });
                Dialog dialog = alert.create();
                dialog.show();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listOptions.dismiss();
            }
        });
    }

    //here we will decide what to do with the list we selected

    public void makeSavedButtons(){
        Button initiate;
        LinearLayout fillIn = (LinearLayout) findViewById(R.id.listHolder);
        for(int i = 0; i < buttonsSaved.size(); i++){
            initiate = new Button(this);
            initiate.setText(buttonsSaved.get(i));
            initiate.setTag(buttonsSaved.get(i));
            initiate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_border));
            int finalI = i;
            initiate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    enterList(v, buttonsSaved.get(finalI), finalI);
                }
            });
            fillIn.addView(initiate);
        }
    }
}
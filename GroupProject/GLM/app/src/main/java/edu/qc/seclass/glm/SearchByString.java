package edu.qc.seclass.glm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;


import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchByString extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    DbConnection db;
    String listName;
    String searched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_string);

        db = new DbConnection(this);
        ArrayList<String> holdItems= db.getItems();
        listName = getIntent().getStringExtra("LIST_NAME");

        //to get list of things to view
        ListView listView=findViewById(R.id.my_list);
        arrayAdapter =new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, holdItems);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if(!item.equals("")){
                    //will ask the user for the quant they would like of this item
                    View v = LayoutInflater.from(SearchByString.this).inflate(R.layout.choose_type_and_quant, null);
                    AlertDialog.Builder alert = new AlertDialog.Builder(SearchByString.this);
                    alert.setView(v);
                    final EditText quantAmt = (EditText) v.findViewById(R.id.quantAmount);
                    Spinner availableTypes = (Spinner) v.findViewById(R.id.existingTypes);
                    ArrayList<String> typesAvailable = db.getTypesFor(item);
                    ArrayAdapter<String> typeAdapt = new ArrayAdapter<String>(SearchByString.this, android.R.layout.simple_spinner_dropdown_item, typesAvailable);
                    availableTypes.setAdapter(typeAdapt);

                    final String[] type = new String[1];


                    /**what to do when an type is selected**/
                    availableTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            type[0] = adapterView.getItemAtPosition(i).toString();
                            System.out.println(type[0]);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            //nothing here
                        }
                    });

                    alert.setCancelable(true).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //will only update if value is entered
                            if(!quantAmt.getText().toString().trim().equals("")){
                                int q = Integer.parseInt(quantAmt.getText().toString());
                                if(q>0) {
                                    //we will cap the user at 99 items
                                    if(q>99){
                                        db.addToUserList(listName, item, type[0], 99);
                                    } else{
                                        db.addToUserList(listName, item, type[0], q);
                                    }
                                }
                                Intent intent = new Intent(SearchByString.this, ListContent.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("LIST_NAME", listName);
                                startActivity(intent);
                            }

                        }
                    });

                    Dialog dialog = alert.create();
                    dialog.show();
                }
            }
        });

        Button b = new Button(this);
        b.setText("New Item");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(SearchByString.this).inflate(R.layout.add_new_item, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(SearchByString.this);
                alert.setView(v);
                final String[] typeToAddTo = new String[1];

                Spinner types = (Spinner) v.findViewById(R.id.allItemTypes);

                ArrayList<String> typeArr;
                typeArr = db.getTypes();
                ArrayAdapter<String> typeAdapt = new ArrayAdapter<String>(SearchByString.this, android.R.layout.simple_spinner_dropdown_item, typeArr);
                types.setAdapter(typeAdapt);

                types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        typeToAddTo[0] = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //nothing here
                    }
                });

                EditText searchedItem = v.findViewById(R.id.newItemName);
                searchedItem.setText(searched);

                //This defines what happens when the user presses the button that says done after giving a name for the list
                alert.setCancelable(true).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //This checks if the name given is an empty string, if it is empty we do nothing, otherwise we check for other conditions
                        //This first condition defines what happens if the list name given is not empty
                        View v;
                        AlertDialog.Builder alert;
                        String item = searchedItem.getText().toString().trim().toLowerCase();

                        if(db.itemExists(typeToAddTo[0], item)) {
                            v = LayoutInflater.from(SearchByString.this).inflate(R.layout.item_already_exists, null);
                            alert = new AlertDialog.Builder(SearchByString.this);
                            alert.setView(v);
                            alert.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            Dialog d = alert.create();
                            d.show();
                        }
                        else {
                            db.addToDB(typeToAddTo[0], WordUtils.capitalize(item));
                            View view2 = LayoutInflater.from(SearchByString.this).inflate(R.layout.quant_select, null);
                            alert = new AlertDialog.Builder(SearchByString.this);
                            alert.setView(view2);
                            final EditText quantAmt = (EditText) view2.findViewById(R.id.quantAmount);

                            alert.setCancelable(true).setPositiveButton("Done", new DialogInterface.OnClickListener() {


                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    //will only update if value is entered
                                    if(!quantAmt.getText().toString().trim().equals("")){
                                        int q = Integer.parseInt(quantAmt.getText().toString());
                                        if(q>0) {
                                            //we will cap the user at 99 items
                                            if(q>99){
                                                db.addToUserList(listName, WordUtils.capitalize(item), typeToAddTo[0], 99);
                                            } else{
                                                db.addToUserList(listName, WordUtils.capitalize(item), typeToAddTo[0], q);
                                            }
                                        }
                                        Intent intent = new Intent(SearchByString.this, ListContent.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("LIST_NAME", listName);
                                        startActivity(intent);
                                    }

                                }
                            });
                            Dialog dialog = alert.create();
                            dialog.show();
                        }
                    }
                });
                Dialog dialog = alert.create();
                dialog.show();
            }
        });
        listView.addFooterView(b);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_by_strings, menu);
        MenuItem menuItem=menu.findItem(R.id.search_icon);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                searched = newText;
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
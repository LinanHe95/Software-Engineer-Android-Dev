package edu.qc.seclass.glm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;


public class ItemTypeSearch extends AppCompatActivity {

    String listName;
    DbConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_type_search);
        listName = getIntent().getStringExtra("LIST_NAME");
        Button backHome;
        Spinner typeSpinner, itemSpinner;
        backHome = findViewById(R.id.testBackToHomeButton);
        typeSpinner = findViewById(R.id.typeSpinner);
        itemSpinner = findViewById(R.id.itemSpinner);
        db = new DbConnection(this);

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(ItemTypeSearch.this, ListContent.class);
                //intent.putExtra("LIST_NAME", listName);
                //startActivity(intent);
                finish();
            }
        });

        //type adapter code here
        ArrayList<String> typeArr;
        typeArr = db.getTypes();
        ArrayAdapter<String> typeAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeArr);
        typeSpinner.setAdapter(typeAdapt);
        final String[] type = new String[1];


        /**what to do when an type is selected**/
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String res = adapterView.getItemAtPosition(i).toString();
                type[0] = res;
                ArrayList<String> itemArr = db.getItemsOfType(res);
                ArrayAdapter<String> itemAdapt = new ArrayAdapter<String>(ItemTypeSearch.this, android.R.layout.simple_spinner_dropdown_item, itemArr);
                itemSpinner.setAdapter(itemAdapt);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nothing here
            }
        });

        /**what to do when an item is selected**/
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getItemAtPosition(i).toString();
                if(!item.equals("")){
                    //will ask the user for the quant they would like of this item
                    View v = LayoutInflater.from(ItemTypeSearch.this).inflate(R.layout.quant_select, null);
                    AlertDialog.Builder alert = new AlertDialog.Builder(ItemTypeSearch.this);
                    alert.setView(v);
                    final EditText quantAmt = (EditText) v.findViewById(R.id.quantAmount);

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
                                Intent intent = new Intent(ItemTypeSearch.this, ListContent.class);
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

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //nothing here
            }
        });

        //end of onCreate
    }
}
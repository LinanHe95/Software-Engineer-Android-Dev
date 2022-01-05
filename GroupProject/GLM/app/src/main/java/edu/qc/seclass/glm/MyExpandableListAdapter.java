package edu.qc.seclass.glm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String, ArrayList<String>> groupedItems;
    private ArrayList<String> typeList;
    private DbConnection db;
    private String listName;
    //private String currentType;

    public MyExpandableListAdapter(Context context1, ArrayList<String> types, Map<String, ArrayList<String>>grouped, String name){
        context=context1;
        typeList=types;
        groupedItems=grouped;
        db = new DbConnection(context1);
        listName=name;
    }

    @Override
    public int getGroupCount() {
        return groupedItems.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupedItems.get(typeList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return typeList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return groupedItems.get(typeList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        String type1Name = getGroup(i).toString();
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_item, null);
        }
        TextView typeTV = view.findViewById(R.id.groupType);
        typeTV.setTypeface(null, Typeface.BOLD);
        typeTV.setText(type1Name);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        String currentType = getGroup(i).toString();
        String item1Names = getChild(i, i1).toString();
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_item, null);
        }
        TextView itemInList = view.findViewById(R.id.itemNameInList);
        TextView quantInList = view.findViewById(R.id.itemQuantInList);
        Button delFromList = view.findViewById(R.id.deleteItemFromList);
        CheckBox itemChk = (CheckBox) view.findViewById(R.id.checkedItem);


        //query for checked
        if(db.getCheck(listName, item1Names)==0){
            itemChk.setChecked(false);
        } else{
            itemChk.setChecked(true);
        }

        itemInList.setText(item1Names); //setting the name of the item
        quantInList.setText(db.getQuant(listName, item1Names)); //query to get quant

        itemChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemChk.isChecked()){
                    //adjusting in db
                    db.setCheck(listName, item1Names, 1);
                } else {
                    db.setCheck(listName, item1Names, 0);
                }
            }
        });

        quantInList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(context).inflate(R.layout.count_adjust, null);
                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(context);
                alert.setView(v);
                final EditText countAmt = (EditText) v.findViewById(R.id.countAdjustAmount);

                alert.setCancelable(true).setPositiveButton("Done", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //will only update if value is entered
                        if(!countAmt.getText().toString().trim().equals("")){
                            int q = Integer.parseInt(countAmt.getText().toString());
                            if(q>0) {
                                //set the text
                                if(q>99){
                                    quantInList.setText("99");
                                    db.updateUserList(listName, item1Names, 99);
                                } else{
                                    quantInList.setText(String.valueOf(q));
                                    db.updateUserList(listName, item1Names, q);
                                }

                            }

                        }

                    }
                });

                Dialog dialog = alert.create();
                dialog.show();
            }
        });

        delFromList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Would you like to remove this item?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        System.out.println(listName + "   " + currentType + "  " + item1Names);
                        List<String> child = groupedItems.get(typeList.get(i));
                        db.removeFromUserList(listName, currentType, item1Names);
                        child.remove(i1);
                        notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
    }

    public void event(){
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

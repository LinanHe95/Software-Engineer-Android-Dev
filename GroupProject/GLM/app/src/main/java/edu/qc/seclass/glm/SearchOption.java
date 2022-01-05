package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_option);
        String listName = getIntent().getStringExtra("LIST_NAME");

        Button tSearch, iSearch, bck;
        tSearch = findViewById(R.id.typeSearchBtn);
        iSearch = findViewById(R.id.itemSearchBtn);
        bck = findViewById(R.id.searchOptionBackBtn);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(SearchOption.this, ListContent.class);
                //intent.putExtra("LIST_NAME", listName);
                //startActivity(intent);
                finish();
            }
        });

        tSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchOption.this, ItemTypeSearch.class);
                intent.putExtra("LIST_NAME", listName);
                startActivity(intent);
            }
        });

        iSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchOption.this, SearchByString.class);
                intent.putExtra("LIST_NAME", listName);
                startActivity(intent);
            }
        });
    }
}
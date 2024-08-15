package com.example.weshops;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransHistory extends AppCompatActivity {
    ArrayList<HistProducts> prod;
    private TextView receive,count;
    HistAdapter histAdapter;

    private HistAdapter hAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_history);

        receive = findViewById(R.id.textView);
        count = findViewById(R.id.count);
        Bundle bObject =  getIntent().getExtras();

        prod = (ArrayList<HistProducts>) bObject.getSerializable("hist");
        count.setText(String.valueOf(prod.size()));


        RecyclerView rec = findViewById(R.id.recyclerView);
        rec.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        histAdapter = new HistAdapter(prod);
        rec.setLayoutManager(layoutManager);
        rec.setAdapter(histAdapter);
        histAdapter.notifyDataSetChanged();

        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new  Intent(TransHistory.this,Home.class);

                startActivity(i);
            }
        });





    }

}

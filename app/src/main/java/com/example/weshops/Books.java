package com.example.weshops;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class Books extends AppCompatActivity {

    private RecyclerView recyclerView;
    private int[] images={R.drawable.meditation,R.drawable.maths,R.drawable.literature,R.drawable.horrorstorybook,R.drawable.firstyearengg,R.drawable.encyclopedia,R.drawable.dsa,R.drawable.music};
    private String[] details={"Meditation","Maths","Literature","Horror Story Book","First Year Engineering Bookset","Encyclopedia","DSA","Music"};
    private int[] prices={399,699,899,459,2299,1299,649,799};
    int[] itemQuantities = {10, 5, 8, 15, 3, 7, 12, 20};
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        recyclerView=findViewById(R.id.rvBooks);
//        TextView addCart = (TextView) findViewById(R.id.addCart);
        final String sna=getIntent().getStringExtra("NAME");
        final String sph=getIntent().getStringExtra("PHONE");
        final String spa=getIntent().getStringExtra("PASSWORD");

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Add book data to Firebase
        for (int i = 0; i < details.length; i++) {
            addBookToFirebase(images[i], details[i], prices[i],itemQuantities[i]);
        }


        layoutManager=new GridLayoutManager(this,1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecyclerAdapter(images,details,prices,this,sna,sph,spa,"Books");
        recyclerView.setAdapter(adapter);

    }

    private void addBookToFirebase(int imageResId, String itemDetails, int itemPrice, int itemQuantities) {
        DatabaseReference bookReference = databaseReference.child(itemDetails);
        bookReference.child("imageResId").setValue(imageResId);
        bookReference.child("itemPrice").setValue(itemPrice);
        bookReference.child("itemQuantity").setValue(itemQuantities);
    }

    public void onBackPressed(){
        final String sna=getIntent().getStringExtra("NAME");
        final String sph=getIntent().getStringExtra("PHONE");
        final String spa=getIntent().getStringExtra("PASSWORD");
        Intent intent=new Intent(Books.this,MainActivity.class);
        intent.putExtra("NAME",sna);
        intent.putExtra("PHONE",sph);
        intent.putExtra("PASSWORD",spa);
        intent.putExtra("CALLINGACTIVITY","Division");
        startActivity(intent);
    }


}

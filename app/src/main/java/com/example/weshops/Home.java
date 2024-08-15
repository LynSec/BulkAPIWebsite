package com.example.weshops;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;



public class Home  extends AppCompatActivity {
    Button btnLogout;

    private ArrayList<ShopProducts> Products;
    private ArrayList<HistProducts> histProduct;
    String HistName,HistPrice,HistDesc,HistDate;

    int HistImage;
    ShopAdapter productAdapter;
    HistAdapter histAdapter;
    String[] names;
    String[] productdescribe;
    private int[] images;
    int[] price;
    int[] dates;
    private String url="http://127.0.0.1:5005";
    private String POST="POST";
    private String GET="GET";


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);


        images = new int[]{
                R.drawable.corn,
                R.drawable.carrots,
                R.drawable.grapes
        };
        price = new int[]{
                300,
                150,
                200
        };
        dates = new int[]{
                20,
                12,
                29
        };
        productdescribe = new String[]{
                "corn are the best in the market",
                "carrots have different colors with difference in test",
                "grapes are purple with succulent juice"
        };
        names = new String[]{
                "corns",
                "carrots",
                "grapes"
        };


        RecyclerView prodRecycle = findViewById(R.id.prod_view);
        prodRecycle.setLayoutManager(new LinearLayoutManager(this));

        Button btnsortaz = findViewById(R.id.sort_az);
        Button btnsortza = findViewById(R.id.sort_za);
        Button btnsortprice = findViewById(R.id.sort_price);
        Button btnsorttime = findViewById(R.id.sort_time);
//        Button getserver = findViewById(R.id.getserver);
//        TextView textv = findViewById(R.id.response);


        btnsortaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortItems();
            }
        });
        btnsortza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortZA();
            }
        });
        btnsortprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortPrice();
            }
        });
        btnsorttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortDate();
            }
        });




        btnLogout = findViewById(R.id.logout_btn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Home.this, MainActivity.class);
                // Clear login details in SharedPreferences
                SharedPreferences preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("USER_ID");
                editor.remove("IS_LOGGED_IN");
                editor.remove("IS_ADMIN_LOGGED_IN");
                editor.apply();
                startActivity(back);

                finish();
            }
        });
        Products = new ArrayList<>();
        Products.add(new ShopProducts(R.drawable.grapes, "grape books", "the best book in the market","34", "12"));
        Products.add(new ShopProducts(R.drawable.carrots, "carrots book", "the best flavours possible","34","25"));//, 150, 12));
        Products.add(new ShopProducts(R.drawable.corn, "corns book", "yellow corns book","300","18"));//, 30, 18));
        productAdapter = new ShopAdapter(Products);
        prodRecycle.setAdapter(productAdapter);
        // notify the adapter that the data has changed
        productAdapter.notifyDataSetChanged();

    }



    public void addToCart(int position) {
        Products.remove(position);
        HistPrice = Products.get(position).getPrice();
        HistDate = Products.get(position).getProdDate();
        HistDesc = Products.get(position).getdescription();
        int HistImage = Products.get(position).getProdImage();
        HistName = Products.get(position).getProdName();
        histProduct = new ArrayList<HistProducts>();
        histProduct.add(new HistProducts(HistImage,HistName,HistDesc,HistPrice,HistDate));
        histAdapter = new HistAdapter(histProduct);

        Intent i = new Intent(Home.this, TransHistory.class);
        i.putExtra("hist", histProduct);
        startActivity(i);







        productAdapter.notifyItemRemoved(position);
    }
    // calling on create option menu
    // layout to inflate our menu file.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = (MenuItem) menu.findItem(R.id.actionSearch);
        MenuItem bookBtn = (MenuItem) menu.findItem(R.id.Books);
        bookBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                startActivity(new Intent(Home.this,Books.class));
                return true;
            }
        });



        return true;
    }

    private void sortItems() {

        Collections.sort(Products, ShopProducts.ProductCompareAZ);
        //sort a z
        Toast.makeText(this, "sorted a-z", Toast.LENGTH_SHORT).show();
        productAdapter.notifyDataSetChanged();
    }
    private void sortZA() {

        Collections.sort(Products, ShopProducts.ProductCompareZA);
        //sort a z
        Toast.makeText(this, "sorted z.a", Toast.LENGTH_SHORT).show();
        productAdapter.notifyDataSetChanged();
    }
    private void sortPrice() {
        Collections.sort(Products, ShopProducts.Productpriceasc);
        //sort a z
        Toast.makeText(this, "sorted a-z", Toast.LENGTH_SHORT).show();
        productAdapter.notifyDataSetChanged();
    }
    private void sortDate() {

        Collections.sort(Products,ShopProducts.Productlatest);
        //sort a z
        Toast.makeText(this, "sorted a-z", Toast.LENGTH_SHORT).show();
        productAdapter.notifyDataSetChanged();
    }
    private void findProduct(String text) {
        // creating a new array list to filter our data.
        ArrayList<ShopProducts> filteredlist = new ArrayList<ShopProducts>();

        // running a for loop to compare elements.
        for (ShopProducts item : filteredlist) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getProdName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            productAdapter.filterList(filteredlist);
        }
    }

}
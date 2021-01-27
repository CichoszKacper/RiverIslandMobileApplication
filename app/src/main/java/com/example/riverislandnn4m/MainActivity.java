package com.example.riverislandnn4m;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//Activity with products display
public class MainActivity extends AppCompatActivity {

    List <product> listOfProducts = new ArrayList<>();
    private RequestQueue mQueue;
    Adapter productAdapter;
    RecyclerView recyclerView;
    SearchView searchview;
    TextView noResults;
    LinearLayoutManager layoutManager;

    private int previousTotal = 0;
    private int view_threshold = 10;

    private int firstItemVisible = 0;
    private int lastItemVisible = 10;

    private boolean mLoadingItems = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.listViewProducts);
        searchview = findViewById(R.id.listSearchView);
        searchview.setFocusable(true);
        noResults = findViewById(R.id.noResults);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mQueue = Volley.newRequestQueue(this);

        //Disabling the detection of everything in ThreadPolicy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        jsonParse();
        initSearchWidget();
        startBottomNavigationBar();
        pagination();

    }

    //Method to disable automatic keyboard on start of the activity
    @Override
    public void onResume() {
        super.onResume();
        searchview.setFocusable(false);
    }


    //Method to parse the JSON file
    public void jsonParse () {

        //URL with JSON
        String url = "https://static-ri.ristack-3.nn4maws.net/v1/plp/en_gb/2506/products.json";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray products =  response.getJSONArray("Products");

                        //Iterating over the list of products in JSON file, using first visible item
                        //and last visible item variables from scroll listener
                        for(int i = firstItemVisible; i < lastItemVisible; i++){
                            JSONObject product = products.getJSONObject(i);

                            //Extracting the needed data
                            String name = product.getString("name");
                            String price = product.getString("cost");
                            String prodid = product.getString("prodid");

                            //Extracting the image of product based on its ID from different URL
                            String imageURLString = "http://riverisland.scene7.com/is/image/RiverIsland/" + product.getString("prodid") + "_main";
                            URL imageURL = new URL(imageURLString);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 2; //Changing the size of the image
                            Bitmap bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream(),null, options);

                            //Creating new product with necessary parameters
                            product newProduct = new product(name,price,prodid,bmp,imageURLString);

                            //Adding the product to the Array with products
                            listOfProducts.add(newProduct);
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    //Running method to add the adapter
                    if(recyclerView.getAdapter()==null) {
                        //New adapter if starting
                        productAdapter = new Adapter(this, listOfProducts);
                        recyclerView.setAdapter(productAdapter);
                    }
                    else{
                        productAdapter.notifyDataSetChanged(); //notifies View reflecting data to refresh
                    }


                }, Throwable::printStackTrace);
        mQueue.add(request);
    }

    //Method to initialize the Search bar
    public void initSearchWidget(){

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Closes the keyboard once query is submitted
                searchview.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //New arraylist for filtered products
                List<product> filteredList = new ArrayList<>();

                //Iterating over the array with products and adding every product that matches
                //the query to the new filtered list
                for (product product : listOfProducts){
                    if (product.getProductName().toLowerCase().contains(newText.toLowerCase())){
                        filteredList.add(product);
                    }
                }

                //Hides the product list and display "not found" message when can't find match
                if (filteredList.size()<1){
                    recyclerView.setVisibility(View.GONE);
                    noResults.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noResults.setVisibility(View.GONE);
                }

                //Sets new adapter with filtered products to the recycler view
                productAdapter = new Adapter(MainActivity.this,filteredList);
                recyclerView.setAdapter(productAdapter);

                return true;
            }
        });
    }

    //Bottom navigation bar
    public void startBottomNavigationBar(){

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationViewBottom);
        bottomNavigationView.setSelectedItemId(R.id.logo);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            //Setting up listener for MainMenu button
            if (item.getItemId() == R.id.logo){
                Intent mainIntent = new Intent(this,MainMenuActivity.class);
                startActivity(mainIntent);
                return true;
            }

            //TODO listeners for the rest of sections once created

            return false;
        });
    }

    //Method to help to load only 10 products at once, using scroll listener
    private void pagination(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy>0){
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastItem  = layoutManager.findLastCompletelyVisibleItemPosition(); //finds last visible item
                    int currentTotalCount = layoutManager.getItemCount(); //find total number of displayed items

                    if (mLoadingItems){
                        if (currentTotalCount > previousTotal){
                            mLoadingItems = false;
                            previousTotal = currentTotalCount;
                        }
                    }
                    if (!mLoadingItems && (currentTotalCount <= (lastItem +view_threshold))) {
                        mLoadingItems = true;
                        //Increment number of items on the list
                        firstItemVisible = firstItemVisible+10;
                        lastItemVisible = lastItemVisible+10;
                        //Update adapter
                        jsonParse();
                    }
                }
            }
        });
    }
}





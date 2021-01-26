package com.example.riverislandnn4m;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

//Activity where details of the product are displayed
public class DetailActivity extends AppCompatActivity implements Serializable {

    String selectedProductName;
    String selectedProductImageURL;
    String selectedProductPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSelectedProduct();
        startBottomNavigationBar();

        TextView productName = findViewById(R.id.productNameDetail);
        TextView productPrice = findViewById(R.id.productPriceDetail);
        ImageView imag = findViewById(R.id.productImageDetail);

        //Exporting image of the product in Detail Activity as bitmap has too large size
        //to pass in serializable. Therefore, URL is put as Extra in previous activity instead of
        //bitmap.
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(selectedProductImageURL).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Adding all product's details to the responsible Views
        productName.setText(selectedProductName);
        String price = "£ " + selectedProductPrice;
        productPrice.setText(price);
        imag.setImageBitmap(bitmap);

    }

    //Method to pass product's details from previous activity to new variables
    private void getSelectedProduct() {
        Intent previousIntent = getIntent();
        selectedProductName = (String) previousIntent.getSerializableExtra("productName");
        selectedProductImageURL = (String) previousIntent.getSerializableExtra("productImageURL");
        selectedProductPrice = (String) previousIntent.getSerializableExtra("productPrice");

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
}
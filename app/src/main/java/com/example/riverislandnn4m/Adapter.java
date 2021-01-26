package com.example.riverislandnn4m;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Adapter Activity
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater layoutInflater;
    List<product> listOfProducts;
    Context context;

    public Adapter(Context context, List<product> products){
        this.layoutInflater = LayoutInflater.from(context);
        this.listOfProducts = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.product_cell,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Setting ViewHolder with product's parameters
        holder.productName.setText(listOfProducts.get(position).getProductName());
        holder.productPrice.setText("£" + listOfProducts.get(position).getPrice());
        holder.productImage.setImageBitmap(listOfProducts.get(position).getImage());
    }

    //Size of the adapter
    @Override
    public int getItemCount() {
        return listOfProducts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productName, productPrice;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);

            // Handle onClick
            itemView.setOnClickListener(v -> {

                //Selects product
                int position = getAdapterPosition();
                product selectedProduct = listOfProducts.get(position);

                //Creating new intent with Detail Activity and adding flag to it
                Intent showDetail = new Intent(context,DetailActivity.class);
                showDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //Passing product's details to next activity and starting it
                showDetail.putExtra("productName",selectedProduct.getProductName());
                showDetail.putExtra("productPrice", selectedProduct.getPrice());
                showDetail.putExtra("productImageURL",selectedProduct.getImageURL());
                context.startActivity(showDetail);
            });
        }
    }
}

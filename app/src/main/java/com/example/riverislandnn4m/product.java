package com.example.riverislandnn4m;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

//Class with product object implementing Parcelable
public class product implements Parcelable {

    private String productName;
    private String price;
    private String prodId;
    private Bitmap image;
    private String imageURL;

    public product(String productName, String price, String prodId, Bitmap image, String imageURL) {
        this.productName = productName;
        this.price = price;
        this.prodId = prodId;
        this.image = image;
        this.imageURL = imageURL;

    }

    protected product(Parcel in) {
        productName = in.readString();
        price = in.readString();
        prodId = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        imageURL = in.readString();
    }

    public static final Creator<product> CREATOR = new Creator<product>() {
        @Override
        public product createFromParcel(Parcel in) {
            return new product(in);
        }

        @Override
        public product[] newArray(int size) {
            return new product[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(price);
        parcel.writeString(prodId);
        parcel.writeParcelable(image, i);
        parcel.writeString(imageURL);
    }
}

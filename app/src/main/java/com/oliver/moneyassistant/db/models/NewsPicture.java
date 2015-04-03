package com.oliver.moneyassistant.db.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsPicture implements Parcelable{
	private int id;
	private int contentId;
	private String imagePath;
	private int orderNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public NewsPicture(){}

    @Override
    public String toString() {
        return "NewsPicture{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", imagePath='" + imagePath + '\'' +
                ", orderNumber=" + orderNumber +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(contentId);
        dest.writeString(imagePath);
        dest.writeInt(orderNumber);
    }

    public static Creator CREATOR = new Creator<NewsPicture>(){

        @Override
        public NewsPicture createFromParcel(Parcel source) {
            NewsPicture picture = new NewsPicture();
            picture.id=source.readInt();
            picture.contentId = source.readInt();
            picture.imagePath = source.readString();
            picture.orderNumber = source.readInt();
            return picture;
        }

        @Override
        public NewsPicture[] newArray(int size) {
            return new NewsPicture[size];
        }
    };
}

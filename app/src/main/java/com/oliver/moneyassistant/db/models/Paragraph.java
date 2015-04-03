package com.oliver.moneyassistant.db.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Paragraph implements Parcelable{
	private int id;
	private int orderNumber;
	private int contentId;
	private String content;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

    public Paragraph(){}

	@Override
	public String toString() {
		return "Paragraph [id=" + id + ", orderNumber=" + orderNumber
				+ ", contentId=" + contentId + ", content=" + content + "]";
	}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(orderNumber);
        dest.writeInt(contentId);
        dest.writeString(content);
    }

    public static final Creator CREATOR = new Creator<Paragraph>(){

        @Override
        public Paragraph createFromParcel(Parcel source) {
            Paragraph par = new Paragraph();
            par.id=source.readInt();
            par.orderNumber = source.readInt();
            par.contentId = source.readInt();
            par.content = source.readString();
            return par;
        }

        @Override
        public Paragraph[] newArray(int size) {
            return new Paragraph[size];
        }
    };

}

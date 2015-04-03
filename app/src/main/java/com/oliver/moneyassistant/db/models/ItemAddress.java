package com.oliver.moneyassistant.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

@Table(name="address")
public class ItemAddress implements Parcelable{

    @Id
    @Column(name="_id")
    private int _id;

    @Column(name="address_header")
	private String addressHeader;

    @Column(name="address_details")
	private String addressDetails;

    @Column(name="o_id")
    private String oId;

    public ItemAddress(){}

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getAddressHeader() {
        return addressHeader;
    }

    public void setAddressHeader(String addressHeader) {
        this.addressHeader = addressHeader;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

    @Override
    public String toString() {
        return "Address{" +
                "_id=" + _id +
                ", addressHeader='" + addressHeader + '\'' +
                ", addressDetails='" + addressDetails + '\'' +
                ", oId='" + oId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(oId);
        dest.writeString(addressHeader);
        dest.writeString(addressDetails);
    }

    public static final Creator CREATOR = new Creator<ItemAddress>(){
        @Override
        public ItemAddress createFromParcel(Parcel source) {
            ItemAddress addr = new ItemAddress();
            addr._id = source.readInt();
            addr.oId = source.readString();
            addr.addressHeader = source.readString();
            addr.addressDetails = source.readString();
            return addr;
        }

        @Override
        public ItemAddress[] newArray(int size) {
            return new ItemAddress[size];
        }
    };
}

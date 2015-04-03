package com.oliver.moneyassistant.db.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Relations;
import com.ab.db.orm.annotation.Table;

@Table(name = "outcome_item")
public class OutcomeItem implements Parcelable{

	@Id
	@Column(name = "_id")
	private int _id;
	
	@Column(name = "o_id")
	private String oId;

	@Column(name="outcome_money",type="float")
	private float outcomeMoney;
    
    @Column(name="type_id",type="integer")
	private int typeId;
    
    @Column(name="outcome_time",type="integer")
	private long outcomeTime;
    
    @Relations(name="address",type = "one2one",foreignKey = "o_id",action="query_insert")
    private ItemAddress address;
    
    @Column(name="outcome_describe")
	private String outcomeDescribe;

	@Relations(name="pictures",type="one2many",foreignKey = "o_id",action="query_insert")
	private List<Picture> pictures;

    private OutcomeType outcomeType;

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

    public float getOutcomeMoney() {
        return outcomeMoney;
    }

    public void setOutcomeMoney(float outcomeMoney) {
        this.outcomeMoney = outcomeMoney;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public long getOutcomeTime() {
        return outcomeTime;
    }

    public void setOutcomeTime(long outcomeTime) {
        this.outcomeTime = outcomeTime;
    }

    public ItemAddress getAddress() {
        return address;
    }

    public void setAddress(ItemAddress address) {
        this.address = address;
    }

    public String getOutcomeDescribe() {
        return outcomeDescribe;
    }

    public void setOutcomeDescribe(String outcomeDescribe) {
        this.outcomeDescribe = outcomeDescribe;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public OutcomeType getOutcomeType() {
        return outcomeType;
    }

    public void setOutcomeType(OutcomeType outcomeType) {
        this.outcomeType = outcomeType;
    }

    public OutcomeItem() {}


    @Override
    public String toString() {
        return "OutcomeItem{" +
                "_id=" + _id +
                ", oId='" + oId + '\'' +
                ", outcomeMoney=" + outcomeMoney +
                ", typeId=" + typeId +
                ", outcomeTime=" + outcomeTime +
                ", address=" + address +
                ", outcomeDescribe='" + outcomeDescribe + '\'' +
                ", pictures=" + pictures +
                ", outcomeType=" + outcomeType +
                '}';
    }

    public OutcomeItem( Context context,
                        String oId,
                       float outcomeMoney,
                       int outcomeType,
                       long outcomeTime,
                       ItemAddress address,
                       String outcomeDescribe,
                       List<Picture>plist) {
        this.oId = oId;
        this.outcomeMoney = outcomeMoney;
        this.typeId = outcomeType;
        this.outcomeTime = outcomeTime;
        this.address = address;
        this.outcomeDescribe = outcomeDescribe;
        this.pictures = plist;
    }

    public OutcomeItem(Parcel in){
            this._id = in.readInt();
            this.oId = in.readString();
            this.outcomeMoney = in.readFloat();
            this.outcomeDescribe = in.readString();
            this.typeId = in.readInt();
            this.outcomeTime = in.readLong();
            this.address = in.readParcelable(ItemAddress.class.getClassLoader());
            this.outcomeType =in.readParcelable(OutcomeType.class.getClassLoader());
            in.readList(this.pictures,Picture.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(this.oId);
        dest.writeFloat(this.outcomeMoney);
        dest.writeString(this.outcomeDescribe);
        dest.writeInt(this.typeId);
        dest.writeLong(this.outcomeTime);
        dest.writeParcelable(this.address,flags);
        dest.writeParcelable(this.outcomeType,flags);
        dest.writeList(pictures);

    }

    public static final Creator<OutcomeItem> CREATOR = new Creator<OutcomeItem>(){
        @Override
        public OutcomeItem createFromParcel(Parcel source) {
             return new OutcomeItem(source);
        }

        @Override
        public OutcomeItem[] newArray(int size) {
            return new OutcomeItem[size];
        }
    };
}


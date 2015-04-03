package com.oliver.moneyassistant.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

/**
 * Created by Oliver on 2015/3/17.
 */
@Table(name="outcome_type")
public class OutcomeType implements Parcelable{

    @Id
    @Column(name="_id")
    private int _id;

    @Column(name="res_id",type = "integer")
    private int resId;

    @Column(name="type_id",type ="integer")
    private int typeId;

    @Column(name="type_text")
    private String typeText;

    public OutcomeType(){};


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public OutcomeType(int resId,int typeId,String text){
        this.resId = resId;
        this.typeId = typeId;
        this.typeText = text;
    }

    @Override
    public String toString() {
        return "OutcomeType{" +
                "_id=" + _id +
                ", resId=" + resId +
                ", typeId=" + typeId +
                ", typeText='" + typeText + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeInt(this.resId);
        dest.writeInt(this.typeId);
        dest.writeString(this.typeText);
    }

    public static final Creator CREATOR = new Creator<OutcomeType>() {
        @Override
        public OutcomeType createFromParcel(Parcel source) {
            OutcomeType type = new OutcomeType();
            type._id = source.readInt();
            type.resId =source.readInt();
            type.typeId = source.readInt();
            type.typeText = source.readString();
            return type;
        }

        @Override
        public OutcomeType[] newArray(int size) {
            return new OutcomeType[size];
        }
    };
}

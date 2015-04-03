package com.oliver.moneyassistant.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Relations;
import com.ab.db.orm.annotation.Table;
import com.ab.db.orm.annotation.Id;
/**
 * Created by Oliver on 2015/3/15.
 */
@Table(name="income_item")
public class IncomeItem implements Parcelable{
    @Id
    @Column(name="_id")
    private int _id;

    @Column(name="income_money",type="float")
    private float incomeMoney;

    @Column(name="type_id",type="integer")
    private int typeId;

    @Column(name="income_time",type="integer")
    private long incomeTime;

    @Column(name="income_describe")
    private String incomeDescribe;

    private IncomeType incomeType;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public float getIncomeMoney() {
        return incomeMoney;
    }

    public void setIncomeMoney(float incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public long getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(long incomeTime) {
        this.incomeTime = incomeTime;
    }

    public String getIncomeDescribe() {
        return incomeDescribe;
    }

    public void setIncomeDescribe(String incomeDescribe) {
        this.incomeDescribe = incomeDescribe;
    }

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    @Override
    public String toString() {
        return "IncomeItem{" +
                "_id=" + _id +
                ", incomeMoney=" + incomeMoney +
                ", typeId=" + typeId +
                ", incomeTime=" + incomeTime +
                ", incomeDescribe='" + incomeDescribe + '\'' +
                ", incomeType=" + incomeType +
                '}';
    }

    public IncomeItem(){}

    public IncomeItem(float incomeMoney, int typeId, long incomeTime, String incomeDescribe) {
        this.incomeMoney = incomeMoney;
        this.typeId = typeId;
        this.incomeTime = incomeTime;
        this.incomeDescribe = incomeDescribe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeFloat(this.incomeMoney);
        dest.writeInt(this.typeId);
        dest.writeString(this.incomeDescribe);
        dest.writeParcelable(this.incomeType,flags);
    }

    public static final Creator<IncomeItem> CREATOR = new Parcelable.Creator<IncomeItem>(){
        @Override
        public IncomeItem createFromParcel(Parcel source) {
            IncomeItem item = new IncomeItem();
            item._id=source.readInt();
            item.incomeMoney = source.readFloat();
            item.typeId = source.readInt();
            item.incomeDescribe = source.readString();
            item.incomeType = source.readParcelable(IncomeItem.class.getClassLoader());
            return item;
        }

        @Override
        public IncomeItem[] newArray(int size) {
            return new IncomeItem[0];
        }
    };
}

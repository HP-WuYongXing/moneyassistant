package com.oliver.moneyassistant.db.models;

/**
 * Created by Oliver on 2015/3/15.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.ab.db.orm.annotation.*;

@Table(name="budget")
public class Budget implements Parcelable{
    @Id
    @Column(name="_id")
    private int _id;

  //  @Column(name="budget_type",type ="integer")
  //  private int budgetType;

    @Column(name="budget_time",type = "integer")
    private long budgetTime;

    @Column(name="start_time",type="integer")
    private long startTime;

    @Column(name="end_time",type = "integer")
    private long endTime;

    @Column(name="money",type = "float")
    private float money;

    @Column(name="budget_describe")
    private String budgetDescribe;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

/*    public int getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(int budgetType) {
        this.budgetType = budgetType;
    }*/

    public long getBudgetTime() {
        return budgetTime;
    }

    public void setBudgetTime(long budgetTime) {
        this.budgetTime = budgetTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getBudgetDescribe() {
        return budgetDescribe;
    }

    public void setBudgetDescribe(String budgetDescribe) {
        this.budgetDescribe = budgetDescribe;
    }

    public Budget(){}


    public Budget(int budgetType, long budgetTime, long startTime, long endTime, float money) {
       // this.budgetType = budgetType;
        this.budgetTime = budgetTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.money = money;
    }


    @Override
    public String toString() {
        return "Budget{" +
                "_id=" + _id +
                ", budgetTime=" + budgetTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", money=" + money +
                ", budgetDescribe='" + budgetDescribe + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

            dest.writeInt(this._id);
            dest.writeLong(this.budgetTime);
            dest.writeLong(this.startTime);
            dest.writeLong(this.endTime);
            dest.writeFloat(this.money);
            dest.writeString(this.budgetDescribe);
    }
    public static final Creator<Budget> CREATOR = new Creator<Budget>() {
        @Override
        public Budget createFromParcel(Parcel source) {
            Budget b = new Budget();
            b._id = source.readInt();
            b.budgetTime = source.readLong();
            b.startTime = source.readLong();
            b.endTime = source.readLong();
            b.money = source.readFloat();
            b.budgetDescribe = source.readString();
            return b;
        }

        @Override
        public Budget[] newArray(int size) {
            return new Budget[size];
        }
    };
}

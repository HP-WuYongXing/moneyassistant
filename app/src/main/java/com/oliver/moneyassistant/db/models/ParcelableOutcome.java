package com.oliver.moneyassistant.db.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Oliver on 2015/3/16.
 */
public class ParcelableOutcome implements Parcelable{
    public int _id;
    public float outcomeMoney;
    public long outcomeTime;
    public int outcomeType;
    public String addressHeader;
    public String describe;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(_id);
            dest.writeFloat(this.outcomeMoney);
            dest.writeLong(this.outcomeTime);
            dest.writeInt(outcomeType);
            dest.writeString(this.addressHeader);
            dest.writeString(this.describe);
    }

    public static final Creator<ParcelableOutcome> CREATOR = new Creator<ParcelableOutcome>(){
        @Override
        public ParcelableOutcome createFromParcel(Parcel source) {
            ParcelableOutcome p = new ParcelableOutcome();
            p._id = source.readInt();
            p.outcomeMoney = source.readFloat();
            p.outcomeTime = source.readLong();
            p.outcomeType = source.readInt();
            p.addressHeader = source.readString();
            p.describe = source.readString();
            return p;
        }

        @Override
        public ParcelableOutcome[] newArray(int size) {
            return new ParcelableOutcome[size];
        }
    };

    public ParcelableOutcome() {}
}

package com.oliver.moneyassistant.db.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

@Table(name = "picture")
public class Picture implements Parcelable{

	@Id
	@Column(name = "_id")
	private int _id;

	@Column(name = "img_path")
	private String imgPath;
	
	@Column(name = "o_id")
	private String oId;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getoId() {
		return oId;
	}

	public void setoId(String oId) {
		this.oId = oId;
	}

    public Picture(){}
	@Override
	public String toString() {
		return "Picture [_id=" + _id + ", imgUri=" + imgPath + ", oId=" + oId + "]";
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(this.imgPath);
        dest.writeString(this.oId);
    }
    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel source) {

            return new Picture(source);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    private Picture(Parcel in){
        this._id = in.readInt();
        this.imgPath = in.readString();
        this.oId = in.readString();
    }

}

package com.ak.newstag.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {

	public String getTag() {
		return tag;
	}

	public int getCount() {
		return count;
	}

	private String tag;

	private int count;

	private float scale;
	private int color;
	private int textSize;
	private float locX, locY, locZ;
	private float loc2DX, loc2DY;
	private int paramNo;

	public int getParamNo() {
		return paramNo;
	}

	public void setParamNo(int paramNo) {
		this.paramNo = paramNo;
	}

	public float getLoc2DX() {
		return loc2DX;
	}

	public void setLoc2DX(float loc2dx) {
		loc2DX = loc2dx;
	}

	public float getLoc2DY() {
		return loc2DY;
	}

	public void setLoc2DY(float loc2dy) {
		loc2DY = loc2dy;
	}

	public float getLocX() {
		return locX;
	}

	public void setLocX(float locX) {
		this.locX = locX;
	}

	public float getLocY() {
		return locY;
	}

	public void setLocY(float locY) {
		this.locY = locY;
	}

	public float getLocZ() {
		return locZ;
	}

	public void setLocZ(float locZ) {
		this.locZ = locZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Tag() {
		setColor(Color.rgb((int) Math.floor(Math.random() * 128) + 64,
				(int) Math.floor(Math.random() * 128) + 64,
				(int) Math.floor(Math.random() * 128) + 64));
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Tag(Parcel in) {
		this.tag = in.readString();
		this.count = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(tag);
		dest.writeInt(count);
	}

	public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
		public Tag createFromParcel(Parcel in) {
			return new Tag(in);
		}

		public Tag[] newArray(int size) {
			return new Tag[size];
		}
	};
}

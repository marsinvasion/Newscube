package com.ak.newstag.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
	
	public String getBirthDay() {
		return birthDay;
	}

	public Person setBirthDay(String birthDay) {
		this.birthDay = birthDay;
		return this;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public Person setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Person setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public int getGender() {
		return gender;
	}

	public Person setGender(int gender) {
		this.gender = gender;
		return this;
	}

	public String getId() {
		return id;
	}

	public Person setId(String id) {
		this.id = id;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public Person setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public Person setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	private String birthDay;

	private String currentLocation;

	private String displayName;

	private int gender;

	private String id;

	private String firstName;

	private String lastName;
	
	private String accountName;

	// Parcelling part
	public Person(Parcel in) {
		String[] data = new String[7];

		in.readStringArray(data);
		this.birthDay = data[0];
		this.currentLocation = data[1];
		this.displayName = data[2];
		this.id = data[3];
		this.firstName = data[4];
		this.lastName = data[5];
		this.setAccountName(data[6]);
		this.gender = in.readInt();
	}

	public Person() {
		super();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { this.birthDay,
				this.currentLocation, this.displayName, this.id,
				this.firstName, this.lastName, this.getAccountName() });
		dest.writeInt(gender);
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
		public Person createFromParcel(Parcel in) {
			return new Person(in);
		}

		public Person[] newArray(int size) {
			return new Person[size];
		}
	};

}

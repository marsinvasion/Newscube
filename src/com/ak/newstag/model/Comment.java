package com.ak.newstag.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

	private String id;

	private int score;

	private String comment;

	private String accountName;

	private String displayName;

	private Date inserted_at;

	private String published;

	private Comment[] comments;

	@Override
	public String toString() {
		return "id=" + id + ", score=" + score + ", comment=" + comment
				+ ", accountName=" + accountName + ", displayName="
				+ displayName + ", published=" + published + ", comments="
				+ comments.toString();
	}

	// Parcelling part
	public Comment(Parcel in) {
		String[] data = new String[5];

		in.readStringArray(data);
		this.id = data[0];
		this.comment = data[1];
		this.accountName = data[2];
		this.displayName = data[3];
		this.setPublished(data[4]);
		this.score = in.readInt();
		Parcelable[] parcelable = in.readParcelableArray(Comment.class
				.getClassLoader());
		comments = new Comment[parcelable.length];
		for(int i = 0; i< parcelable.length; i++){
			comments[i] = (Comment) parcelable[i];
		}
	}

	public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
		public Comment createFromParcel(Parcel in) {
			return new Comment(in);
		}

		public Comment[] newArray(int size) {
			return new Comment[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { this.id, this.comment,
				this.accountName, this.displayName, this.getPublished() });
		dest.writeInt(score);
		dest.writeParcelableArray(comments, 0);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Date getInserted_at() {
		return inserted_at;
	}

	public void setInserted_at(Date inserted_at) {
		this.inserted_at = inserted_at;
	}

	public Comment[] getComments() {
		return comments;
	}

	public void setComments(Comment[] comments) {
		this.comments = comments;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

}

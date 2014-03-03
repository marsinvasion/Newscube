package com.ak.newstag.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anoopkulkarni
 * 
 */
public class News implements Parcelable {

	private String title;

	private String summary;

	private String published;

	private Date published_at;

	private String website;

	private String source;

	private String url;

	private String id;

	private int score;

	private Comment[] comments;

	@Override
	public String toString() {
		return "title=" + title + ", summary=" + summary + ", published="
				+ published + ", website=" + website + ", source=" + source
				+ ", url=" + url + ", id=" + id + ", score=" + score
				+ ", comments=" + comments.toString();
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	// Parcelling part
	public News(Parcel in) {
		String[] data = new String[7];

		in.readStringArray(data);
		this.title = data[0];
		this.summary = data[1];
		this.published = data[2];
		this.website = data[3];
		this.source = data[4];
		this.url = data[5];
		this.id = data[6];
		this.score = in.readInt();
		Parcelable[] parcelable = in.readParcelableArray(Comment.class
				.getClassLoader());
		if (parcelable != null && parcelable.length > 0) {
			comments = new Comment[parcelable.length];
			for (int i = 0; i < parcelable.length; i++) {
				comments[i] = (Comment) parcelable[i];
			}
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { this.title, this.summary,
				this.published, this.website, this.source, this.url, this.id });
		dest.writeInt(score);
		dest.writeParcelableArray(comments, 0);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public Comment[] getComments() {
		return comments;
	}

	public void setComments(Comment[] comments) {
		this.comments = comments;
	}

	public Date getPublished_at() {
		return published_at;
	}

	public void setPublished_at(Date published_at) {
		this.published_at = published_at;
	}

	public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
		public News createFromParcel(Parcel in) {
			return new News(in);
		}

		public News[] newArray(int size) {
			return new News[size];
		}
	};

}

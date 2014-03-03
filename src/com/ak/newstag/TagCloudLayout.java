package com.ak.newstag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.newstag.model.Tag;

public class TagCloudLayout extends RelativeLayout {
	private final float TOUCH_SCALE_FACTOR = .8f;
	private final float TRACKBALL_SCALE_FACTOR = 10;
	private float tspeed;
	private TagCloud mTagCloud;
	private float mAngleX = 0;
	private float mAngleY = 0;
	private float centerX, centerY;
	private float radius;
	private INActivity mContext;
	private List<TextView> mTextView;
	private List<RelativeLayout.LayoutParams> mParams;
	private int shiftLeft;
	private String baseTagUrl;
	
	public TagCloudLayout(Context context){
		super(context);
	}

	public TagCloudLayout(INActivity context, int width, int height,
			List<Tag> tags, String tagUrl) {
		this(context, width, height, tags, 5, 40, 5, tagUrl);
	}

	public TagCloudLayout(INActivity mContext, int width, int height,
			List<Tag> tagList, int textSizeMin, int textSizeMax,
			int scrollSpeed, String tagUrl) {

		super(mContext);
		this.mContext = mContext;
		baseTagUrl = tagUrl;

		tspeed = scrollSpeed;

		// set the center of the sphere on center of our screen:
		centerX = width / 2;
		centerY = height / 2;
		radius = Math.min(centerX * 0.85f, centerY * 0.85f); // use 85% of
																// screen
		// since we set tag margins from left of screen, we shift the whole tags
		// to left so that
		// it looks more realistic and symmetric relative to center of screen in
		// X direction
		shiftLeft = (int) (Math.min(centerX * 0.15f, centerY * 0.15f));

		// initialize the TagCloud from a list of tags
		// Filter() func. screens tagList and ignores Tags with same text (Case
		// Insensitive)
		mTagCloud = new TagCloud(filter(tagList), (int) radius, textSizeMin,
				textSizeMax);
		float[] tempColor1 = { 0.9412f, 0.7686f, 0.2f, 1 }; // rgb Alpha
		// {1f,0f,0f,1} red {0.3882f,0.21568f,0.0f,1} orange
		// {0.9412f,0.7686f,0.2f,1} light orange
		float[] tempColor2 = { 1f, 0f, 0f, 1 }; // rgb Alpha
												// {0f,0f,1f,1} blue
												// {0.1294f,0.1294f,0.1294f,1}
												// grey
												// {0.9412f,0.7686f,0.2f,1}
												// light orange
		mTagCloud.setTagColor1(tempColor1);// higher color
		mTagCloud.setTagColor2(tempColor2);// lower color
		mTagCloud.setRadius((int) radius);
		mTagCloud.create(true); // to put each Tag at its correct initial
								// location

		// update the transparency/scale of tags
		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update();

		mTextView = new ArrayList<TextView>();
		mParams = new ArrayList<RelativeLayout.LayoutParams>();
		// Now Draw the 3D objects: for all the tags in the TagCloud
		Iterator<Tag> it = mTagCloud.iterator();
		Tag tempTag;
		int i = 0;
		while (it.hasNext()) {
			tempTag = it.next();
			tempTag.setParamNo(i); // store the parameter No. related to this
									// tag

			TextView textView = new TextView(this.mContext);

			textView.setText(tempTag.getTag());

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			layoutParams.setMargins(
					(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()), 0, 0);
			textView.setLayoutParams(layoutParams);

			textView.setSingleLine(true);
			textView.setTextColor(tempTag.getColor());
			textView.setTextSize((int) (tempTag.getTextSize() * tempTag
					.getScale()));
			addView(textView);
			textView.setOnClickListener(OnTagClickListener(getUrl(tempTag
					.getTag())));
			mTextView.add(textView);
			mParams.add(layoutParams);
			i++;
		}
	}

	private String getUrl(String tag) {
		return baseTagUrl + "/" + tag;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();

		mAngleX = (y) * tspeed * TRACKBALL_SCALE_FACTOR;
		mAngleY = (-x) * tspeed * TRACKBALL_SCALE_FACTOR;

		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update();

		Iterator<Tag> it = mTagCloud.iterator();
		Tag tempTag;
		while (it.hasNext()) {
			tempTag = it.next();
			mParams.get(tempTag.getParamNo()).setMargins(
					(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()), 0, 0);
			mTextView.get(tempTag.getParamNo()).setTextSize(
					(int) (tempTag.getTextSize() * tempTag.getScale()));
			mTextView.get(tempTag.getParamNo())
					.setTextColor(tempTag.getColor());
			mTextView.get(tempTag.getParamNo()).bringToFront();
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			// rotate elements depending on how far the selection point is from
			// center of cloud
			float dx = x - centerX;
			float dy = y - centerY;
			mAngleX = (dy / radius) * tspeed * TOUCH_SCALE_FACTOR;
			mAngleY = (-dx / radius) * tspeed * TOUCH_SCALE_FACTOR;

			mTagCloud.setAngleX(mAngleX);
			mTagCloud.setAngleY(mAngleY);
			mTagCloud.update();

			Iterator<Tag> it = mTagCloud.iterator();
			Tag tempTag;
			while (it.hasNext()) {
				tempTag = it.next();
				mParams.get(tempTag.getParamNo()).setMargins(
						(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
						(int) (centerY + tempTag.getLoc2DY()), 0, 0);
				mTextView.get(tempTag.getParamNo()).setTextSize(
						(int) (tempTag.getTextSize() * tempTag.getScale()));
				mTextView.get(tempTag.getParamNo()).setTextColor(
						tempTag.getColor());
				mTextView.get(tempTag.getParamNo()).bringToFront();
			}

			break;
		}

		return true;
	}


	// the filter function makes sure that there all elements are having unique
	// Text field:
	List<Tag> filter(List<Tag> tagList) {
		// current implementation is O(n^2) but since the number of tags are not
		// that many,
		// it is acceptable.
		List<Tag> tempTagList = new ArrayList<Tag>();
		Iterator<Tag> itr = tagList.iterator();
		Iterator<Tag> itrInternal;
		Tag tempTag1, tempTag2;
		// for all elements of TagList
		while (itr.hasNext()) {
			tempTag1 = (itr.next());
			boolean found = false;
			// go over all elements of temoTagList
			itrInternal = tempTagList.iterator();
			while (itrInternal.hasNext()) {
				tempTag2 = (itrInternal.next());
				if (tempTag2.getTag().equalsIgnoreCase(tempTag1.getTag())) {
					found = true;
					break;
				}
			}
			if (found == false)
				tempTagList.add(tempTag1);
		}
		return tempTagList;
	}

	// for handling the click on the tags
	// onclick open the tag url in a new window. Back button will bring you back
	// to TagCloud
	View.OnClickListener OnTagClickListener(final String url) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.setSelectedNewsUrl(url);
				mContext.refresh();
			}
		};
	}

}

package com.ak.newstag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class CommentDialogFragment extends DialogFragment {

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface CommentDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	protected static final String HEAD_ID = "com.ak.newstag.headId";

	// Use this instance of the interface to deliver action events
	CommentDialogListener mListener;

	public CommentDialogFragment() {
		super();
	}

	private String headId;

	private String headComment;

	// Override the Fragment.onAttach() method to instantiate the
	// NoticeDialogListener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (CommentDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	/** The system calls this only when creating the layout in a dialog. */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setPositiveButton(R.string.go,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Send the positive button event back to the
						// host activity
						mListener
								.onDialogPositiveClick(CommentDialogFragment.this);
					}
				}).setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Send the negative button event back to the
						// host activity
						mListener
								.onDialogNegativeClick(CommentDialogFragment.this);
					}
				});
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_comment, null);
		TextView headIdView = (TextView) view.findViewById(R.id.headId);
		headIdView.setText(headId);
		TextView headCommentView = (TextView) view
				.findViewById(R.id.headComment);
		if (headComment != null) {
			headCommentView.setText(headComment);
		} else {
			headCommentView.setVisibility(View.GONE);
		}
		builder.setView(view);
		Dialog dialog = builder.create();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getHeadComment() {
		return headComment;
	}

	public void setHeadComment(String headComment) {
		this.headComment = headComment;
	}
}
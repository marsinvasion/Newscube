package com.ak.newstag.receiver;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.ak.newstag.R;
import com.ak.newstag.service.PutService;

public class PutReceiver extends ResultReceiver {

	private Activity activity;

	public PutReceiver(Handler handler, Activity activity) {
		super(handler);
		this.activity = activity;
	}

	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if (resultCode == PutService.PUT_PROGRESS) {
			int result = resultData.getInt(PutService.PUT_RESULT);
			switch (result) {
			case PutService.UNAUTHORIZED:
				Toast toast = Toast.makeText(activity, R.string.log_out_in,
						Toast.LENGTH_LONG);
				toast.show();
				break;
			case PutService.NOT_FOUND:
				toast = Toast.makeText(activity, R.string.resource_not_found,
						Toast.LENGTH_LONG);
				toast.show();
				break;
			case PutService.ERROR:
				toast = Toast.makeText(activity, R.string.put_error,
						Toast.LENGTH_LONG);
				toast.show();
				break;
			default:
				toast = Toast.makeText(activity, R.string.success,
						Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}
}

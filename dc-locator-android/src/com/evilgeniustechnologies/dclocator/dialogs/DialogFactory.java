package com.evilgeniustechnologies.dclocator.dialogs;

import com.evilgeniustechnologies.dclocator.type.DialogType;

import android.app.Dialog;
import android.content.Context;

public class DialogFactory {
	public static Dialog GetDialog(Context context, int type){
		switch (type) {
		case DialogType.DIALOG_CHECKIN:
			return new CheckinDialog(context);
			
		case DialogType.DIALOG_WHEELDAY:
			return new WheelBirthdayDialog(context);
		default:
			break;
		}
		return null;
		
	}

}

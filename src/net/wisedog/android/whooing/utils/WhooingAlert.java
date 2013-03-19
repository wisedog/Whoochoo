package net.wisedog.android.whooing.utils;

import net.wisedog.android.whooing.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class WhooingAlert {
	/**
	 * Show alert that inform user not enough message
	 * */
	static public void showNotEnoughApi(Context context){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.global_no_api_inform_title));
        alertDialogBuilder.setMessage(context.getString(R.string.global_no_api_inform))
        .setCancelable(true)
        .setPositiveButton(context.getString(R.string.text_okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();

            }
        });
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}

	/**
	 * Show alert that leads user to go whooing webpage
	 * @param	context		context
	 * @param	msg			Alert dialog message
	 * */
	public static void showGoWhooing(final Context context, String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.text_inform));
        alertDialogBuilder.setMessage(msg)
        .setCancelable(true)
        .setPositiveButton(context.getString(R.string.alert_go_setting_whooing_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	Intent intent = new Intent(Intent.ACTION_VIEW);
            	intent.setData(Uri.parse("http://www.whooing.com"));
            	context.startActivity(intent);
            	dialog.dismiss();

            }
        })
        .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dialog.dismiss();
            }
        });
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
		
	}
}

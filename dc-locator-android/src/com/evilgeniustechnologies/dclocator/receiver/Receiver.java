package com.evilgeniustechnologies.dclocator.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.evilgeniustechnologies.dclocator.R;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import com.evilgeniustechnologies.dclocator.commons.Config;
import com.evilgeniustechnologies.dclocator.dc_locator.MainViewActivity;

public class Receiver extends ParsePushBroadcastReceiver {

    private static final String TAG = "Receiver";
    String userId;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "onReceive");
        if (intent != null
                && intent.getAction().equals("com.parse.push.intent.RECEIVE")) {
            try {
                String content = intent.getExtras().getString("com.parse.Data");
                Log.d(TAG, content);
                JSONObject json = new JSONObject(content);
                String needSplid = json.getString("alert");
                sentBroadCast(context, content);
                pushNotification(context, needSplid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPushReceive(Context context, Intent intent) {
        Log.i(TAG, "onPushReceive");
        ParseAnalytics.trackAppOpened(intent);
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        Log.e(TAG, intent.toString());
    }

    private void pushNotification(Context context, String content) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("DC_LOCATOR").setContentText(content);
        Intent resultIntent = new Intent(context, MainViewActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainViewActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void sentBroadCast(Context context, String message) {
        Intent intent = new Intent("update_data");
        intent.putExtra("message", message);
        context.sendBroadcast(intent);
    }

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.i(TAG, "onPushOpen");
        Intent i = new Intent(context, MainViewActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        Log.i(TAG, "getNotification");
        SharedPreferences ref = context.getSharedPreferences(
                Config.SHARE_PREFERENCES, 0);
        userId = ref.getString(Config.USERID_KEY, null);
        try {
            if (intent == null) {
                Log.d(TAG, "Receiver intent null");
            } else {
                String action = intent.getAction();
                Log.d(TAG, "got action " + action);
                if (action.equals("com.parse.push.intent.RECEIVE")) {
                    String channel = intent.getExtras().getString(
                            "com.parse.Channel");
                    JSONObject json = new JSONObject(intent.getExtras()
                            .getString("com.parse.Data"));
                    String noti = json.getString("badge");
                    String Namesender = json.getString("sender");
                    String needSplid = json.getString("alert");
                    Log.d(TAG, "json: " + json.toString());
                    int icon = R.drawable.ic_launcher;
                    long when = System.currentTimeMillis();

                    // NotificationManager notificationManager =
                    // (NotificationManager)
                    // context.getSystemService(Context.NOTIFICATION_SERVICE);
                    //
                    // Notification notification =new
                    // Notification.Builder(context)
                    // .setSmallIcon(icon)
                    // .setContentText(needSplid)
                    // .setContentTitle(context.getResources().getString(R.string.app_name))
                    // .setWhen(when)
                    // .build();
                    //
                    // Notification notification1 = new Notification();
                    //
                    // String title = context.getString(R.string.app_name);
                    // Intent notificationIntent = new Intent(context,
                    // MainViewActivity.class);
                    //
                    // PendingIntent pintent =
                    // PendingIntent.getActivity(context, 0, notificationIntent,
                    // Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    // Intent.FLAG_ACTIVITY_NEW_TASK);
                    //
                    // // notification.setLatestEventInfo(context, title,
                    // needSplid, pintent);
                    // notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    // notification.defaults |= Notification.DEFAULT_SOUND;
                    // notification.defaults |= Notification.DEFAULT_VIBRATE;
                    // notificationManager.notify(1, notification);
                    // // MainViewActivity activity= new MainViewActivity();
                    // // activity.setCountNotification(Integer.parseInt(noti));
                    // //
                    // // for (GroupItemChat element : Utils.groupId(context,
                    // Utils.SortList(Utils.List(userId)))) {
                    // // if(element.groupName.equals(Namesender)){
                    // // element.ReadMessage= (element.ReadMessage+1);
                    // // }
                    // // }
                    // Log.d(TAG, "got action " + action + " on channel " +
                    // channel + " with:");
                    // Iterator itr = json.keys();
                    // while (itr.hasNext()) {
                    // String key = (String) itr.next();
                    // Intent pupInt = new Intent(context,
                    // MainViewActivity.class);
                    // pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // context.getApplicationContext().sendBroadcast(pupInt);
                    // Log.d(TAG, "..." + key + " => " + json.getString(key));
                    //
                    // }
                }
            }

        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }

        return super.getNotification(context, intent);
    }
}

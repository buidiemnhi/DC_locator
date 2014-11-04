package com.evilgeniustechnologies.dclocator.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import com.evilgeniustechnologies.dclocator.asynctasks.RestClient.RequestMethod;
import com.evilgeniustechnologies.dclocator.commons.Config;
import com.evilgeniustechnologies.dclocator.dc_locator.MainViewActivity;

public class LoginAsyncstask extends AsyncTask<String, Void, String> {
    Activity activity;

    public String UserName;
    public String pass;
    private Handler handler;
    List<ParseObject> login_email;
    ParseObject userInfo;
    ProgressDialog progressDialog;
    MainViewActivity main;

    private static final String TAG = "RestClient";
    private static final String USER_AGENT = "VisioStorm Android";
    public RestClient restClient;
    Context context;

    public LoginAsyncstask(Activity activity, Context context, String UserName, String pass) {
        this.activity = activity;
        this.UserName = UserName;
        this.pass = pass;
        progressDialog = new ProgressDialog(activity);
        handler = new Handler();
        main = new MainViewActivity();
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog == null)
            progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (restClient.getResponse() == 200) {
            progressDialog.dismiss();
         //   ParseService.startActionParse(activity);
            transitionToMainView();
        } else {
            progressDialog.dismiss();
            Toast toast= Toast.makeText(context,
                    "Email or Password is incorrect, please try again!",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            
        }
    }

    private void transitionToMainView() {
        progressDialog.dismiss();
        try {
            ParseLogin();
        } catch (ParseException e) {
            Log.e(TAG, "LoginAsyncTask", e);
        }
        handler.post(new Runnable() {

            @Override
            public void run() {
                Intent intentArea = new Intent(activity,
                        MainViewActivity.class);
                activity.startActivity(intentArea);
                activity.overridePendingTransition(0, 0);
            }
        });
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                Intent intentSplash = new Intent(activity, dc_locator.SplashActivity.class);
//                activity.startActivity(intentSplash);
//            }
//        }, 200);
        activity.finish();
    }

    @Override
    protected String doInBackground(String... params) {
        String Url = "https://external.ningapis.com/xn/rest/dynamitecircle/1.0/Token";
        String oauth_consumer_key = "17933cb8-c8d3-470c-a180-41c14f2ab84c";
        String oauth_signature = "f42ebf14-fa5b-4f3a-b2e9-9937646f43b9&";
        String oauth_signature_method = "PLAINTEXT";

        try {
            restClient = new RestClient(Url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "init restClient", e);
        }

        restClient.addParam("oauth_consumer_key", oauth_consumer_key);
        restClient.addParam("oauth_signature", oauth_signature);
        restClient.addParam("oauth_signature_method", oauth_signature_method);
        try {
            restClient.execute(RequestMethod.POST, UserName, pass);
        } catch (Exception e) {
            Log.e(TAG, "execute restClient", e);
            try {
                restClient.execute(RequestMethod.GET, UserName, pass);
            } catch (NoSuchMethodException e1) {
                Log.e(TAG, "re-execute restClient", e1);
            } catch (URISyntaxException e1) {
                Log.e(TAG, "re-execute restClient", e1);
            }
        }

        return String.valueOf(restClient.getResponse());
    }

    public void ParseLogin() throws ParseException {
        ParseQuery<ParseObject> loginQuery = new ParseQuery<ParseObject>(
                "Member");
        loginQuery.whereMatches("login_email", UserName, "i");

        userInfo = loginQuery.getFirst();
        Log.e(TAG, "is userInfo null? " + (userInfo == null));
        if (userInfo != null) {
            Log.e(TAG, "save to preferences");
            Log.e(TAG, "objectId " + userInfo.getObjectId());
            userInfo.put("available", true);
            logIn(userInfo.getObjectId(), UserName, userInfo);
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("user", userInfo);
            installation.save();
        }
    }

    private void logIn(String userID, String email, ParseObject object) {
        SharedPreferences ref = activity.getSharedPreferences(
                Config.SHARE_PREFERENCES, 0);
        SharedPreferences.Editor editor = ref.edit();
        Log.e(TAG, "save userId " + userID);
        editor.putString(Config.USERID_KEY, userID)
                .putString(Config.EMAIL_KEY, email)
                .putString(Config.AVATAR_KEY, object.getParseFile("avatar") != null ? object.getParseFile("avatar").getUrl() : "")
                .putString(Config.EX_KEY, object.getString("skill") != null ? object.getString("skill") : "").putString(Config.WEB_KEY, object.getString("website") != null ? object.getString("website") : "")
                .putString(Config.SKYPE_KEY, object.getString("skypeId") != null ? object.getString("skypeId") : "")
                .putString(Config.NAME_KEY, object.getString("fullName"))
                .putString(Config.TWITER_KEY, object.getString("twitter") != null ? object.getString("twitter") : "");
        Config.IdUser = userID;
        Config.EMAILUSER = email;
        editor.commit();
    }
}

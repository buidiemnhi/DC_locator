package com.evilgeniustechnologies.dclocator.dc_locator;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evilgeniustechnologies.dclocator.R;

import com.evilgeniustechnologies.dclocator.asynctasks.LoginAsyncstask;
import com.evilgeniustechnologies.dclocator.commons.Config;
import com.evilgeniustechnologies.dclocator.commons.ISConnectInternet;
import com.evilgeniustechnologies.dclocator.utils.ResizeUtils;

public class LoginActivity extends BaseActivity implements
        View.OnClickListener {
    private static final String TAG = "EGT.LoginActivity";
    Context context;
    Handler handler;
    Button btnlogin;
    TextView tvEmail, tvPass;
    ImageView ivInfo;
    EditText etEmail, etPass;
//    public Criteria criteria;
//    public static boolean isEnable = false;
    String Return;

    public String getReturn() {
        return Return;
    }

    public void setReturn(String return1) {
        this.Return = return1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.e(TAG, "start onCreate");
        super.onCreate(savedInstanceState);

      //  Crashlytics.start(this);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.context = this;

        handler = new Handler();
        setContentView(R.layout.login_layout);
        Config.screenWidth = ResizeUtils.getSizeDevice(this).x;
        Config.screenHeight = ResizeUtils.getSizeDevice(this).y;
        btnlogin = (Button) findViewById(R.id.btn_login);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvPass = (TextView) findViewById(R.id.tv_pass);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPass = (EditText) findViewById(R.id.et_pass);
        ivInfo = (ImageView) findViewById(R.id.iv_infor);
        ivInfo.setOnClickListener(this);

        etPass.setOnClickListener(this);
        // etPass.setOnTouchListener(this);
        etEmail.setOnClickListener(this);
        // etEmail.setOnTouchListener(this);
        // AjustUserinterface();

        if (!Config.EMAILUSER.equals("")) {
            etEmail.setText(Config.EMAILUSER);

        }
        autoLogin();

        etEmail.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    tvEmail.setVisibility(View.GONE);
                else
                    tvEmail.setVisibility(View.VISIBLE);
            }
        });

        etPass.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    tvPass.setVisibility(View.GONE);
                else
                    tvPass.setVisibility(View.VISIBLE);
            }
        });

        btnlogin.setOnClickListener(this);
        Log.e(TAG, "end onCreate");
    }

    public void autoLogin() {
        Log.e(TAG, "start autoLogin");
        SharedPreferences ref = getSharedPreferences(Config.SHARE_PREFERENCES, 0);
        String userId = ref.getString(Config.USERID_KEY, null);
//        final String email = ref.getString(Config.EMAIL_KEY, null);
        if (userId != null) {
            if (ISConnectInternet.isConnectedInternet(this)) {
            	handler.post(new Runnable() {

					@Override
					public void run() {
						Intent intentArea = new Intent(context,
								MainViewActivity.class);
						context.startActivity(intentArea);
						overridePendingTransition(0, 0);
					}
				});
//				handler.postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						Intent intentSplash = new Intent(context,
//								SplashActivity.class);
//						context.startActivity(intentSplash);
//					}
//				}, 200);
				this.finish();
            } else {
                ISConnectInternet.showAlertInternet(this);
            }
        }
        Log.e(TAG, "end autoLogin");
    }

    public void AjustUserinterface() {

        ResizeUtils.resizeButton(btnlogin, (int) (Config.screenWidth * 0.7),
                (int) (Config.screenHeight * 0.08), 0, 0,
                (int) (Config.screenHeight * 0.1), 0,
                RelativeLayout.CENTER_IN_PARENT);

        ResizeUtils.resizeTextView(tvEmail, (int) (Config.screenWidth * 0.3),
                (int) (Config.screenHeight * 0.05), 0, 0, 0, 0,
                RelativeLayout.CENTER_VERTICAL);
        ResizeUtils.resizeTextView(tvPass, (int) (Config.screenWidth * 0.3),
                (int) (Config.screenHeight * 0.05), 0, 0, 0, 0,
                RelativeLayout.CENTER_VERTICAL);

		/*
         * ResizeUtils.resizeEditText(etEmail, (int) (Config.screenWidth*0.5),
		 * (int)(Config.screenHeight*0.06), (int) (Config.screenWidth*0.3), 0,
		 * 0, 0, RelativeLayout.CENTER_VERTICAL);
		 * ResizeUtils.resizeEditText(etPass, (int) (Config.screenWidth*0.5),
		 * (int)(Config.screenHeight*0.06), (int) (Config.screenWidth*0.3), 0,
		 * 0, 0, RelativeLayout.CENTER_VERTICAL);
		 */

    }

    private boolean checkEditText() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";

        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.requestFocus();
            Toast toast=
            Toast.makeText(this,
                    getResources().getString(R.string.email_not_empty),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        } else if (TextUtils.isEmpty(etPass.getText().toString())) {

            etPass.requestFocus();
            Toast toast=Toast.makeText(this,
                    getResources().getString(R.string.password_not_empty),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        if (!etEmail.getText().toString().matches(emailPattern)) {

            etPass.requestFocus();
            Toast toast=
            Toast.makeText(this,
                    getResources().getString(R.string.email_not_formatted),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }

    public String Return_userName() {
        return etEmail.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (ISConnectInternet.isConnectedInternet(this)) {
                    if (checkEditText()) {
                        new LoginAsyncstask(this, this, etEmail.getText().toString(),
                                etPass.getText().toString()).execute();
                        setReturn(etEmail.getText().toString());
                    }
                } else {
                    ISConnectInternet.showAlertInternet(this);
                }
                break;
            case R.id.et_email:
                tvEmail.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.width = (int) (Config.screenWidth * 0.9);
                params.height = (int) (Config.screenHeight * 0.06);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                etEmail.setLayoutParams(params);

                break;
            case R.id.iv_infor:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.tropicalmba.com/innercircle/"));
                startActivity(intent);
                break;
            case R.id.et_pass:
                tvPass.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params1.width = (int) (Config.screenWidth * 0.9);
                params1.height = (int) (Config.screenHeight * 0.06);

                params1.addRule(RelativeLayout.CENTER_VERTICAL);

                etPass.setLayoutParams(params1);

                break;
            default:
                break;
        }
    }

//    @Override
//    protected void onReceived(String status) {
//        if (Datastore.DATABASE_READY.equals(status)) {
//            loginAsyncstask.transitionToMainView();
//        }
//    }

    // @Override
    // public boolean onTouch(View v, MotionEvent event) {
    // switch (v.getId()) {
    // case R.id.et_pass:
    // tvPass.setVisibility(View.GONE);
    // RelativeLayout.LayoutParams params1 = new
    // android.widget.RelativeLayout.LayoutParams(
    // android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
    // android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
    // params1.width = (int) (Config.screenWidth * 0.9);
    // params1.height = (int) (Config.screenHeight * 0.06);
    //
    // params1.addRule(RelativeLayout.CENTER_VERTICAL);
    //
    // etPass.setLayoutParams(params1);
    //
    // break;
    // }
    //
    // return false;
    // }
}

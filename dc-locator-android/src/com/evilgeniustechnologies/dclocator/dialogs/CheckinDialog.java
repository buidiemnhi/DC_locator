package com.evilgeniustechnologies.dclocator.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evilgeniustechnologies.dclocator.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.evilgeniustechnologies.dclocator.commons.Config;

public class CheckinDialog extends Dialog implements
        android.view.View.OnClickListener {
    RelativeLayout checkinDialog, rl_datime, rl_departure_day, rl_head, rl_home, rl_visit;
    ImageView close;
    TextView mylocation, tv_year, tv_month, tv_day;
    Button departureDay;
    ImageButton btn_public, btn_private;
    ImageView iv_home, iv_justVisit;
    Boolean is_home = true;
    String dates, months, years;
    Context context;
    String myLocation;

    public CheckinDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.checkin_dialog);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.context = context;
        checkinDialog = (RelativeLayout) findViewById(R.id.dialog_checkin);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // params.width = (int) (Config.screenWidth * 0.6);
        // params.height = (int) (Config.screenHeight * 0.3);
        params1.width = (int) (Config.screenWidth * 0.15);
        params1.height = (int) (Config.screenWidth * 0.15);

        close = (ImageView) findViewById(R.id.iv_close);
        mylocation = (TextView) findViewById(R.id.tv_my_location);
        rl_departure_day = (RelativeLayout) findViewById(R.id.departureDate);
        rl_home = (RelativeLayout) findViewById(R.id.rl_myhome);
        rl_visit = (RelativeLayout) findViewById(R.id.rl_visit);
        iv_home = (ImageView) findViewById(R.id.my_home);
        iv_justVisit = (ImageView) findViewById(R.id.my_visit);
        iv_home.setLayoutParams(params1);
        iv_justVisit.setLayoutParams(params1);
        rl_datime = (RelativeLayout) findViewById(R.id.rl_daytime);
        rl_datime.setVisibility(View.GONE);
        departureDay = (Button) findViewById(R.id.btn_departure);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_year = (TextView) findViewById(R.id.tv_year);
        btn_public = (ImageButton) findViewById(R.id.ibn_tell_dc);
        btn_private = (ImageButton) findViewById(R.id.ibn_incog);
        iv_home.setOnClickListener(this);
        iv_justVisit.setOnClickListener(this);
        departureDay.setOnClickListener(this);
        rl_departure_day.setOnClickListener(this);
        myLocation = getLocation();
        if (myLocation != null) {
            mylocation.setText(myLocation);
        } else {
            mylocation.setText("try load again to get your current Location");
        }
        rl_head = (RelativeLayout) findViewById(R.id.head);
        adjustUserInterFace();
    }

    public void setPublicClickListener(View.OnClickListener listener) {
        btn_public.setOnClickListener(listener);
    }

    public void setPrivateClickListener(View.OnClickListener listener) {
        btn_private.setOnClickListener(listener);
    }

    public void setClose(View.OnClickListener listener) {
        close.setOnClickListener(listener);
    }

    public void adjustUserInterFace() {
//	checkinDialog.getLayoutParams().width= (int)(Config.screenWidth*0.8);
//	checkinDialog.getLayoutParams().height= (int)(Config.screenHeight*0.8);
//		ResizeUtils.resizeImageView(close, (int) (Config.screenWidth * 0.05),
//				(int) (Config.screenWidth * 0.05), 0,
//				(int) (Config.screenWidth * 0.02),
//				(int) (Config.screenWidth * 0.02), 0,
//				RelativeLayout.ALIGN_PARENT_RIGHT,
//				RelativeLayout.ALIGN_PARENT_TOP);
//		ResizeUtils.resizeTextView(mylocation,
//				(int) (Config.screenWidth * 0.5),
//				(int) (Config.screenHeight * 0.18), 0, 0, 0, 0,
//				RelativeLayout.CENTER_IN_PARENT);
//		ResizeUtils.resizeImageView(iv_home, (int) (Config.screenWidth * 0.15),
//				(int) (Config.screenWidth * 0.15),
//				(int) (Config.screenWidth * 0.05), 0,
//				(int) (Config.screenWidth * 0.05),
//				(int) (Config.screenWidth * 0.05),
//				RelativeLayout.ALIGN_PARENT_LEFT);
//		ResizeUtils.resizeImageView(iv_justVisit,
//				(int) (Config.screenWidth * 0.15),
//				(int) (Config.screenWidth * 0.15),
//				(int) (Config.screenWidth * 0.05), 0,
//				(int) (Config.screenWidth * 0.05),
//				(int) (Config.screenWidth * 0.05),
//				RelativeLayout.ALIGN_PARENT_LEFT);
//		ResizeUtils.resizeButton(departureDay,
//				(int) (Config.screenWidth * 0.4),
//				(int) (Config.screenHeight * 0.08), 0, 0,
//				(int) (Config.screenWidth * 0.05),
//				(int) (Config.screenWidth * 0.05),
//				RelativeLayout.CENTER_IN_PARENT);
//		ResizeUtils.resizeRelativeLayout(rl_datime,
//				(int) (Config.screenWidth * 0.6),
//				(int) (Config.screenHeight * 0.08), 0, 0,
//				(int) (Config.screenWidth * 0.05),
//				(int) (Config.screenWidth * 0.05),
//				RelativeLayout.CENTER_IN_PARENT);
//		ResizeUtils.resizeImageButton(btn_public,
//				(int) (Config.screenWidth * 0.2),
//				(int) (Config.screenHeight * 0.07),(int) (Config.screenWidth * 0.15) , 0, 0,
//				(int) (Config.screenHeight * 0.025),
//				RelativeLayout.ALIGN_PARENT_BOTTOM);
//		ResizeUtils.resizeImageButton(btn_private,
//				(int) (Config.screenWidth * 0.2),
//				(int) (Config.screenHeight * 0.07), (int) (Config.screenWidth * 0.08), 0, 0,
//				(int) (Config.screenHeight * 0.025),
//				RelativeLayout.ALIGN_PARENT_BOTTOM);
//		RelativeLayout.LayoutParams tmp = (RelativeLayout.LayoutParams) mylocation
//				.getLayoutParams();
//		tmp.addRule(RelativeLayout.BELOW, rl_head.getId());
//		RelativeLayout.LayoutParams tmp1 = (RelativeLayout.LayoutParams) rl_home
//				.getLayoutParams();
//		tmp1.addRule(RelativeLayout.BELOW, mylocation.getId());
//		RelativeLayout.LayoutParams tmp2 = (RelativeLayout.LayoutParams) rl_visit
//				.getLayoutParams();
//		tmp2.addRule(RelativeLayout.BELOW, rl_home.getId());
//		RelativeLayout.LayoutParams tmp3= (RelativeLayout.LayoutParams) btn_public
//				.getLayoutParams();
//		tmp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.ALIGN_PARENT_LEFT);
//		RelativeLayout.LayoutParams tmp4 = (RelativeLayout.LayoutParams) btn_private
//				.getLayoutParams();
//		tmp4.addRule(RelativeLayout.RIGHT_OF,btn_public.getId());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_home:
                setIs_home(true);
                iv_home.setImageDrawable(context.getResources().getDrawable(R.drawable.dynamite_2x));
                iv_justVisit.setImageDrawable(context.getResources().getDrawable(R.drawable.gray_dynamite));
                break;
            case R.id.my_visit:
                rl_datime.setVisibility(View.GONE);
                rl_departure_day.setVisibility(View.VISIBLE);
                departureDay.setVisibility(View.VISIBLE);

                iv_justVisit.setImageDrawable(context.getResources().getDrawable(R.drawable.dynamite_2x));
                iv_home.setImageDrawable(context.getResources().getDrawable(R.drawable.gray_dynamite));
                if (getIs_home() == true) {
                    setIs_home(false);
                } else {
                    setIs_home(false);
                }
                break;
            case R.id.btn_departure:
                departureDay.setVisibility(View.GONE);
                rl_datime.setVisibility(View.VISIBLE);
                WheelBirthdayDialog dialog2 = (WheelBirthdayDialog) DialogFactory
                        .GetDialog(context, com.evilgeniustechnologies.dclocator.type.DialogType.DIALOG_WHEELDAY);
                dialog2.show();
                dialog2.setDialog(this);
                break;

            case R.id.departureDate:
                departureDay.setVisibility(View.GONE);
                rl_datime.setVisibility(View.VISIBLE);
                WheelBirthdayDialog dialog1 = (WheelBirthdayDialog) DialogFactory
                        .GetDialog(context, com.evilgeniustechnologies.dclocator.type.DialogType.DIALOG_WHEELDAY);
                dialog1.show();
                dialog1.setDialog(this);

                break;
            default:
                break;
        }

    }

    public void setStartTimeFromWheel(String day, String month, String year) {
        tv_day.setText(day);
        tv_month.setText(month);
        tv_year.setText(year);
        dates = day;
        months = month;
        years = year;
    }

    public void cancelWheel() {
        departureDay.setVisibility(View.VISIBLE);
        rl_datime.setVisibility(View.GONE);
    }

    public Date getdateTime() {
        Date mdate = null;
        String mydate = String.format("%s:%s:%s", years, months, dates);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:mm:dd");

        try {
            mdate = dateFormat.parse(mydate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mdate;
    }

    public Boolean getIs_home() {
        return is_home;
    }

    public void setIs_home(Boolean is_home) {
        this.is_home = is_home;
    }

    public String getLocation() {
        Geocoder geocoder;
        String ad = null;
        Address fulladresss;
        List<Address> addresses = new ArrayList<Address>();
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(Config.lat, Config.lng, 1);
        } catch (IOException e) {

            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            fulladresss = addresses.get(0);
            StringBuilder strReturnedAddress = new StringBuilder("");
            for (int i = 0; i < fulladresss.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(fulladresss.getAddressLine(i))
                        .append(",");
            }
            ad = strReturnedAddress.toString();
        } else {
            Toast.makeText(context, "can not get location right now",
                    Toast.LENGTH_LONG).show();
        }

        return ad;

    }

}

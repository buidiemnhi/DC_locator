package com.evilgeniustechnologies.dclocator.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.evilgeniustechnologies.dclocator.R;

import java.util.Calendar;

import com.evilgeniustechnologies.dclocator.commons.Config;
import com.evilgeniustechnologies.dclocator.utils.ResizeUtils;
import com.evilgeniustechnologies.dclocator.wheel.widget.OnWheelChangedListener;
import com.evilgeniustechnologies.dclocator.wheel.widget.WheelView;
import com.evilgeniustechnologies.dclocator.wheel.widget.adapters.NumericWheelAdapter;

public class WheelBirthdayDialog extends Dialog implements
        android.view.View.OnClickListener {

    Context context;
    LinearLayout llWheelDialog;
    WheelView wvYear, wvMonth, wvDay;
    Button btnOk, btnCancel;
    CheckinDialog dialog;

    public WheelBirthdayDialog(final Context context) {
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_wheel_birthday);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        llWheelDialog = (LinearLayout) findViewById(R.id.ll_wheel_dialog_birth);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.width = (int) (Config.screenWidth * 0.6);
        params.height = (int) (Config.screenHeight * 0.3);
        llWheelDialog.setLayoutParams(params);

        btnCancel = (Button) findViewById(R.id.btn_wheel_cancel);
        btnOk = (Button) findViewById(R.id.btn_wheel_ok);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        adjustInterface();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int Month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        wvYear = (WheelView) findViewById(R.id.wv_year);
        NumericWheelAdapter yearAdapter = new NumericWheelAdapter(context,
                year, 2030);
        yearAdapter.setItemResource(R.layout.wheel_text_item);
        yearAdapter.setItemTextResource(R.id.text);
        wvYear.setBackgroundColor(context.getResources()
                .getColor(R.color.white));
        wvYear.setViewAdapter(yearAdapter);

        wvMonth = (WheelView) findViewById(R.id.wv_month);
        NumericWheelAdapter monthAdapter = new NumericWheelAdapter(context, 1,
                12, "%02d");
        monthAdapter.setItemResource(R.layout.wheel_text_item);
        monthAdapter.setItemTextResource(R.id.text);
        wvMonth.setViewAdapter(monthAdapter);
        wvMonth.setCyclic(true);

        wvDay = (WheelView) findViewById(R.id.wv_day);
        NumericWheelAdapter dayAdapter = new NumericWheelAdapter(context, 1,
                31, "%02d");
        dayAdapter.setItemResource(R.layout.wheel_text_item);
        dayAdapter.setItemTextResource(R.id.text);
        wvDay.setViewAdapter(dayAdapter);
        wvDay.setCyclic(true);

        wvMonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                Calendar calendar = (Calendar) (Calendar.getInstance()).clone();
                calendar.set(2014, newValue, 1);
                NumericWheelAdapter dayAdapter = new NumericWheelAdapter(
                        context, 01, calendar
                        .getActualMaximum(Calendar.DAY_OF_MONTH),
                        "%02d");
                dayAdapter.setItemResource(R.layout.wheel_text_item);
                dayAdapter.setItemTextResource(R.id.text);
                wvDay.setViewAdapter(dayAdapter);
                wvDay.setCurrentItem(0);
            }
        });
    }

    private void adjustInterface() {
        Drawable okDrawable = context.getResources().getDrawable(
                R.drawable.ok_button);
        ResizeUtils.resizeButton(
                btnOk,
                (int) (Config.screenWidth * 0.1),
                (int) (Config.screenWidth * 0.1
                        * okDrawable.getIntrinsicHeight() / okDrawable
                        .getIntrinsicWidth()), 0, 0, 0, 0);

        Drawable cancelDrawable = context.getResources().getDrawable(
                R.drawable.cancel_button);
        ResizeUtils.resizeButton(
                btnCancel,
                (int) (Config.screenWidth * 0.1),
                (int) (Config.screenWidth * 0.1
                        * cancelDrawable.getIntrinsicHeight() / cancelDrawable
                        .getIntrinsicWidth()), 0, 0, 0, 0);

    }

    public int getYear() {
        return wvYear.getCurrentItem() + 2014;
    }

    public int getMonth() {
        return wvMonth.getCurrentItem() + 01;
    }

    public void setDialog(CheckinDialog dialog) {
        this.dialog = dialog;
    }

    public int getDay() {
        return wvDay.getCurrentItem() + 01;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_wheel_cancel:
                dialog.cancelWheel();
                dismiss();
                break;

            case R.id.btn_wheel_ok:
                Calendar calendar = Calendar.getInstance();
                int YEAR = calendar.get(Calendar.YEAR);
                int Month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int y = getYear();
                int m = getMonth();
                int d = getDay();
                if (getYear() < YEAR) {
                    y = YEAR;
                } else {
                    y = getYear();
                    m = getMonth();
                    d = getDay();
                }
                if (getYear() == YEAR) {
                    if (getMonth() < Month) {
                        m = Month;
                        d = day;
                    } else {
                        m = getMonth();
                        d = getDay();
                    }
                    if (getMonth() == Month) {
                        if (getDay() < day) {
                            d = day;
                        } else {
                            d = getDay();
                        }
                    }
                }

                dialog.setStartTimeFromWheel(String.valueOf(d), String.valueOf(m),
                        String.valueOf(y));

                dismiss();
            default:
                break;
        }
    }
}

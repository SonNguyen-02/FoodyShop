package com.example.foodyshop.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import com.example.foodyshop.R;

import org.jetbrains.annotations.Contract;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class DatePickerSpinnerDialog {

    private final Activity activity;
    private AlertDialog dialog;
    private final String dateInit;
    private final IOnDateSetListener mIOnDateSetListener;

    public DatePickerSpinnerDialog(Activity activity, IOnDateSetListener mIOnDateSetListener, String dateInit) {
        this.activity = activity;
        this.mIOnDateSetListener = mIOnDateSetListener;
        this.dateInit = dateInit;
    }

    @NonNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static DatePickerSpinnerDialog newInstance(Activity activity, IOnDateSetListener mIOnDateSetListener, String dateInit) {
        return new DatePickerSpinnerDialog(activity, mIOnDateSetListener, dateInit);
    }

    public interface IOnDateSetListener {
        void onClick(DatePickerSpinnerDialog datePicker, int year, int month, int day);
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_date_picker_spinner, null);
        DatePicker datePicker = view.findViewById(R.id.date_picker);
        datePicker.setMaxDate(System.currentTimeMillis());
        String dateRegex = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
        if (dateInit != null && dateInit.matches(dateRegex)) {
            String[] arr = dateInit.split("-");
            datePicker.updateDate(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]) - 1, Integer.parseInt(arr[2]));
        } else {
            datePicker.updateDate(2000, 0, 1);
        }

        Button btnCancel, btnConfirm;
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnConfirm = view.findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(v -> dismiss());
        btnConfirm.setOnClickListener(v -> mIOnDateSetListener.onClick(this, datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));

        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}

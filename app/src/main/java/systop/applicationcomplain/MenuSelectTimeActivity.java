package systop.applicationcomplain;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuSelectTimeActivity extends AppCompatActivity {
    EditText editText1,editText2;
    private int year;
    private int month;
    private int day;
    private int year1;
    private int month1;
    private int day1;
    private static final int DATE_DIALOG_ID = 1;
    private String currentDate = "";
    private String currentDate1 = "";
    String time ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select_time);
        clearCache.deleteCache(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( getResources().getColor( R.color.title_color ) ) );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(MenuSelectTimeActivity.this);
                dialog1.setTitle("ต้องการย้อนกลับไปหน้าหลักหรือไม่?");
                dialog1.setCancelable(true);
                dialog1.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                dialog1.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog1.show();

            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        Date dNow = new Date( );
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy-MM-dd");

        editText1.setText( ft.format(dNow));
        editText2.setText( ft.format(dNow));
        currentDate = ft.format(dNow);
        currentDate1 = ft.format(dNow);

        if (!isNetworkConnected() && !isWifiConnected() ) {

            new AlertDialog.Builder(this)
                    .setTitle("ไม่มีการเชื่อมต่อข้อมูล")
                    .setMessage("กรุณาตรวจสอบอินเทอร์เน็ต")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), ClearActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();

        }

    }
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการย้อนกลับหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }
    private boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) && networkInfo.isConnected();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
    @TargetApi(Build.VERSION_CODES.N)
    public void select(View view){
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDialog(DATE_DIALOG_ID);

    }
    @TargetApi(Build.VERSION_CODES.N)
    public void select1(View view){
        Calendar c1 = Calendar.getInstance();
        year1 = c1.get(Calendar.YEAR);
        month1 = c1.get(Calendar.MONTH);
        day1 = c1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(MenuSelectTimeActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        editText2.setText( year + "-" + (String.format("%02d", monthOfYear + 1  ))
                                + "-" + String.format("%02d", dayOfMonth  ));
                        currentDate1 = year + "-" + (String.format("%02d", monthOfYear + 1  ))
                                + "-" + String.format("%02d", dayOfMonth  );
                    }
                }, year1, month1, day1);
        dpd.show();
        SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyy-MM-dd");
        try {
            time = currentDate;
            Date date1 = formatter1.parse(time);
            dpd.getDatePicker().setMinDate(date1.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        dpd.getDatePicker().setMaxDate(new Date().getTime());


    }
    private void updateDisplay() {
        currentDate = new StringBuilder().append(year).append("-")
                .append(String.format("%02d", month + 1  )).append("-").append(String.format("%02d", day  )).toString();

        Log.i("DATE", currentDate);

        SimpleDateFormat formatter2 = new SimpleDateFormat ("yyyy-MM-dd");
        try {

            Date date1 = formatter2.parse(currentDate);
            Date date2 = formatter2.parse(currentDate1);

            if(date2.before(date1)){
                editText2.setText(currentDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int j, int k) {
            year = i;
            month = j;
            day = k;
            updateDisplay();
            editText1.setText(currentDate);

        }
    };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dialog = new DatePickerDialog(this, myDateSetListener, year, month,
                        day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dialog;

        }
        return null;
    }
    public void click1(View view){
        Intent intent = new Intent(getApplicationContext(), MenuSelect1TimeActivity.class);
        intent.putExtra("time1", currentDate);
        intent.putExtra("time2", currentDate1);
        startActivity(intent);
        finish();
    }

}

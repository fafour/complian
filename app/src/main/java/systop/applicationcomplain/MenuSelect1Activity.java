package systop.applicationcomplain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuSelect1Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private Spinner spinner1;

    String detailMount="";
    String detailYear="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select1);
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
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(MenuSelect1Activity.this);
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
        spinner = (Spinner) findViewById(R.id.spinner);
        List dataStatus = new ArrayList();
        dataStatus.add("มกราคม");
        dataStatus.add("กุมภาพันธ์");
        dataStatus.add("มีนาคม");
        dataStatus.add("เมษายน");
        dataStatus.add("พฤษภาคม");
        dataStatus.add("มิถุนายน");
        dataStatus.add("กรกฎาคม");
        dataStatus.add("สิงหาคม");
        dataStatus.add("กันยายน");
        dataStatus.add("ตุลาคม");
        dataStatus.add("พฤศจิกายน");
        dataStatus.add("ธันวาคม");

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataStatus);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if(parentView.getItemAtPosition(position).toString().equals("มกราคม")){
                    detailMount = "01";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("กุมภาพันธ์")){
                    detailMount = "02";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("มีนาคม")){
                    detailMount = "03";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("เมษายน")){
                    detailMount = "04";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("พฤษภาคม")){
                    detailMount = "05";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("มิถุนายน")){
                    detailMount = "06";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("กรกฎาคม")){
                    detailMount = "07";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("สิงหาคม")){
                    detailMount = "08";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("กันยายน")){
                    detailMount = "09";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("ตุลาคม")){
                    detailMount = "10";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("พฤศจิกายน")){
                    detailMount = "11";
                }
                else if(parentView.getItemAtPosition(position).toString().equals("ธันวาคม")){
                    detailMount = "12";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        List dataStatus1 = new ArrayList();
        dataStatus1.add("2015");
        dataStatus1.add("2016");
        dataStatus1.add("2017");
        dataStatus1.add("2018");
        dataStatus1.add("2019");
        dataStatus1.add("2020");
        dataStatus1.add("2021");
        dataStatus1.add("2022");
        dataStatus1.add("2023");
        dataStatus1.add("2024");
        dataStatus1.add("2025");
        dataStatus1.add("2026");
        dataStatus1.add("2027");
        dataStatus1.add("2028");
        dataStatus1.add("2029");
        dataStatus1.add("2030");


        ArrayAdapter dataAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataStatus1);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                detailYear = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void click1(View view){
        Intent intent = new Intent(getApplicationContext(), MenuSelectMountYearActivity.class);
        intent.putExtra("mount", detailMount);
        intent.putExtra("year", detailYear);
        startActivity(intent);
        finish();

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
}

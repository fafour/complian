package systop.applicationcomplain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuComplainAdminActivity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVManageEmployee;
    private AdapterEmployee1 mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
//    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_complain_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( getResources().getColor( R.color.title_color ) ) );

        clearCache.deleteCache(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(MenuComplainAdminActivity.this);
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


        new AsyncFetch().execute();

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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncFetch().execute();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, R.color.colorAccent, R.color.colorPrimaryDark);

//        doTheAutoRefresh();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmployee();
            }
        });

    }
    public void addEmployee(){
        String User = getIntent().getStringExtra("User");
        Intent intent = new Intent(getApplicationContext(), InputIdPeople1Activity.class);
        intent.putExtra("User", User);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        new AsyncFetch().execute();
    }
//    private void doTheAutoRefresh() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new AsyncFetch().execute();
//                doTheAutoRefresh();
//            }
//        }, 5000);
//    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                new AsyncFetch().execute();
                break;
            // action with ID action_settings was selected
            case R.id.action_search:
                Intent intent = new Intent(getApplicationContext(), SearchComplainAdminActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการย้อนกลับหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                handler.removeMessages(0);
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
    private class AsyncFetch extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url+"adminShowData.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            List<DataComplain> data=new ArrayList<>();

            data.clear();
            mRVManageEmployee = (RecyclerView)findViewById(R.id.ManageList);
            mAdapter = new AdapterEmployee1(MenuComplainAdminActivity.this, data);
            mRVManageEmployee.setAdapter(mAdapter);
            mRVManageEmployee.setLayoutManager(new LinearLayoutManager(MenuComplainAdminActivity.this));

            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataComplain Data = new DataComplain();
                    Data.Main= json_data.getString("Main");
                    Data.IdCode= json_data.getString("IdCode");
                    Data.Status= json_data.getString("Status");
                    Data.DocterName= json_data.getString("NameDoctor");
                    Data.ResponsiblePerson= json_data.getString("responsiblePerson");

                    Data.IdPeople= json_data.getString("IdPeople");
                    Data.TittleName= json_data.getString("TittleName");
                    Data.Name= json_data.getString("Name");
                    Data.SurName= json_data.getString("SurName");
                    Data.Relationship= json_data.getString("Relationship");
                    Data.Phone= json_data.getString("PhoneNumber");
                    Data.PhoneHome= json_data.getString("PhoneHome");
                    Data.Email= json_data.getString("Email");
                    Data.Adress= json_data.getString("Adress");
                    Data.Day= json_data.getString("Day");
                    Data.AtDay= json_data.getString("AtDay");
                    Data.Detail= json_data.getString("Detai");
                    Data.HospitalName= json_data.getString("HospitalName");
                    Data.RecipientComplaints= json_data.getString("RecipientComplaints");

                    data.add(Data);
                }

                // Setup and Handover data to recyclerview
                mRVManageEmployee = (RecyclerView)findViewById(R.id.ManageList);
                mAdapter = new AdapterEmployee1(MenuComplainAdminActivity.this, data);
                mRVManageEmployee.setAdapter(mAdapter);
                mRVManageEmployee.setLayoutManager(new LinearLayoutManager(MenuComplainAdminActivity.this));
                


            } catch (JSONException e) {

            }

        }

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

}





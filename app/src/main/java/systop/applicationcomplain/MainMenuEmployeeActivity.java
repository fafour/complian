package systop.applicationcomplain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenuEmployeeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtUserName;
    private final Handler handler = new Handler();
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_employee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( getResources().getColor( R.color.title_color ) ) );

        clearCache.deleteCache(this);

        String text = getString(R.string.msg_employee);
        String linkText = getString(R.string.msg_linkable);
        int start = text.indexOf(linkText);
        int end = start + linkText.length();

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new MainMenuEmployeeActivity.CallToast(), start, end, 0);

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText(spannableString);
        textView.setMovementMethod(new LinkMovementMethod());

        String User = getIntent().getStringExtra("User");
        String Pass = getIntent().getStringExtra("Pass");
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText("สวัสดี "+User +"!!!");

        new AsyncCheck().execute();
        doTheAutoCheck();

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            String User = getIntent().getStringExtra("User");
            Intent intent = new Intent(getApplicationContext(), InputIdPeople1Activity.class);
            intent.putExtra("User", User);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            String User = getIntent().getStringExtra("User");
            Intent intent = new Intent(getApplicationContext(), MenuComplainEmployeeActivity.class);
            intent.putExtra("User", User);
            startActivity(intent);
        } else if (id == R.id.nav_c) {
            String User = getIntent().getStringExtra("User");
            String Pass = getIntent().getStringExtra("Pass");
            Intent intent = new Intent(getApplicationContext(), ChangPasswordActivity.class);
            intent.putExtra("User", User);
            intent.putExtra("Pass", Pass);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            handler.removeMessages(0);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class CallToast extends ClickableSpan {
        @Override
        public void onClick(View widget) {
//            Toast.makeText(InputIdPeopleActivity.this, "Test", Toast.LENGTH_SHORT).show();
            //AssetFileDescriptor fd = amanager.openFd(files[0]);
            CopyReadAssets();

        }
    }
    private void CopyReadAssets()
    {
        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getFilesDir(), "pdf_emp.pdf");
        try
        {
            in = assetManager.open("pdf_emp.pdf");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getFilesDir() + "/pdf_emp.pdf"),
                "application/pdf");

        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_home, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_logout:
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(MainMenuEmployeeActivity.this);
                dialog1.setTitle("ต้องการออกหรือไม่?");
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
                handler.removeMessages(0);
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
    public void click1(View v){
        Intent intent = new Intent(getApplicationContext(), SearchDoctorActivity.class);
        startActivity(intent);
    }
    public void click2(View v){
        String User = getIntent().getStringExtra("User");
        Intent intent = new Intent(getApplicationContext(), InputIdPeople1Activity.class);
        intent.putExtra("User", User);
        startActivity(intent);

    }
    public void click3(View v){
        String User = getIntent().getStringExtra("User");
        Intent intent = new Intent(getApplicationContext(), MenuComplainEmployeeActivity.class);
        intent.putExtra("User", User);
        startActivity(intent);
    }
    public void click4(View v){
        String User = getIntent().getStringExtra("User");
        String Pass = getIntent().getStringExtra("Pass");
        Intent intent = new Intent(getApplicationContext(), ChangPasswordActivity.class);
        intent.putExtra("User", User);
        intent.putExtra("Pass", Pass);
        startActivity(intent);

    }
    private void doTheAutoCheck() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new AsyncCheck().execute();
                doTheAutoCheck();
            }
        }, 1000);
    }
    private class AsyncCheck extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        String User = getIntent().getStringExtra("User");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url+"check_now.php");

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

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", User);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

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

            String admin = "[{\"Level\":\"1\",\"Status\":\"1\"}]";
            if (!admin.equals(result)) {
                Intent intent = new Intent(getApplicationContext(), ClearActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                handler.removeMessages(0);
            }

        }

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

}


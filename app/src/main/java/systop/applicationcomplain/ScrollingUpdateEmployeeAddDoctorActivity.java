package systop.applicationcomplain;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrollingUpdateEmployeeAddDoctorActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    EditText txtHospital;
    ListView listview;
    AutoCompleteTextView autoCompleteTextView2;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    List<String> dataString=new ArrayList<>();
    ArrayList<DetailDoctor> arrayList = new ArrayList<>();
    private String selection = "";
    AdapterAddDoctor adapterDoctor;
    private ImageView imageView;
    String image_data ="";
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_update_employee_add_doctor);
        clearCache.deleteCache(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(ScrollingUpdateEmployeeAddDoctorActivity.this);
                dialog1.setTitle("ต้องการย้อนกลับไปหน้ารายการหรือไม่?");
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
        String DocterName = getIntent().getStringExtra("DocterName");
        String HospitalName = getIntent().getStringExtra("HospitalName");
        String Detail = getIntent().getStringExtra("Detail");
        String Day = getIntent().getStringExtra("Day");
        String AtDay = getIntent().getStringExtra("AtDay");
        String IdCode = getIntent().getStringExtra("IdCode");
        String ResponsiblePerson = getIntent().getStringExtra("ResponsiblePerson");
        String Main = getIntent().getStringExtra("Main");
        String Status = getIntent().getStringExtra("Status");
        String RecipientComplaints = getIntent().getStringExtra("RecipientComplaints");

        String[] parts = DocterName.split("\n");
        if(parts.length-1 == 0){
            String a = parts[0];
            String[] b = a.split(":");
            String dataDoctor = b[1];
            arrayList.add(new DetailDoctor(dataDoctor));

        }else{
            for(int i = 0 ; i< parts.length;i++) {
                String a = parts[i];
                String[] b = a.split(":");
                String dataDoctor = b[1];
                arrayList.add(new DetailDoctor(dataDoctor));
            }
        }

        imageView = (ImageView) findViewById(R.id.img);
        String Url = localhost.url+"pic_Get.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            JSONObject jsonResponse = jsonarray.getJSONObject(0);
                            image_data= jsonResponse.getString("image_data");

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "ไม่สามารถทำงานได้ ", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(getApplicationContext(),image_data, Toast.LENGTH_SHORT).show();
                        new DownloadImage().execute(image_data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"ระบบเกิดข้อผิดพลาด กรุณาตรวจสอบการเชื่อมต่อข้อมูลของท่าน", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws com.android.volley.AuthFailureError{
                String txtIdpeople = getIntent().getStringExtra("Idpeople");
                Map<String,String> params = new HashMap<String, String>();
                params.put("image_tag",txtIdpeople);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


        txtHospital = (EditText) findViewById(R.id.txtHospital);
        listview = (ListView) findViewById(R.id.listview);
        autoCompleteTextView2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        txtHospital.setText(HospitalName);
        new AsyncFetch1().execute();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,dataString);

        autoCompleteTextView2.setAdapter(adapter);
        autoCompleteTextView2.setThreshold(1);

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selection = (String)parent.getItemAtPosition(position);

            }
        });

        adapterDoctor = new AdapterAddDoctor(ScrollingUpdateEmployeeAddDoctorActivity.this, arrayList);
        listview.setAdapter(adapterDoctor);
        listview.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next(view);
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

    }
    public void selectData(View view){
        String check = "no";
        if(selection.isEmpty() || selection.equals("")){
            AlertDialog.Builder dialogs = new AlertDialog.Builder(ScrollingUpdateEmployeeAddDoctorActivity.this);
            dialogs.setTitle("คำเตือน");
            dialogs.setMessage("ไม่สามารถเพิ่มรายชื่อผู้ถูกร้องได้ กรุณากรอกข้อมูลด้วย");
            dialogs.setCancelable(true);
            dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogs.show();
        }else {
            if(arrayList.size() != 0) {
                for (int i=0; i < arrayList.size();i++){
                    if (arrayList.get(i).doctorName.equals(selection)){
                        check = "check";
                        break;
                    }
                }
                if(check.equals("no")){
                    arrayList.add(new DetailDoctor(selection));
                    listview.setAdapter(adapterDoctor);
                    selection="";
                    autoCompleteTextView2.setText("");
                    Context context = getApplicationContext();
                    CharSequence text = "เพิ่มข้อมูลเสร็จสิ้น";
                    int duration = Toast.LENGTH_SHORT;
                    selection = "";
                    autoCompleteTextView2.setText("");
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(check.equals("check")){
                    AlertDialog.Builder dialogs = new AlertDialog.Builder(ScrollingUpdateEmployeeAddDoctorActivity.this);
                    dialogs.setTitle("คำเตือน");
                    dialogs.setMessage("ไม่สามารถเพิ่มรายชื่อผู้ถูกร้องได้ ข้อมูลซ้ำกัน");
                    dialogs.setCancelable(true);
                    dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialogs.show();
                }


            }else if(arrayList.size() == 0) {
                arrayList.add(new DetailDoctor(selection));
                listview.setAdapter(adapterDoctor);
                selection="";
                autoCompleteTextView2.setText("");
                Context context = getApplicationContext();
                CharSequence text = "เพิ่มข้อมูลเสร็จสิ้น";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }


    }
    public void next(final View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการทำรายการถัดไปแล้วหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            String txtIdpeople = getIntent().getStringExtra("Idpeople");
            String txtTitleName = getIntent().getStringExtra("TitleName");
            String txtName = getIntent().getStringExtra("Name");
            String txtSurname = getIntent().getStringExtra("Surname");
            String txtRelationship = getIntent().getStringExtra("Relationship");
            String txtPhone = getIntent().getStringExtra("phone");
            String txtEmail = getIntent().getStringExtra("Email");

            String txtHouseNo = getIntent().getStringExtra("HouseNo");
            String txtLane = getIntent().getStringExtra("Lane");
            String txtRoad = getIntent().getStringExtra("Road");
            String txtSubDistrict = getIntent().getStringExtra("SubDistrict");
            String txtDistrict = getIntent().getStringExtra("District");
            String txtProvince = getIntent().getStringExtra("Province");
            String txtPostalCode = getIntent().getStringExtra("PostalCode");
            String txtPhoneHome = getIntent().getStringExtra("PhoneHome");

            String Detail = getIntent().getStringExtra("Detail");
            String Day = getIntent().getStringExtra("Day");
            String AtDay = getIntent().getStringExtra("AtDay");
            String IdCode = getIntent().getStringExtra("IdCode");
            String ResponsiblePerson = getIntent().getStringExtra("ResponsiblePerson");
            String Main = getIntent().getStringExtra("Main");
            String Status = getIntent().getStringExtra("Status");
            String RecipientComplaints = getIntent().getStringExtra("RecipientComplaints");

            public void onClick(DialogInterface dialog, int which) {
                if(arrayList.size() == 0) {
                    if(arrayList.size() == 0) {
                        AlertDialog.Builder dialogs = new AlertDialog.Builder(ScrollingUpdateEmployeeAddDoctorActivity.this);
                        dialogs.setTitle("คำเตือน");
                        dialogs.setMessage("กรุณาเพิ่มรายชื่อผู้ถูกร้อง");
                        dialogs.setCancelable(true);
                        dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogs.show();
                    }

                }else {
                    String dataDoctor = "";
                    for (int i = 0; i < arrayList.size(); i++) {
                        dataDoctor = dataDoctor+(i+1)+":"+arrayList.get(i).doctorName+"\n";
                    }

                    Intent intent = new Intent(getApplicationContext(), ScrollingUpdateEmployeeAddDetailActivity.class);
                    intent.putExtra("Idpeople", txtIdpeople);
                    intent.putExtra("TitleName", txtTitleName);
                    intent.putExtra("Name", txtName);
                    intent.putExtra("Surname", txtSurname);
                    intent.putExtra("Relationship", txtRelationship);
                    intent.putExtra("phone", txtPhone);
                    intent.putExtra("Email", txtEmail);

                    intent.putExtra("HouseNo", txtHouseNo);
                    intent.putExtra("Lane", txtLane);
                    intent.putExtra("Road", txtRoad);
                    intent.putExtra("SubDistrict", txtSubDistrict);
                    intent.putExtra("District", txtDistrict);
                    intent.putExtra("Province", txtProvince);
                    intent.putExtra("PostalCode", txtPostalCode);
                    intent.putExtra("PhoneHome", txtPhoneHome);

                    intent.putExtra("DoctorName", dataDoctor);
                    intent.putExtra("HospitalName", txtHospital.getText().toString());

                    intent.putExtra("Detail",Detail);
                    intent.putExtra("Day",Day);
                    intent.putExtra("AtDay",AtDay);
                    intent.putExtra("IdCode",IdCode);
                    intent.putExtra("ResponsiblePerson",ResponsiblePerson);
                    intent.putExtra("Main",Main);
                    intent.putExtra("Status",Status);
                    intent.putExtra("RecipientComplaints",RecipientComplaints);
                    startActivity(intent);
                    finish();
                }
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
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

    private class AsyncFetch1 extends AsyncTask<String, String, String> {
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
                url = new URL(localhost.url+"addDoctor.php");

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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", "data");
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

            //this method will be running on UI thread

            try {

                JSONArray jArray = new JSONArray(result);

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataSearch Data = new DataSearch();
                    Data.nameDoc = json_data.getString("name");
                    Data.snameDoc = json_data.getString("surname");
                    Data.idcodeDoc = json_data.getString("numbercode");
                    dataString.add(json_data.getString("name")+"  "+json_data.getString("surname")+"  "+json_data.getString("numbercode"));
                }

                // Setup and Handover data to recyclerview

            } catch (JSONException e) {

            }

        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setTitle("เลือกรายการ");
        final CharSequence[] items = { "ลบข้อมูล"};
        final int positionToRemove = position;
        adb.setItems(items, new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                arrayList.remove(positionToRemove);
                listview.setAdapter(adapterDoctor);
            }
        });
        adb.show();
    }
    @Override
    public void onClick(View v) {

    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ScrollingUpdateEmployeeAddDoctorActivity.this,"กำลังโหลดข้อมูล","กรุณารอสักครู่",false,false);
        }


        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL = URL[0];
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            progressDialog.dismiss();
            imageView.setImageBitmap( getRoundedShape(result));

        }
    }
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 2000;
        int targetHeight = 2000;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
}

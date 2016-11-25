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
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ScrollingAddDocter1Activity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    EditText Hospital;
    String txtHospital;
    ListView listview;
    ArrayList<DetailDoctor> arrayList = new ArrayList<>();
    AdapterAddDoctor adapter;
    private ImageView imageView;
    String image_data ="";
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_add_docter1);
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
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(ScrollingAddDocter1Activity.this);
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

        listview = (ListView) findViewById(R.id.listview);
        Hospital = (EditText) findViewById(R.id.txtHospital);
        txtHospital = Hospital.getText().toString();


        adapter = new AdapterAddDoctor(getApplicationContext(), arrayList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

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
    public void  next(final View view){
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

            public void onClick(DialogInterface dialog, int which) {
                txtHospital = Hospital.getText().toString();
                if(arrayList.size() == 0) {
                    if(arrayList.size() == 0) {
                        AlertDialog.Builder dialogs = new AlertDialog.Builder(ScrollingAddDocter1Activity.this);
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
                    txtHospital = Hospital.getText().toString();

                    String dataDoctor = "";
                    for (int i = 0; i < arrayList.size(); i++) {
                        dataDoctor = dataDoctor+(i+1)+":"+arrayList.get(i).doctorName+"\n";
                    }

                    Intent intent = new Intent(getApplicationContext(), ScrollingDetail1Activity.class);
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
                    intent.putExtra("HospitalName", txtHospital);
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

    @Override
    public void onClick(View v) {

    }
    public void selectData(final View view){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View subView = inflater.inflate(R.layout.doctor_detail, null);
        final EditText name1 = (EditText)subView.findViewById(R.id.item_name);
        final EditText sname1 = (EditText)subView.findViewById(R.id.item_sname);
        final EditText id1 = (EditText)subView.findViewById(R.id.item_id);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(subView);
        dialog.create();
        final Button button1 = (Button)subView.findViewById(R.id.button1);
        final Button button2 = (Button)subView.findViewById(R.id.button2);

        final AlertDialog alertDialog = dialog.create();

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String txtname = name1.getText().toString();
                String txtsname = sname1.getText().toString();
                String txtid = id1.getText().toString();


                if(!txtname.isEmpty() && !txtsname.isEmpty()) {
                    String txtData = txtname + "  " + txtsname + "   " + txtid;
                    arrayList.add(new DetailDoctor(txtData));
                    listview.setAdapter(adapter);
                    alertDialog.dismiss();
                }else {
                    if (txtname.isEmpty()) {
                        name1.setError("กรุณากรอกชื่อ");

                    } else {
                        name1.setError(null);
                    }
                    if (txtsname.isEmpty()) {
                        sname1.setError("กรุณากรอกคำนามสกุล");

                    } else {
                        sname1.setError(null);

                    }
                }
            }
        });

        alertDialog.show();


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
                listview.setAdapter(adapter);
            }
        });
        adb.show();
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ScrollingAddDocter1Activity.this,"กำลังโหลดข้อมูล","กรุณารอสักครู่",false,false);
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
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
}

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

public class ScrollingAdressActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    EditText PhoneHome,PostalCode,District,SubDistrict,Road,Lane,HouseNo;
    String txtPhoneHome,txtPostalCode,txtProvince,txtDistrict,txtSubDistrict,txtRoad,txtLane,txtHouseNo;
    private Spinner spinner;
    private ImageView imageView;
    String image_data ="";
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_adress);
        clearCache.deleteCache(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(ScrollingAdressActivity.this);
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
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List dataStatus = new ArrayList();
        dataStatus.add("กรุณาเลือกจังหวัด");
        dataStatus.add("กรุงเทพมหานคร ");
        dataStatus.add("กระบี่");
        dataStatus.add("กาญจนบุรี");
        dataStatus.add("กาฬสินธุ์");
        dataStatus.add("กำแพงเพชร");
        dataStatus.add("ขอนแก่น");
        dataStatus.add("จันทบุรี");
        dataStatus.add("ฉะเชิงเทรา");
        dataStatus.add("ชลบุรี");
        dataStatus.add("ชัยนาท");
        dataStatus.add("ชัยภูมิ");
        dataStatus.add("ชุมพร");
        dataStatus.add("เชียงราย");
        dataStatus.add("เชียงใหม่");
        dataStatus.add("ตรัง");
        dataStatus.add("ตราด");
        dataStatus.add("ตาก");
        dataStatus.add("นครนายก");
        dataStatus.add("นครปฐม");
        dataStatus.add("นครพนม");
        dataStatus.add("นครราชสีมา");
        dataStatus.add("นครศรีธรรมราช");
        dataStatus.add("นครสวรรค์");
        dataStatus.add("นนทบุรี");
        dataStatus.add("นราธิวาส");
        dataStatus.add("น่าน");
        dataStatus.add("บึงกาฬ");
        dataStatus.add("บุรีรัมย์");
        dataStatus.add("ปทุมธานี");
        dataStatus.add("ประจวบคีรีขันธ์");
        dataStatus.add("ปราจีนบุรี");
        dataStatus.add("ปัตตานี");
        dataStatus.add("พระนครศรีอยุธยา");
        dataStatus.add("พังงา");
        dataStatus.add("พัทลุง");
        dataStatus.add("พิจิตร");
        dataStatus.add("พิษณุโลก");
        dataStatus.add("เพชรบุรี");
        dataStatus.add("เพชรบูรณ์");
        dataStatus.add("แพร่");
        dataStatus.add("พะเยา");
        dataStatus.add("ภูเก็ต");
        dataStatus.add("มหาสารคาม");
        dataStatus.add("มุกดาหาร");
        dataStatus.add("แม่ฮ่องสอน");
        dataStatus.add("ยะลา");
        dataStatus.add("ยโสธร");
        dataStatus.add("ร้อยเอ็ด");
        dataStatus.add("ระนอง");
        dataStatus.add("ระยอง");
        dataStatus.add("ราชบุรี");
        dataStatus.add("ลพบุรี");
        dataStatus.add("ลำปาง");
        dataStatus.add("ลำพูน");
        dataStatus.add("เลย");
        dataStatus.add("ศรีสะเกษ");
        dataStatus.add("สกลนคร");
        dataStatus.add("สงขลา");
        dataStatus.add("สตูล");
        dataStatus.add("สมุทรปราการ");
        dataStatus.add("สมุทรสงคราม");
        dataStatus.add("สมุทรสาคร");
        dataStatus.add("สระแก้ว");
        dataStatus.add("สระบุรี");
        dataStatus.add("สิงห์บุรี");
        dataStatus.add("สุโขทัย");
        dataStatus.add("สุพรรณบุรี");
        dataStatus.add("สุราษฎร์ธานี");
        dataStatus.add("สุรินทร์");
        dataStatus.add("หนองคาย");
        dataStatus.add("หนองบัวลำภู");
        dataStatus.add("อ่างทอง");
        dataStatus.add("อุดรธานี");
        dataStatus.add("อุทัยธานี");
        dataStatus.add("อุตรดิตถ์");
        dataStatus.add("อุบลราชธานี");
        dataStatus.add("อำนาจเจริญ");

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataStatus);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

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

        PhoneHome = (EditText) findViewById(R.id.PhoneHome);
        PostalCode = (EditText) findViewById(R.id.PostalCode);
        District = (EditText) findViewById(R.id.District);
        SubDistrict = (EditText) findViewById(R.id.SubDistrict);
        Road = (EditText) findViewById(R.id.Road);
        Lane = (EditText) findViewById(R.id.Lane);
        HouseNo = (EditText) findViewById(R.id.HouseNo);

        txtPhoneHome = PhoneHome.getText().toString();
        txtPostalCode = PostalCode.getText().toString();
        txtDistrict = District.getText().toString();
        txtSubDistrict = SubDistrict.getText().toString();
        txtRoad = Road.getText().toString();
        txtLane = Lane.getText().toString();
        txtHouseNo = HouseNo.getText().toString();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        txtProvince = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
    public void next(){
        String txtIdpeople = getIntent().getStringExtra("Idpeople");
        String txtTitleName = getIntent().getStringExtra("TitleName");
        String txtName = getIntent().getStringExtra("Name");
        String txtSurname = getIntent().getStringExtra("Surname");
        String txtRelationship = getIntent().getStringExtra("Relationship");
        String txtphone = getIntent().getStringExtra("phone");
        String txtEmail = getIntent().getStringExtra("Email");

        txtPhoneHome = PhoneHome.getText().toString();
        txtPostalCode = PostalCode.getText().toString();
        txtDistrict = District.getText().toString();
        txtSubDistrict = SubDistrict.getText().toString();
        txtRoad = Road.getText().toString();
        txtLane = Lane.getText().toString();
        txtHouseNo = HouseNo.getText().toString();

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการทำรายการถัดไปแล้วหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String txtIdpeople = getIntent().getStringExtra("Idpeople");
                String txtTitleName = getIntent().getStringExtra("TitleName");
                String txtName = getIntent().getStringExtra("Name");
                String txtSurname = getIntent().getStringExtra("Surname");
                String txtRelationship = getIntent().getStringExtra("Relationship");
                String txtPhone = getIntent().getStringExtra("phone");
                String txtEmail = getIntent().getStringExtra("Email");

                txtPhoneHome = PhoneHome.getText().toString();
                txtPostalCode = PostalCode.getText().toString();
                txtDistrict = District.getText().toString();
                txtSubDistrict = SubDistrict.getText().toString();
                txtRoad = Road.getText().toString();
                txtLane = Lane.getText().toString();
                txtHouseNo = HouseNo.getText().toString();

                if(!txtProvince.equals("กรุณาเลือกจังหวัด") &&
                   !txtDistrict.isEmpty()  &&
                   !txtSubDistrict.isEmpty()  &&
                   !txtHouseNo.isEmpty()
                        ){

                    Intent intent = new Intent(getApplicationContext(), ScrollingAddDoctorActivity.class);
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

                    startActivity(intent);
                    finish();


                }else{


                    if (txtHouseNo.isEmpty()) {
                        HouseNo.setError("กรุณากรอกบ้านเลขที่");
                    }else {
                        HouseNo.setError(null);
                    }

                    if (txtSubDistrict.isEmpty()) {
                        SubDistrict.setError("กรุณากรอกตำบล");
                    }else {
                        SubDistrict.setError(null);
                    }

                    if (txtDistrict.isEmpty()) {
                        District.setError("กรุณากรอกอำเภอ");
                    }else {
                        District.setError(null);
                    }
                    if (txtProvince.equals("กรุณาเลือกจังหวัด")) {
                        AlertDialog.Builder dialogs = new AlertDialog.Builder(ScrollingAdressActivity.this);
                        dialogs.setTitle("คำเตือน");
                        dialogs.setMessage("กรุณาเลือกจังหวัดด้วย");
                        dialogs.setCancelable(true);
                        dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogs.show();

                    }

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
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ScrollingAdressActivity.this,"กำลังโหลดข้อมูล","กรุณารอสักครู่",false,false);
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

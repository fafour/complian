package systop.applicationcomplain;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UploadImageIdPeopleActivity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 1;
//    private static final int PICK_FROM_GALLERY = 2;
    ImageView imgview;
    private Bitmap bitmap;
    URL filePath;

    private String a = null;

    String ImageTag = "image_tag" ;
    String ImageName = "image_data" ;
    String ServerUploadPath =localhost.url+"pic_upload.php" ;
    ProgressDialog progressDialog ;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String ConvertImage ;
    String GetImageNameFromEditText;
    HttpURLConnection httpURLConnection ;
    OutputStream outputStream;
    BufferedWriter bufferedWriter ;
    int RC ;
    BufferedReader bufferedReader ;
    StringBuilder stringBuilder;
    boolean check = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_id_people);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clearCache.deleteCache(this);
        byteArrayOutputStream = new ByteArrayOutputStream();

        imgview = (ImageView) findViewById(R.id.imageView1);
        Button buttonCamera = (Button) findViewById(R.id.btn_take_camera);
//        Button buttonGallery = (Button) findViewById(R.id.btn_select_gallery);
        buttonCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // call android default camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                try {

                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_CAMERA);

                } catch (ActivityNotFoundException e) {
                }


            }
        });


//        buttonGallery.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                try {
//
//                    intent.putExtra("return-data", true);
//                    startActivityForResult(Intent.createChooser(intent,
//                            "Complete action using"), PICK_FROM_GALLERY);
//
//                } catch (ActivityNotFoundException e) {
//
//                }
//
//            }
//
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            a =  getPath(filePath);
//            Toast.makeText(getApplicationContext(),a,
//                    Toast.LENGTH_SHORT).show();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if (bitmap.getWidth() > bitmap.getHeight()) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap = Bitmap.createBitmap(
                            bitmap,
                            bitmap.getWidth()/2 - bitmap.getHeight()/2,
                            0,
                            bitmap.getHeight(),
                            bitmap.getHeight(),matrix, true
                    );

                }else{

                    bitmap = Bitmap.createBitmap(
                            bitmap,
                            0,
                            bitmap.getHeight()/2 - bitmap.getWidth()/2,
                            bitmap.getWidth(),
                            bitmap.getWidth()
                    );
                }
                imgview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri  filePath = data.getData();
//            a =  getRealPathFromURI(filePath);
//            Toast.makeText(getApplicationContext(),a,
//                    Toast.LENGTH_SHORT).show();
//
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                if (bitmap.getWidth() >= bitmap.getHeight()) {
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(90);
//
//                    bitmap = Bitmap.createBitmap(
//                            bitmap,
//                            bitmap.getWidth()/2 - bitmap.getHeight()/2,
//                            0,
//                            bitmap.getHeight(),
//                            bitmap.getHeight(),matrix, true
//                    );
//
//                }else{
//
//                    bitmap = Bitmap.createBitmap(
//                            bitmap,
//                            0,
//                            bitmap.getHeight()/2 - bitmap.getWidth()/2,
//                            bitmap.getWidth(),
//                            bitmap.getWidth()
//                    );
//                }
//                imgview.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
//    private String getRealPathFromURI(Uri contentURI) {
//        String res = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(contentURI, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//    }

    public void upload(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการอัพโหลดรูปภาพหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String txtIdpeople = getIntent().getStringExtra("Idpeople");
                GetImageNameFromEditText = txtIdpeople;

                UploadImageToServer();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();

    }
    public void UploadImageToServer(){

        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            byteArray = byteArrayOutputStream.toByteArray();

            ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


        }else {
            AlertDialog alertDialog = new AlertDialog.Builder(UploadImageIdPeopleActivity.this).create();
            alertDialog.setTitle("คำเตือน");
            alertDialog.setMessage("กรุณาเพิ่มรูปภาพก่อนจะอัพโหลดรูป");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(UploadImageIdPeopleActivity.this,"กำลังอัพโหลดข้อมูล","กรุณารอสักครู่",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                if(!string1.isEmpty() && !string1.equals("")){
                    String txtIdpeople = getIntent().getStringExtra("Idpeople");
                    Toast.makeText(UploadImageIdPeopleActivity.this,string1,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
                    intent.putExtra("Idpeople", txtIdpeople);
                    startActivity(intent);
                    finish();
                }else {

                }

            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageTag, GetImageNameFromEditText);

                HashMapParams.put(ImageName, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                filePath = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) filePath.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                httpURLConnection.setChunkedStreamingMode(1024);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }

}
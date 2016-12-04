package systop.applicationcomplain;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static java.lang.Float.parseFloat;

public class InputIdPeopleActivity extends AppCompatActivity {
    EditText editText1,editText2,editText3,editText4,editText5;
    TextView txt1,txt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_id_people);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( getResources().getColor( R.color.title_color ) ) );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        clearCache.deleteCache(this);

        String text = getString(R.string.msg_sample);
        String linkText = getString(R.string.msg_linkable);
        int start = text.indexOf(linkText);
        int end = start + linkText.length();

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new CallToast(), start, end, 0);

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText(spannableString);
        textView.setMovementMethod(new LinkMovementMethod());

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);

        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/datafont.ttf");
        txt1.setTypeface(face);
        txt2.setTypeface(face);


        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);

        editText1.setTypeface(face);
        editText2.setTypeface(face);
        editText3.setTypeface(face);
        editText4.setTypeface(face);
        editText5.setTypeface(face);

        editText1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    editText2.requestFocus();
                }
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    editText3.requestFocus();
                }
            }
        });

        editText3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 5) {
                    editText4.requestFocus();
                }
            }
        });
        editText4.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    editText5.requestFocus();
                }
            }
        });


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                next();
//            }
//        });
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
        File file = new File(getFilesDir(), "pdf_people.pdf");
        try
        {
            in = assetManager.open("pdf_people.pdf");
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
                Uri.parse("file://" + getFilesDir() + "/pdf_people.pdf"),
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
    public void Click3(View view){
        next();
    }
    public void  next(){
        int sum1 = 0;

        String id = editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString()
                +editText5.getText().toString();
        int count = editText1.length()+editText2.length()+editText3.length()+editText4.length()+editText5.length();
        if(count == 13 ) {
            for (int i = 0; i < 12; i++) {
                sum1 += parseFloat(String.valueOf(id.charAt(i))) * (13 - i);
            }

            if ((11 - sum1 % 11) % 10 != parseFloat(String.valueOf(id.charAt(12)))){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("คำเตือน");
                dialog.setMessage("เลขประจำตัวประชาชนไม่ถูกต้อง กรุณาตรวจสอบเลขประจำตัวประชาชนใหม่อีกครั้ง");
                dialog.setCancelable(true);
                dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }else {
                Intent intent = new Intent(getApplicationContext(), CheckComplainActivity.class);
                intent.putExtra("Idpeople", id);
                startActivity(intent);
                finish();

            }
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("คำเตือน");
            dialog.setMessage("กรุณากรอกเลขประจำตัวประชาชนให้ถูกต้อง");
            dialog.setCancelable(true);
            dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();


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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

}

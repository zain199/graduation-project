package com.appz.qrcode.helperUi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appz.qrcode.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.AppCompatActivity;

public class QrActivity extends AppCompatActivity {

    //ui
    Button genebtn;
    Button savebtn;
    EditText edtTxt;
    ImageView imgqr;

    //var
    String inputvale;
    String tag = "GenrateQrCode";
    BitmapDrawable bitmapDrawable;
    Bitmap img;
    FileOutputStream outputStream;
    QRGEncoder qrgEncoder ;
    boolean ok ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findview();

        generateQRCode();

        savebtn();

    }

    private void findview()
    {
        ok=false;
        imgqr = (ImageView) findViewById(R.id.imgview);
        savebtn = findViewById(R.id.btnsave);
    }

    private void generateQRCode()
    {
        Intent intent = getIntent();
        inputvale = intent.getStringExtra("idCard") ;
        if (inputvale.length()>0)
        {

            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int w = point.x;
            int h = point.y;
            int smallerone = w <h ? w : h ;
            qrgEncoder = new QRGEncoder(inputvale,null, QRGContents.Type.TEXT,smallerone);

            try {
                img = qrgEncoder.encodeAsBitmap();
                imgqr.setImageBitmap(img);
                ok=true;
            } catch (Exception e) {
                Log.v(tag,e.toString());
                ok=false;
            }


        }else
        {
            edtTxt.setError("Required");
            ok=false;
        }
    }

    private void savebtn()
    {
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ok)
                {
                    bitmapDrawable = (BitmapDrawable)imgqr.getDrawable();
                    img = bitmapDrawable.getBitmap();

                    File filepath = Environment.getExternalStorageDirectory();
                    File dir = new File(filepath.getAbsolutePath()+"/Ration Service/");
                    dir.mkdir();

                    File imdge = new File(dir,System.currentTimeMillis()+".jpg");
                    try {
                        outputStream = new FileOutputStream(imdge);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    img.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    Toast.makeText(getBaseContext(),"QR Saved",Toast.LENGTH_LONG).show();

                    try {
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else
                {
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    edtTxt.setError("Required");
                    ok=false;
                }


            }
        });
    }


}

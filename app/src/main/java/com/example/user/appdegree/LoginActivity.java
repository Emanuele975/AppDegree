package com.example.user.appdegree;

import android.Manifest;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class LoginActivity extends AppCompatActivity {
    Button btnInvio;
    ImageView imageView;
    String pathToFile;
    Bitmap photo;
    EditText password;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnInvio=findViewById(R.id.button3);
        final Bundle richiesta = getIntent().getExtras();

        if (Build.VERSION.SDK_INT >=23)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2 );
        }

        btnInvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchPictureTakerAction();
                String url = "http://localhost:8080/prova";
                StringRequest sq =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    protected Map< String,String > getParams(){

                        Map<String,String> parr=new HashMap<String, String>();
                        parr.put("password",password.getText().toString());
                        parr.put("richiesta", richiesta.toString());
                        // inserire anche il parametro della foto

                        return parr;



                    }
                };
                AppController.getInstance().addToRequestQueue(sq);
                Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                //String psw= password.getText().toString();
                // metodo che mi ritorna il file della foto
            }
        });

        imageView = (ImageView)findViewById(R.id.imageView);

    }

    private void dispatchPictureTakerAction()
    {
        if(checkPermission()) {
            Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePic.resolveActivity(getPackageManager()) != null)
            {
                File photoFile = null;
                try {
                    photoFile = createPhotoFile();
                    if (photoFile != null) {
                        pathToFile = photoFile.getAbsolutePath();
                        Uri photoURI = FileProvider.getUriForFile(LoginActivity.this, "com.example.user.appdegree", photoFile);
                        takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePic,1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            //startActivityForResult(openCamera, 0);
            //openLogin.putExtra("richiesta", true);
        }
        else {
            requestPermission();
        }
    }

    private File createPhotoFile() throws IOException {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try
        {
            image = File.createTempFile(name, ".jpg", storageDir);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return image;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            //System.out.print("nel metodo");
        if (resultCode == RESULT_OK ) {
            if(requestCode == 1 && data!=null) {
                photo = BitmapFactory.decodeFile(pathToFile);
                imageView.setImageBitmap(photo);
            }
            else
                System.out.println("data =null");
        }
        else
            System.out.println("request code errato");

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
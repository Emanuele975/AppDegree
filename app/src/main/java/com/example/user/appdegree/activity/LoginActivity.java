package com.example.user.appdegree.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.solver.widgets.Helper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;

import android.util.Log;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.appdegree.R;
import com.example.user.appdegree.activity.utility.MyKeyboard;
import com.example.user.appdegree.activity.utility.VolleyMultipartRequest;
import com.example.user.appdegree.activity.utility.VolleySingleton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class LoginActivity extends AppCompatActivity {
    int control = 0;
    Button btnInvio;
    String pathToFile;
    Bitmap photo;
    EditText editcity;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int CAMERA_REQUEST = 1888;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnInvio=findViewById(R.id.Button_invio);
        editcity = (EditText) findViewById(R.id.EditText);
        MyKeyboard keyBoard= findViewById(R.id.keyboard);
        editcity.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editcity.setTextIsSelectable(true);
        InputConnection ic = editcity.onCreateInputConnection(new EditorInfo());
        keyBoard.setInputConnection(ic);
        editcity.setShowSoftInputOnFocus(false);
        if (Build.VERSION.SDK_INT >=23)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2 );
        }
        final String richiesta = getIntent().getExtras().getString("richiesta");
        btnInvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(richiesta.equals("true")) {
                    dispatchPictureTakerAction();
                    control = 1;
                }
                else
                {
                    sendInfoUscita();
                }
                //control=0;
            }
        });
        //imageView = (ImageView)findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (control!=0) {
            this.sendInfoEntrata();
        }
        control = 0;
    }

    private void sendInfoUscita() {
        String url = "http://10.0.2.2:8080/api/activity/exit";
        //final String richiesta = getIntent().getExtras().getString("richiesta");
        final String password = editcity.getText().toString();
        //System.out.println("password: "+password);
        StringRequest stringRequest = new StringRequest(Request.Method.POST ,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                //System.out.println(response);
                if(response.equals("-2")) {
                    Toast.makeText(getApplicationContext(), "password errata" , Toast.LENGTH_LONG).show();
                    //System.out.println("psw errata");
                }
                else if(response.equals("-1"))
                    Toast.makeText(getApplicationContext(), "errore generico" , Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(getApplicationContext(), "uscita effettuata correttamente" , Toast.LENGTH_LONG).show();
                    Intent openMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(openMain);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error: " + error.toString(), Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password",password);
                params.put("type","EXIT");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    private void sendInfoEntrata() {
        String url = "http://10.0.2.2:8080/api/activity/entry";
        final String password = editcity.getText().toString();
        final byte[] data = convertBitmapToByteArray(photo);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url , new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                String result = new String(response.data);
                System.out.println(result);
                if(result.equals("-2")) {
                    Toast.makeText(getApplicationContext(), "password errata" , Toast.LENGTH_LONG).show();
                    //System.out.println("psw errata");
                }
                else if(result.equals("-1"))
                    Toast.makeText(getApplicationContext(), "errore generico" , Toast.LENGTH_LONG).show();
                else if (result.equals("1")) {
                    Intent openMain = new Intent(LoginActivity.this, MainActivity.class);
                    Toast.makeText(getApplicationContext(), "entrata effettuata correttamente" , Toast.LENGTH_LONG).show();
                    startActivity(openMain);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //errorExtractionForSaveProfileAccount(error);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("type","ENTRY");
                System.out.println(Arrays.asList(params));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    //InputStream iStream = getContentResolver().openInputStream(Uri.parse(image_path));
                    //byte[] inputData = getBytes(iStream);
                    params.put("file", new DataPart("", data, "image/jpeg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        //System.out.println("richiesta andata a buon fine");
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
        //System.out.println("tutto a posto");



        /*
        HttpClient httpClient = HttpClients.createDefault();
        PostMethod postMethod = new PostMethod(url);
        postMethod.addParameter("Email", email);
        postMethod.addParameter("fname", fname);
        try {
            httpClient.executeMethod(postMethod);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        URL url = null;
        try {
            url = new URL("http://10.0.2.2:8080/api/send_info_entry");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //header
            conn.setRequestProperty("Content-Type","multipart/form-data;" );
            //DataOutputStream request = new DataOutputStream(
              //      conn.getOutputStream());
            //request.write(data);
            //conn.

        } catch (Exception e) {
            e.printStackTrace();
        }

        //method




        //final String image = encodeTobase64(photo);
        StringRequest stringRequest = new StringRequest(Request.Method.POST ,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                System.out.println(response);
                if(response.equals("-2")) {
                    Toast.makeText(getApplicationContext(), "password errata" , Toast.LENGTH_LONG).show();
                    //System.out.println("psw errata");
                }
                else if(response.equals("-1"))
                    Toast.makeText(getApplicationContext(), "errore generico" , Toast.LENGTH_LONG).show();
                else {
                    Intent openMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(openMain);
                }
            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error: " + error.toString(), Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password",password);
                return params;
            }

        };
        */



        //RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        //requestQueue.add(stringRequest);


    }

    private void dispatchPictureTakerAction() {
        if(checkPermission()) {
            Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePic.resolveActivity(getPackageManager()) != null)
            {
                File photoFile = null;
                try {
                    photoFile = createPhotoFile();// ci potrebbero essere degli errori negli input
                    if (photoFile != null) {
                        pathToFile = photoFile.getAbsolutePath();
                        Uri photoURI = FileProvider.getUriForFile(LoginActivity.this, "com.example.user.appdegree",photoFile);
                        takePic.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
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
            //System.out.print("AAAAAAAAAAAAAAAAAAAAAAAAAAA");
        if (resultCode == RESULT_OK ) {
            //System.out.print("AAAAAAAAAAAAAAAAAAAAAAAAAAA     ");
            if(requestCode == 1 && data!=null) {
                //System.out.print("BBBBBBBBBBBBBBBBBBBBBBBBB    ");
                photo = BitmapFactory.decodeFile(pathToFile);
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

    public byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            return stream.toByteArray();
        }finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(Helper.class.getSimpleName(), "ByteArrayOutputStream was not closed");
                }
            }
        }
    }

}
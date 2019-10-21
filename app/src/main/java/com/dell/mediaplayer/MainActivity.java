package com.dell.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Media> songs;
    EditText link;
    RecyclerView recycler;
    RecyclerViewAdapter adapter;
    final int PERMISSION_CODE=1;
    SQLiteHelper sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }

        link = findViewById(R.id.url);


        songs = new ArrayList<>();
        sql=new SQLiteHelper(this);

         /*
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
        songs.add(new Media("Something right","Katy Perry"));
          */

        //setting up recycler view with adapter
        recycler = findViewById(R.id.recycler);
        adapter = new RecyclerViewAdapter(this, songs);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        //adding line divider between item views
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(getResources().getDrawable(R.drawable.divider));
        recycler.addItemDecoration(itemDecor);
        //add clicking event to download

        link.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Search();
                }
                return false;
            }
        });


    }
    public void sendData() {
        Cursor dataHolder = sql.readALL();
        if (dataHolder.getCount() == 0) {
            return;
        }
        try {
            while (dataHolder.moveToNext()) {
                //all paths will be stored in this variable in loop
                //you should bring it to the recycler to be shown and played
                String path = dataHolder.getString(1);


            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            dataHolder.close();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_CODE&&grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_DENIED){
            Toast.makeText(this,"we'll not be able to display or download data",Toast.LENGTH_LONG).show();
        }
    }

    public void Search() {
        if (!link.getText().toString().isEmpty() && link.getText().toString().contains("http")) {
            new Download(MainActivity.this, link.getText().toString()).execute();
        }else{
            Toast.makeText(this,"Something wrong with URL",Toast.LENGTH_LONG).show();
        }
    }

    public class Download extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        Context context;
        String url;
        String mp3;

        Download(Context context, String url) {
            this.context = context;

            this.url = url;

        }


        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait \n Download...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL(this.url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                String[] path = url.getPath().split("/");
                mp3 = path[path.length - 1];
                int lengthOfFile = c.getContentLength();

                String folder_main = "MediaPlayer";
                File f = new File(Environment.getExternalStorageDirectory(), folder_main);
                if (!f.exists()) {
                    f.mkdirs();
                }
                String PATH = Environment.getExternalStoragePublicDirectory(folder_main).toString();

                Log.v("", "PATH: " + PATH);
                File file = new File(PATH);
                boolean mkdirs = file.mkdirs();
                Log.v("", "mkdirs: " + mkdirs);

                String fileName = mp3;

                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {

                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "done";
        }


        protected void onPostExecute(String result) {
            if (result.equals("done")) {
                progressDialog.dismiss();

                songs.add(new Media(mp3,"Katy Perry"));
                recycler.setAdapter(adapter);
                //add paths here
                //sql.insert(mp3);
                Toast.makeText(MainActivity.this,"Download finished",Toast.LENGTH_LONG).show();
            }
        }

    }
}


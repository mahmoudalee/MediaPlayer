package com.dell.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.widget.GridLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    List<Media> mSongs;
    EditText link;
    ImageButton search;
    RecyclerView recycler;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        link = findViewById(R.id.editText);
        search = findViewById(R.id.search);


        mSongs = new ArrayList<>();

         /*
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
        mSongs.add(new Media("Something right","Katy Perry"));
          */

        //setting up recycler view with adapter
        recycler = findViewById(R.id.recycler);
        adapter = new RecyclerViewAdapter(this, mSongs);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        //adding line divider between item views
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(getResources().getDrawable(R.drawable.divider));
        recycler.addItemDecoration(itemDecor);

    }

    public void Search(View view) {
        if (!link.getText().toString().isEmpty() && link.getText().toString().contains("http")) {
            new Download(MainActivity.this, link.getText().toString()).execute();
        }else{
            Toast.makeText(this,"Some thing wrong with URL",Toast.LENGTH_LONG).show();
        }
    }

    public class Download extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;

        Context context;
        String url;
        String mp3;

        Download(Context context, String url) {
            this.context = context;

            this.url = url;

        }


        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Please wait \n Download...");
            mProgressDialog.show();
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
                mProgressDialog.dismiss();
                mSongs.add(new Media(mp3,"Katy Perry"));
                recycler.setAdapter(adapter);

                Toast.makeText(MainActivity.this,"Download finished",Toast.LENGTH_LONG).show();
            }
        }

    }
}


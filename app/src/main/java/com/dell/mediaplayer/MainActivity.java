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

import com.dell.mediaplayer.db.Song;
import com.dell.mediaplayer.db.SongDao;
import com.dell.mediaplayer.db.SongDatabase;
import com.dell.mediaplayer.util.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    List<Song> songs;
    EditText link;
    RecyclerView recycler;
    RecyclerViewAdapter adapter;
    final int PERMISSION_CODE=1;


    private SongDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SongDatabase database = SongDatabase.getDatabase(this);
        dao = database.getDaoInstance();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }

        link = findViewById(R.id.url);


//        songs = new ArrayList<>();

         /*
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
        songs.add(new song("Something right","Katy Perry"));
          */


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

        // set up the RecyclerView After Add the Data
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        //setting up recycler view with adapter
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        //adding line divider between item views
//        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        itemDecor.setDrawable(getResources().getDrawable(R.drawable.divider));
//        recycler.addItemDecoration(itemDecor);
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
        String PATH;

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
                PATH = Environment.getExternalStoragePublicDirectory(folder_main).toString();

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


//                songs.add(new Song(PATH,DateUtils.getCurrentDateTime(),mp3,"Katy Perry"));

                // Add the song to database
                saveSong(PATH , mp3 ,"Katy Perry" );

                // to refresh the recycler view
                new FetchData().execute();

//                recycler.setAdapter(adapter);
                //add paths here
                //sql.insert(mp3);
                Toast.makeText(MainActivity.this,"Download finished",Toast.LENGTH_LONG).show();
            }
        }

    }


    private void saveSong(String link, String tile, String artist) {

        // still no optimized
        Song song = new Song(link, DateUtils.getCurrentDateTime(),tile,artist);
        new InsertSong().execute(song);
    }


    class InsertSong extends AsyncTask<Song,Void,Void>{
        @Override
        protected Void doInBackground(Song... songs) {
            dao.addSong(songs[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Done...", Toast.LENGTH_SHORT).show();
        }
    }


    // onStart the app in Second Times Fetch data to show it
    @Override
    protected void onStart() {
        super.onStart();
        new FetchData().execute();
    }


    class FetchData extends AsyncTask<Void,Void, List<Song>>{

        @Override
        protected List<Song> doInBackground(Void... voids) {

            // do the query in dao
            return dao.viewAllSongs();

        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);

            // refresh the recycler view
            adapter.setData(songs);
        }
    }
}


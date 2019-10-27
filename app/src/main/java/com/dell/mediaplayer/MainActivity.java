package com.dell.mediaplayer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int PERMISSION_CODE = 1;
    EditText url;
    RecyclerViewAdapter adapter;
    private SongDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SongDatabase database = SongDatabase.getDatabase(this);
        dao = database.getDaoInstance();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }

        url = findViewById(R.id.url);


        //add clicking event to download
        url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Search();
                }
                return false;
            }
        });

        // set up the RecyclerView After Adding the Data
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        //setting up recycler view with adapter
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        //adding line divider between item views
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(itemDecor);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "we'll not be able to display or download data", Toast.LENGTH_LONG).show();
        }
    }

    public void Search() {
        if (!url.getText().toString().isEmpty() && url.getText().toString().contains("http")) {
            new Download(MainActivity.this, url.getText().toString()).execute();
        } else {
            Toast.makeText(this, "Something is wrong with URL", Toast.LENGTH_LONG).show();
        }
    }

    private void saveSong(String path, String title, String artist, byte[] image, String url, String duration) {

        // still not optimized
        Song song = new Song(path, DateUtils.getCurrentDateTime(), title, artist, image, url, duration);
        new InsertSong().execute(song);
    }

    // onStart the app in Second Times Fetch data to show it
    @Override
    protected void onStart() {
        super.onStart();
        new FetchData().execute();
    }

    public class Download extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        Context context;
        String url;
        String defaultFileName;
        String folderPath;
        String fileName;

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
                //build connection
                URL url = new URL(this.url);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                //
                defaultFileName = "Untitled";
                int lengthOfFile = c.getContentLength();
                String folder_main = "MediaPlayer";
                File folder = new File(Environment.getExternalStorageDirectory(), folder_main);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                folderPath = Environment.getExternalStoragePublicDirectory(folder_main).toString() + "/";
                Log.v("file state", "folderPath: " + folderPath);
                File fileWithPath = new File(folderPath);
                boolean mkdirs = fileWithPath.mkdirs();
                Log.v("file state", "mkdirs: " + mkdirs);

                fileName = defaultFileName + " 0.mp3";
                int i;
                while (new File(fileWithPath, fileName).exists()) {
                    String[] split = fileName.split(".mp3");
                    String[] split2 = split[0].split(" ");
                    i = Integer.parseInt(split2[split2.length - 1]) + 1;
                    fileName = defaultFileName + " " + i + ".mp3";
                }

                Log.i("file state", folderPath + fileName + "");
                File outputFile = new File(fileWithPath, fileName);
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                InputStream inputStream = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1;
                while ((len1 = inputStream.read(buffer)) != -1) {

                    outputStream.write(buffer, 0, len1);
                }
                outputStream.close();
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "done";
        }


        protected void onPostExecute(String result) {
            if (result.equals("done")) {
                progressDialog.dismiss();

                //extract data from file
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(folderPath + fileName);
                String name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                //you can extract date here (you don't have to create separate class for that)
                String date = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
                byte[] imageBytes = mmr.getEmbeddedPicture();
                //convert duration from milliseconds to readable format
                long minutes = Long.parseLong(duration) / (60 * 1000);
                long seconds = (Long.parseLong(duration) / 1000) % 60;
                duration = String.format("%02d:%02d", minutes, seconds);

                File from = new File(folderPath, fileName);
                if (!(name == null)) {
                    fileName = fileName.replace(defaultFileName, name);
                    from.renameTo(new File(folderPath, fileName));
                }

                // Add the song to database
                saveSong(folderPath + fileName, fileName, artist, imageBytes, url, duration);

                // to refresh the recycler view
                new FetchData().execute();


                Toast.makeText(MainActivity.this, "Download finished", Toast.LENGTH_LONG).show();
            }
        }

    }

    class InsertSong extends AsyncTask<Song, Void, Void> {
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

    class FetchData extends AsyncTask<Void, Void, List<Song>> {

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


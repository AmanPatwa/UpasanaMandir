package patwa.aman.com.upasanamandir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import patwa.aman.com.upasanamandir.Gallery_RecyclerView.onItemClickedListener;

public class ImageZoom extends AppCompatActivity  {

    ImageView imageView;
    Toolbar toolbar;
    int position;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        toolbar=findViewById(R.id.include);
        toolbar.setTitle("Upasana Mandir");
        setSupportActionBar(toolbar);

        imageView=(ImageView)findViewById(R.id.image_zoom);
        Intent intent=getIntent();
        imageUrl=intent.getStringExtra("imageUrl");
        position=intent.getIntExtra("position",1);
        Picasso.with(this).load(imageUrl)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .fit()
            .centerCrop()
            .into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ImageZoom.this).load(imageUrl)
                            .fit()
                            .centerCrop()
                            .into(imageView);
                }
            });
    }

    /*public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select Action");
        //MenuItem click=contextMenu.add(Menu.NONE,1,1,"See Image");
        MenuItem download=contextMenu.add(Menu.NONE,1,1,"Download");
        MenuItem delete=contextMenu.add(Menu.NONE,2,2,"Delete");

        //click.setOnMenuItemClickListener(this);
        download.setOnMenuItemClickListener(this);
        delete.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(position!=RecyclerView.NO_POSITION) {
            switch (menuItem.getItemId()) {
                case 1:
                    onDownloadClicked(position);
                    return true;
                case 2:
                    onDeleteClicked(position);
                    return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClicked(int position) {

        Toast.makeText(this,"clicked"+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClicked(int position) {

    }

    @Override
    public void onDownloadClicked(int position) {

        DownloadTask downloadTask=new DownloadTask();
        downloadTask.execute(imageUrl);

       /* Bitmap bitmap= BitmapFactory.decodeResource(getResources(),);

        File path= Environment.getExternalStorageDirectory();

        File dir=new File(path+"/UpasanaMandir/");
        dir.mkdirs();

        File file=new File(dir,galleryModel.getName());

        OutputStream outputStream=null;

        try {
            outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (java.io.IOException e) {
            e.printStackTrace();
    }

    @Override
    public void onClick(View view) {

    }


    class DownloadTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(MainActivity.context);
            progressDialog.setTitle("Download in progress...");
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            String path1=strings[0];
            int file_length=0;
            try {
                URL url=new URL(path1);
                URLConnection urlConnection=url.openConnection();
                urlConnection.connect();
                file_length=urlConnection.getContentLength();

                File path= Environment.getExternalStorageDirectory();
                File new_folder=new File(path+"/UpasanaMandir/");
                if(!new_folder.exists()) {
                    new_folder.mkdir();
                }
                File input_file=new File(new_folder,System.currentTimeMillis()+".jpg");
                InputStream inputStream=new BufferedInputStream(url.openStream(),8192);
                byte[] data=new byte[1024];
                int total=0;
                int count=0;
                OutputStream outputStream=new FileOutputStream(input_file);
                while ((count=inputStream.read(data))!=-1) {
                    total+=count;
                    outputStream.write(data,0,count);
                    int progress=(int)total*100/file_length;
                    publishProgress(progress);
                }

                inputStream.close();
                outputStream.close();

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            return "Download completed";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            progressDialog.hide();
            Toast.makeText(MainActivity.context,aVoid,Toast.LENGTH_SHORT).show();
        }
    }*/


}

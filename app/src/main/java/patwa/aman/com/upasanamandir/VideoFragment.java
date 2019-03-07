package patwa.aman.com.upasanamandir;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 25-12-2018.
 */

public class VideoFragment extends android.support.v4.app.Fragment implements Video_ViewHolder.onItemClickedListener{

    private RecyclerView recyclerView;
    private Button admin;
    private List<VideoModel> modelList;
    private RecyclerViewVideo mAdapter;
    private DatabaseReference mdatabaseReference;
    private String value;
    private FirebaseRecyclerOptions<VideoModel> options;
    private FirebaseRecyclerAdapter<VideoModel,Video_ViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_videos,container,false);

        Bundle bundle=getArguments();
        if(bundle!=null)
            value=bundle.getString("key");


        admin=(Button)view.findViewById(R.id.btn_video_admin);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview_video);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mdatabaseReference= FirebaseDatabase.getInstance().getReference("videos");
        mdatabaseReference.keepSynced(true);

        options=new FirebaseRecyclerOptions.Builder<VideoModel>()
                .setQuery(mdatabaseReference,VideoModel.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<VideoModel, Video_ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final Video_ViewHolder holder, int position, VideoModel model) {
                model.setKey(options.getSnapshots().getSnapshot(position).getKey());
                holder.name.setText(model.getName());
                holder.videoView.setVideoURI(Uri.parse(model.getVideoUrl()));
                holder.videoView.start();
            }

            @Override
            public Video_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(MainActivity.context).inflate(R.layout.video_card,parent,false);
                Video_ViewHolder videoHolder=new Video_ViewHolder(view,VideoFragment.this);
                return videoHolder;
            }
        };

        /*modelList=new ArrayList<>();
        mAdapter=new RecyclerViewVideo(this,modelList);
        recyclerView.setAdapter(mAdapter);

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    VideoModel model=snapshot.getValue(VideoModel.class);
                    model.setKey(snapshot.getKey());
                    modelList.add(model);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });*/

        adapter.startListening();
        recyclerView.setAdapter(adapter);



        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),GalleryUpload.class);
                intent.putExtra("key","video");
                startActivity(intent);
            }
        });

        return view;
    }

    /*@Override
    public void onitemClick(int position) {
        VideoModel model=adapter.getItem(position);
        String video=model.getVideoUrl();
        Log.v("videoUrl",video);
        Intent intent=new Intent(getActivity(),VideoZoomActivity.class);
        intent.putExtra("videoUrl",video);
        startActivity(intent);
    }*/

    @Override
    public void onItemClicked(int position) {

        VideoModel model=adapter.getItem(position);
        String video=model.getVideoUrl();
        Log.v("videoUrl",video);
        Intent intent=new Intent(getActivity(),VideoZoomActivity.class);
        intent.putExtra("videoUrl",video);
        startActivity(intent);

    }

    @Override
    public void onDeleteClicked(int position) {

        VideoModel model=adapter.getItem(position);
        final String selectedKey=model.getKey();

        StorageReference imageRef= FirebaseStorage.getInstance().getReferenceFromUrl(model.getVideoUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mdatabaseReference.child(selectedKey).removeValue();
                Toast.makeText(getActivity(), "Video Deleted Successfully", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDownloadClicked(int position) {

        VideoModel model=adapter.getItem(position);
        String video=model.getVideoUrl();

        DownloadTask downloadTask=new DownloadTask();
        downloadTask.execute(video);

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
                InputStream inputStream=new BufferedInputStream(url.openStream(),262144);
                byte[] data=new byte[262144];
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
    }
}

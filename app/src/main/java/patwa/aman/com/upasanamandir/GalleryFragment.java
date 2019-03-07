package patwa.aman.com.upasanamandir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import java.util.List;

import static patwa.aman.com.upasanamandir.Gallery_ViewHolder.*;

/**
 * Created by dell on 22-12-2018.
 */

public class GalleryFragment extends Fragment implements onItemClickedListener {

    private RecyclerView rv;
    private Button upload;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference mdatabaseReference;
    Gallery_ViewHolder viewHolder;
    private List<GalleryModel> modelList;
    private String value;
    private ProgressBar mprogressBar;
    FirebaseRecyclerOptions<GalleryModel> options;
    FirebaseRecyclerAdapter<GalleryModel,Gallery_ViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_gallery,container,false);

        Bundle bundle=getArguments();
        if(bundle!=null)
            value=bundle.getString("key");

        mprogressBar=(ProgressBar)view.findViewById(R.id.progress_circle);
        upload=(Button)view.findViewById(R.id.gallery_upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),GalleryUpload.class);
                intent.putExtra("key","image");
                startActivity(intent);
            }
        });
        rv=(RecyclerView)view.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),3));


        mdatabaseReference= FirebaseDatabase.getInstance().getReference("uploads");
        mdatabaseReference.keepSynced(true);

        firebaseStorage=FirebaseStorage.getInstance();
        /*modelList=new ArrayList<>();
        mAdapter = new Gallery_RecyclerView(this,modelList);
        rv.setAdapter(mAdapter);
        mprogressBar.setVisibility(View.INVISIBLE);/*

        firebaseStorage=FirebaseStorage.getInstance();
        //mstorageReference= FirebaseStorage.getInstance().getReference("uploads");
        //mstorageReference.getFile(Uri.parse("https://firebasestorage.googleapis.com/v0/b/upasanamandir-33cad.appspot.com/o/uploads%2FIMG_20170630_235757.jpg?alt=media&token=648c6f9d-7a80-4932-997d-18e7012112d6"));


        mdatabaseReference= FirebaseDatabase.getInstance().getReference("uploads");
        /*mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                modelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    GalleryModel galleryModel=snapshot.getValue(GalleryModel.class);
                    galleryModel.setKey(snapshot.getKey());
                    modelList.add(galleryModel);
                }
                mAdapter.notifyDataSetChanged();
                mprogressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                mprogressBar.setVisibility(View.INVISIBLE);
            }
        });*/


        //Firebase Recycler

        //viewHolder.setOnItemClickListener(this);
        options=new FirebaseRecyclerOptions.Builder<GalleryModel>()
                .setQuery(mdatabaseReference,GalleryModel.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<GalleryModel, Gallery_ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(final Gallery_ViewHolder holder, int position, final GalleryModel model) {
                model.setKey(options.getSnapshots().getSnapshot(position).getKey());
                System.out.println("key:"+ options.getSnapshots().getSnapshot(position).getKey());
                holder.name.setText(model.getName());
                Picasso.with(getActivity()).load(model.getImageUrl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .fit()
                        .centerCrop()
                        .into(holder.image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity()).load(model.getImageUrl())
                                        .fit()
                                        .centerCrop()
                                        .into(holder.image);
                            }
                        });
            }

            @Override
            public Gallery_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_card,parent,false);
                Gallery_ViewHolder galleryViewHolder=new Gallery_ViewHolder(view,GalleryFragment.this);
                return galleryViewHolder;

            }
        };

        adapter.startListening();
        rv.setAdapter(adapter);
        //viewHolder.mvalueEventListener=this;

        return view;
    }


    @Override
    public void onItemClicked(int position) {
        GalleryModel galleryModel=adapter.getItem(position);
        String item=galleryModel.getImageUrl();

        Intent intent=new Intent(getActivity(),ImageZoom.class);
        intent.putExtra("position",position);
        intent.putExtra("imageUrl",item);
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(int position) {

        GalleryModel galleryModel=adapter.getItem(position);
        final String selectedKey=galleryModel.getKey();

        StorageReference imageRef=firebaseStorage.getReferenceFromUrl(galleryModel.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mdatabaseReference.child(selectedKey).removeValue();
                Toast.makeText(getActivity(), "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDownloadClicked(int position) {

        GalleryModel galleryModel=adapter.getItem(position);
        String image=galleryModel.getImageUrl();

        DownloadTask downloadTask=new DownloadTask();
        downloadTask.execute(image);

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
            e.printStackTrace();*/
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

                File path=Environment.getExternalStorageDirectory();
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
    }
}

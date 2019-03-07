package patwa.aman.com.upasanamandir;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class VideoUploadActivity extends AppCompatActivity {

    private Button upload, chooseFile;
    private TextView showUpload;
    private EditText fileName;
    private VideoView mvideoView;
    private ProgressBar checkProgress;
    private static final int PICK_VIDEO_REQUEST = 1;
    private Uri mvideoUri;
    private StorageReference mstorageReference;
    private DatabaseReference mdatabaseReference;
    private int stopPosition;
    StorageTask mstorageTask;
    private MediaController mediaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);

        upload = (Button) findViewById(R.id.btn_video_upload);
        chooseFile = (Button) findViewById(R.id.choose_video);
        showUpload = (TextView) findViewById(R.id.txt_show_video);
        fileName = (EditText) findViewById(R.id.edt_video_name);
        mvideoView = (VideoView) findViewById(R.id.video_view_upload);
        checkProgress = (ProgressBar) findViewById(R.id.video_progress);
        mediaController = new MediaController(this);
        mstorageReference= FirebaseStorage.getInstance().getReference("videos");
        mdatabaseReference= FirebaseDatabase.getInstance().getReference("videos");

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mstorageTask!=null && mstorageTask.isInProgress())
                    Toast.makeText(VideoUploadActivity.this,"Upload in progress....",Toast.LENGTH_SHORT).show();
                else
                    uploadVideo();
            }
        });


        mvideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvideoView.setMediaController(mediaController);
                mediaController.setAnchorView(mvideoView);
            }
        });

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_VIDEO_REQUEST);
            }
        });


        showUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VideoUploadActivity.this,MainActivity.class);
                intent.putExtra("key","video");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mvideoUri = data.getData();
            //Picasso.with(this).load(mvideoUri).centerCrop().fit().into((Target) mvideoView);
            mvideoView.setVideoURI(mvideoUri);
            mvideoView.start();

        }

    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void uploadVideo(){
        StorageReference storageReference=mstorageReference.child(System.currentTimeMillis()+"."+getFileExtension(mvideoUri));

        File video_file=new File(mvideoUri.getPath());
        //Bitmap thumb_video= ThumbnailUtils.createVideoThumbnail(mvideoUri.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

        //System.out.println("Bitmap:"+thumb_video);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //thumb_video.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] videoData = baos.toByteArray();

        final StorageReference video_thumbnail=mstorageReference.child("video_thumb").child(System.currentTimeMillis()+"."+getFileExtension(mvideoUri));

        mstorageTask=storageReference.putFile(mvideoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler= new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkProgress.setProgress(0);
                            }
                        },200);



                        final VideoModel model=new VideoModel(fileName.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());

                        //For video Thumbnail


                        UploadTask uploadTask=video_thumbnail.putBytes(videoData);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                String thumbUrl=task.getResult().getDownloadUrl().toString();


                                String keyName=mdatabaseReference.push().getKey();

                                mdatabaseReference.child(keyName).setValue(model);
                                mdatabaseReference.child(keyName).child("video_thumb").setValue(thumbUrl);
                                Toast.makeText(VideoUploadActivity.this,"Upload Successfull",Toast.LENGTH_SHORT).show();
                            }
                        });

                        //videothumbnail over




                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        checkProgress.setProgress((int) progress);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VideoUploadActivity.this,"Failed to upload",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

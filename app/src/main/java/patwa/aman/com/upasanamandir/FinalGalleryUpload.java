package patwa.aman.com.upasanamandir;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class FinalGalleryUpload extends AppCompatActivity {

    private Button upload,chooseFile;
    private TextView showUpload;
    private EditText fileName;
    private ImageView mimageView;
    private ProgressBar checkProgress;
    private static final int PICK_IMAGE_REQUEST=1;
    private Uri mimageUri;
    private StorageReference mstorageReference;
    private DatabaseReference mdatabaseReference;
    private String d=null;
    private Toolbar toolbar;
    private StorageTask muploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_gallery_upload);

        upload=(Button)findViewById(R.id.btn_upload);
        chooseFile=(Button)findViewById(R.id.btn_choose_image);
        showUpload=(TextView)findViewById(R.id.txt_show_upload);
        fileName=(EditText)findViewById(R.id.edt_file_name);
        mimageView=(ImageView)findViewById(R.id.image_view_upload);
        checkProgress=(ProgressBar)findViewById(R.id.progressBar);

        mstorageReference= FirebaseStorage.getInstance().getReference("uploads");
        mdatabaseReference= FirebaseDatabase.getInstance().getReference("uploads");

        mdatabaseReference.keepSynced(true);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(muploadTask!=null && muploadTask.isInProgress())
                {
                    Toast.makeText(FinalGalleryUpload.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadFile();
                }
            }
        });

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        showUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FinalGalleryUpload.this,MainActivity.class);
                intent.putExtra("key","value");
                startActivity(intent);
            }
        });
    }

    private void openFileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!=null && data.getData()!=null)
        {
            mimageUri= data.getData();
            Picasso.with(this).load(mimageUri).into(mimageView);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile()
    {
        if(mimageUri!=null)
        {
            StorageReference reference=mstorageReference.child(System.currentTimeMillis()+"."+getFileExtension(mimageUri));
            muploadTask=reference.putFile(mimageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler= new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    checkProgress.setProgress(0);
                                }
                            },200);

                            Toast.makeText(FinalGalleryUpload.this,"Upload Successfull",Toast.LENGTH_SHORT).show();
                            GalleryModel model=new GalleryModel(fileName.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());

                            String uploadName=mdatabaseReference.push().getKey();
                            mdatabaseReference.child(uploadName).setValue(model);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FinalGalleryUpload.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            checkProgress.setProgress((int) progress);
                        }
                    });
        }
        else
        {
            Toast.makeText(this,"No File Selected",Toast.LENGTH_SHORT).show();
        }
    }
}

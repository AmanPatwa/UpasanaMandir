package patwa.aman.com.upasanamandir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference mdatabaseReference;
    private CircleImageView userImage;
    private FirebaseUser mCurrentuser;
    private DatabaseReference muserDatabase;
    private StorageReference muserImage;
    private static final int PICK_IMAGE_REQUEST=1;
    private TextView inputUser,useremail;
    private Button changeImage;
    private Bitmap thumb_img;
    private String uid,userName,imageUrl,thumbnailUrl,email;
    private ProgressDialog imageProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userImage=(CircleImageView)findViewById(R.id.settings_circle_imgview);
        inputUser=(TextView)findViewById(R.id.settings_userName);
        changeImage=(Button)findViewById(R.id.settings_change_img_btn);
        mCurrentuser= FirebaseAuth.getInstance().getCurrentUser();
        uid=mCurrentuser.getUid();
        useremail=(TextView)findViewById(R.id.settings_email);
        muserDatabase= FirebaseDatabase.getInstance().getReference("users").child(uid);
        muserImage= FirebaseStorage.getInstance().getReference("profile_image");
        imageProgressDialog=new ProgressDialog(this);


        muserDatabase.keepSynced(true);
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent imageIntent=new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imageIntent,PICK_IMAGE_REQUEST);*/

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });


        muserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userName=dataSnapshot.child("username").getValue().toString();
                imageUrl=dataSnapshot.child("image").getValue().toString();
                thumbnailUrl=dataSnapshot.child("thumb_image").getValue().toString();
                email=dataSnapshot.child("email").getValue().toString();

                UserModel model=new UserModel(userName,email,imageUrl,thumbnailUrl);
                model.setEmail(email);
                model.setImageThumbnail(thumbnailUrl);
                model.setUsername(userName);
                model.setImageUrl(imageUrl);

                inputUser.setText(userName);
                useremail.setText(email);
                if(!imageUrl.equals("default")) {
                    Picasso.with(SettingsActivity.this)
                            .load(imageUrl)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.dummyimage)
                            .into(userImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(SettingsActivity.this)
                                            .load(imageUrl)
                                            .placeholder(R.drawable.dummyimage)
                                            .into(userImage);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SettingsActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageProgressDialog.setTitle("Uploading Image..");
                imageProgressDialog.setMessage("Please wait...");
                imageProgressDialog.setCanceledOnTouchOutside(false);
                imageProgressDialog.show();

                final Uri resultUri = result.getUri();
                File thumb_filePath=new File(resultUri.getPath());

                StorageReference storeimage=muserImage.child(uid+".jpg");

                try {
                    thumb_img = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(60)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference thumbImage=muserImage.child("thumbnail").child(uid+".jpg");


                storeimage.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumbImage.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumbnail_url = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()) {
                                        Map update_hashmap = new HashMap<>();
                                        update_hashmap.put("image", downloadUrl);
                                        update_hashmap.put("thumb_image", thumbnail_url);

                                        muserDatabase.updateChildren(update_hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    imageProgressDialog.dismiss();
                                                    Picasso.with(SettingsActivity.this)
                                                            .load(imageUrl)
                                                            .placeholder(R.drawable.dummyimage)
                                                            .into(userImage);
                                                    Toast.makeText(SettingsActivity.this, "Upload Successfull", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        imageProgressDialog.dismiss();
                                    }
                                }


                            });
                        }

                        else
                        {
                            imageProgressDialog.hide();
                            Toast.makeText(SettingsActivity.this,"Error in uploading,Please try again later",Toast.LENGTH_LONG).show();
                        }

                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(12);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}

package patwa.aman.com.upasanamandir;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class GalleryUpload extends AppCompatActivity {

    private EditText adminName,adminPass;
    private String Up="UpasanaMandir";
    private ImageView msaheb;
    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_upload);

        adminName=(EditText)findViewById(R.id.edt_admin_name);
        adminPass=(EditText)findViewById(R.id.edt_admin_pass);
        msaheb=(ImageView)findViewById(R.id.guruji);
        toolbar=findViewById(R.id.nav_actionbar);
        toolbar.setTitle("Upasana Mandir");
        mAuth=FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);


        final String email="patwaaman25@gmail.com";
        final String pass="UpasanaMandir";
      /* mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("UpasanaMandir", "createUser onComplete: " + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.d("UpasanaMandir", "User Creation Failed");
                            Toast.makeText(GalleryUpload.this, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    }

                });*/




        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        msaheb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.equals(adminName.getText().toString()) && pass.equals(adminPass.getText().toString()))
                {
                    /*mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(GalleryUpload.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("UpasanaMandir","signInWithEmail() onComplete:"+task.isSuccessful());

                            if(!task.isSuccessful()){
                                Log.d("UpasanaMandir","Problem signing in:" +task.getException());
                                Toast.makeText(GalleryUpload.this,"Invalid User",Toast.LENGTH_SHORT).show();

                            }*/

                                Intent i=getIntent();
                                String value=i.getStringExtra("key");

                                if(value.equals("video")){
                                    Intent intent=new Intent(GalleryUpload.this,VideoUploadActivity.class);
                                    startActivity(intent);
                                }
                                else if(value.equals("image")){

                                    Intent intent = new Intent(GalleryUpload.this, FinalGalleryUpload.class);
                                    startActivity(intent);
                                }
                            }

                else
                {
                    Toast.makeText(GalleryUpload.this,"You are not an Admin",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}

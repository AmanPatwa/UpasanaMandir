package patwa.aman.com.upasanamandir;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userData;
    private TextInputLayout memail,mpass,muserName;
    private Button register;
    private String email,password,userName;
    private Toolbar toolbar;
    private ProgressDialog mprogressDialog;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar=(Toolbar) findViewById(R.id.register_action_bar);
        toolbar.setTitle("Upasana Mandir");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        memail=(TextInputLayout)findViewById(R.id.register_email);
        mpass=(TextInputLayout)findViewById(R.id.register_password);
        muserName=(TextInputLayout)findViewById(R.id.register_display_name);
        register=(Button)findViewById(R.id.register_reg_btn);

        mprogressDialog=new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mprogressDialog.setTitle("Registering User");
                mprogressDialog.setMessage("Please wait!!");
                mprogressDialog.setCanceledOnTouchOutside(false);
                mprogressDialog.show();
                registeruser(memail,mpass,muserName);
            }
        });

    }

    private void registeruser(TextInputLayout memail, TextInputLayout mpass, TextInputLayout muserName) {

        memail.setError(null);
        mpass.setError(null);
        muserName.setError(null);

        email=memail.getEditText().getText().toString();
        password=mpass.getEditText().getText().toString();
        userName=muserName.getEditText().getText().toString();

        boolean cancel=false;
        View focusView=null;

        if(TextUtils.isEmpty(userName))
        {
            muserName.setError("Invalid Username");
            focusView=muserName;
            cancel=true;
        }

        else if(TextUtils.isEmpty(email) || !(email.contains("@") && email.contains(".com")))
        {
            memail.setError("Invalid Email address");
            focusView=memail;
            cancel=true;
        }

        else if(TextUtils.isEmpty(password) || password.length() < 6)
        {
            mpass.setError("Invalid password, Length should be greater than 6");
            focusView=mpass;
            cancel=true;
        }

        if(cancel){
            mprogressDialog.hide();
            focusView.requestFocus();
        }
        else{
            createFirebaseUser(email,password,userName);
        }
    }

    private void createFirebaseUser(final String email, final String password, final String userName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser currentUser=mAuth.getCurrentUser();
                            String uid=currentUser.getUid();

                            userData=FirebaseDatabase.getInstance().getReference("users").child(uid);
                            /*userData.child(uid).child("Email").setValue(email);
                            userData.child(uid).child("Password").setValue(password);
                            userData.child(uid).child("Username").setValue(userName);*/

                            HashMap<String ,String> userMap=new HashMap<>();
                            userMap.put("email",email);
                            userMap.put("password",password);
                            userMap.put("username",userName);
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");

                            userData.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Log.d("upasana mandir", "createUserWithEmail:success");
                                        mprogressDialog.dismiss();
                                        Intent chatIntent=new Intent(RegisterActivity.this,MainActivity.class);
                                        chatIntent.putExtra("key","chat");
                                        startActivity(chatIntent);
                                        finish();
                                    }
                                }
                            });
                        }

                        else {
                            // If sign in fails, display a message to the user.
                            mprogressDialog.hide();
                            Log.w("Upasana Mandir", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed,Email Address already in use",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}

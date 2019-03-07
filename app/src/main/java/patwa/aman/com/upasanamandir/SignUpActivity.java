package patwa.aman.com.upasanamandir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private Button mregister,mlogin;
    private TextInputLayout memail,mpass;
    private Toolbar toolbar;
    private String email,password;
    private FirebaseAuth mAuth;
    private ProgressDialog mprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar=findViewById(R.id.chat_action_bar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mregister=(Button)findViewById(R.id.signup_reg_btn);
        mlogin=(Button)findViewById(R.id.signUp_login_btn);
        memail=(TextInputLayout)findViewById(R.id.signUp_email);
        mpass=(TextInputLayout)findViewById(R.id.signUp_password);
        mAuth = FirebaseAuth.getInstance();
        mprogressDialog=new ProgressDialog(this);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mprogressDialog.setTitle("Loging User");
                mprogressDialog.setMessage("Please wait!!");
                mprogressDialog.setCanceledOnTouchOutside(false);
                mprogressDialog.show();
                signInUser(memail,mpass);
            }
        });


        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent=new Intent(SignUpActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

    }

    private void signInUser(TextInputLayout memail, TextInputLayout mpass) {
        memail.setError(null);
        mpass.setError(null);

        email=memail.getEditText().getText().toString();
        password=mpass.getEditText().getText().toString();

        boolean cancel=false;
        View focusView=null;

        if(TextUtils.isEmpty(email) || !(email.contains("@") && email.contains(".com")))
        {
            memail.setError("Enter valid Email address");
            focusView=memail;
            cancel=true;
        }

        else if(TextUtils.isEmpty(password))
        {
            mpass.setError("Enter valid password");
            focusView=mpass;
            cancel=true;
        }

        if(cancel){
            mprogressDialog.hide();
            focusView.requestFocus();
        }
        else{
            signInFirebaseUser(email,password);
        }

    }

    private void signInFirebaseUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("UpasanaMandir", "signInWithEmail:success");

                            mprogressDialog.dismiss();
                            Intent chatIntent=new Intent(SignUpActivity.this,MainActivity.class);
                            chatIntent.putExtra("key","chat");
                            startActivity(chatIntent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            mprogressDialog.hide();
                            Log.w("UpasanaMandir", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}

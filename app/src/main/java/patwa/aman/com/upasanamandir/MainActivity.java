package patwa.aman.com.upasanamandir;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static Context context;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private android.support.v7.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mUserDatabase;
    private CircleImageView headerImage;
    private TextView userName;
    private UserModel model;
    private View headerView;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser=FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabase= FirebaseDatabase.getInstance().getReference("users");
        mUserDatabase.keepSynced(true);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.CAPTURE_VIDEO_OUTPUT},2);
        //ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET},1);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.nav_actionbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView=navigationView.getHeaderView(0);
        userName=(TextView)headerView.findViewById(R.id.header_userName);
        headerImage=(CircleImageView)headerView.findViewById(R.id.header_circleImageView);

        if(currentUser==null){
            userName.setText(null);}
        else {
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(currentUser!=null) {
                        userUid = currentUser.getUid();
                        String displayUser = dataSnapshot.child(userUid).child("username").getValue().toString();
                        final String displaythumbimg=dataSnapshot.child(userUid).child("thumb_image").getValue().toString();
                        userName.setText(displayUser);
                        Picasso.with(MainActivity.this).load(displaythumbimg).networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.dummyimage).into(headerImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(MainActivity.this).load(displaythumbimg).placeholder(R.drawable.dummyimage).into(headerImage);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }


        mAuth=FirebaseAuth.getInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        Intent intent=getIntent();
        String value=intent.getStringExtra("key");
        String tp="value";
        if(Objects.equals(value, tp)) {
            Bundle bundle = new Bundle();
            bundle.putString("key", value);
            GalleryFragment galleryFragment = new GalleryFragment();
            galleryFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, galleryFragment).addToBackStack(null).commit();
        }
        else if(Objects.equals(value, "video"))
        {
            Bundle bundle = new Bundle();
            bundle.putString("key", value);
            VideoFragment videoFragment=new VideoFragment();
            videoFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,videoFragment).addToBackStack(null).commit();
        }
        else if(Objects.equals(value,"chat"))
        {
            Bundle bundle=new Bundle();
            bundle.putString("key",value);
            ChatFragment chatFragment=new ChatFragment();
            chatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,chatFragment).addToBackStack(null).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.chat_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        currentUser = mAuth.getCurrentUser();
        if (toggle.onOptionsItemSelected(item))
            return true;

        if(item.getItemId()==R.id.chat_log_out_btn)
        {


            System.out.println("Current User:"+currentUser);

            if(currentUser==null)
            {
                Toast.makeText(MainActivity.this,"Please Log in first",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
            else {

                FirebaseAuth.getInstance().signOut();
                return true;
            }
        }
        if(item.getItemId()==R.id.settings_btn)
        {
            if(currentUser==null)
            {
                Toast.makeText(MainActivity.this,"Plaese Log in first",Toast.LENGTH_LONG).show();
            }
            else {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertdialoge = new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you really want to exit!!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false);
        AlertDialog alert = alertdialoge.create();
        alert.show();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit();
                break;
            case R.id.nav_course:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new CourseFragment()).commit();
                break;
            case R.id.nav_religiousActivities:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ReligiousActivityFragment()).commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ChatFragment()).commit();
                break;
            case R.id.nav_contactUs:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ContactFragment()).commit();
                break;
            case R.id.nav_locateUs:
                String uri=String.format(Locale.ENGLISH,"geo:0,0?q=ARIHANT BUNGLOW UPASANA MANDIR");
                Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
                startActivity(i);
                break;
            case R.id.nav_donateUs:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new DonateFragment()).commit();
                break;
            case R.id.nav_gallery:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new GalleryFragment()).commit();
                break;
            case R.id.nav_videos:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new VideoFragment()).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}

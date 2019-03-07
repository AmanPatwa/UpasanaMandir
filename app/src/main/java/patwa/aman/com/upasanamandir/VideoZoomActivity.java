package patwa.aman.com.upasanamandir;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoZoomActivity extends AppCompatActivity {

    VideoView videoView;
    Toolbar toolbar;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_zoom);

        toolbar=findViewById(R.id.include);
        toolbar.setTitle("Upasana Mandir");
        setSupportActionBar(toolbar);

        videoView=(VideoView)findViewById(R.id.video_zoom);
        mediaController=new MediaController(this);
        mediaController.show(500);

        Intent intent=getIntent();
        final String videoUrl=intent.getStringExtra("videoUrl");

        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.start();

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.setMediaController(mediaController);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mediaController.setAnchorView(videoView);
                    }
                },2000);

            }
        });
        //Log.v("videoUrl",videoUrl);

        //videoView.requestFocus();



    }
}

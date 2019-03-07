package patwa.aman.com.upasanamandir;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

/**
 * Created by dell on 25-12-2018.
 */

public class RecyclerViewVideo extends RecyclerView.Adapter<RecyclerViewVideo.VideoHolder> {

    List<VideoModel> videoModels;
    onItemClickListener listener;


    public RecyclerViewVideo(onItemClickListener videoFragment, List<VideoModel> modelList) {
        listener=videoFragment;
        videoModels=modelList;

    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(MainActivity.context).inflate(R.layout.video_card,parent,false);
        VideoHolder videoHolder=new VideoHolder(view);
        return videoHolder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        VideoModel model=videoModels.get(position);
        holder.name.setText(model.getName());
        holder.videoView.setVideoURI(Uri.parse(model.getVideoUrl()));
        holder.videoView.start();
    }


    @Override
    public int getItemCount() {
        return videoModels.size();
    }


    public interface onItemClickListener
    {
        void onitemClick(int position);
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        VideoView videoView;
        TextView name;

        public VideoHolder(View itemView) {
            super(itemView);

            videoView=(VideoView)itemView.findViewById(R.id.video_view);
            name=(TextView)itemView.findViewById(R.id.text_video_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION)
            {
                listener.onitemClick(position);
            }
        }
    }
}

package patwa.aman.com.upasanamandir;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by dell on 14-01-2019.
 */

public class Video_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,MenuItem.OnMenuItemClickListener,View.OnCreateContextMenuListener {
    VideoView videoView;
    TextView name;
    onItemClickedListener mvalueEventListener;

    public Video_ViewHolder(View itemView,onItemClickedListener onItemClickListener) {
        super(itemView);

        videoView=(VideoView)itemView.findViewById(R.id.video_view);
        name=(TextView)itemView.findViewById(R.id.text_video_name);
        mvalueEventListener=onItemClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {
        int position=getAdapterPosition();
        if(position!=RecyclerView.NO_POSITION)
        {
            mvalueEventListener.onItemClicked(position);
        }
    }



    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int position=getAdapterPosition();
        if(position!=RecyclerView.NO_POSITION)
        {
            System.out.println("In onMenuItemClick");
            switch (menuItem.getItemId())
            {
                case 1:mvalueEventListener.onItemClicked(position);
                    return true;
                case 2:mvalueEventListener.onDownloadClicked(position);
                    return true;
                case 3:mvalueEventListener.onDeleteClicked(position);
                    return true;
            }
        }
        return false;
    }


    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        System.out.println("In onCreateContextMenu");
        if(view.isLongClickable()) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem click = contextMenu.add(Menu.NONE, 1, 1, "See Video");
            MenuItem download = contextMenu.add(Menu.NONE, 2, 2, "Download");
            MenuItem delete = contextMenu.add(Menu.NONE, 3, 3, "Delete");

            click.setOnMenuItemClickListener(this);
            download.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

    }

    public interface onItemClickedListener{
        void onItemClicked(int position);
        void onDeleteClicked(int position);
        void onDownloadClicked(int position);
    }



}

package patwa.aman.com.upasanamandir;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Created by dell on 14-01-2019.
 */

public class Gallery_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener {

    TextView name;
    ImageView image;
    onItemClickedListener mvalueEventListener;


    public Gallery_ViewHolder(View itemView,onItemClickedListener listener) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.image_name);
        image = (ImageView) itemView.findViewById(R.id.image_view_retreive);
        mvalueEventListener=listener;
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


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("Select Action");
        MenuItem click=contextMenu.add(Menu.NONE,1,1,"See Image");
        MenuItem download=contextMenu.add(Menu.NONE,2,2,"Download");
        MenuItem delete=contextMenu.add(Menu.NONE,3,3,"Delete");

        click.setOnMenuItemClickListener(this);
        download.setOnMenuItemClickListener(this);
        delete.setOnMenuItemClickListener(this);

    }

    public interface onItemClickedListener{
        void onItemClicked(int position);
        void onDeleteClicked(int position);
        void onDownloadClicked(int position);
    }


}

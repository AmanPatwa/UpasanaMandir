package patwa.aman.com.upasanamandir;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dell on 22-12-2018.
 */

public class Gallery_RecyclerView extends RecyclerView.Adapter<Gallery_RecyclerView.GalleryViewHolder> {

    Context context=MainActivity.context;
    onItemClickedListener mvalueEventListener;
    private List<GalleryModel> galleryModels;


    public Gallery_RecyclerView(onItemClickedListener onItemClickedListener, List<GalleryModel> galleryModels) {
        this.galleryModels = galleryModels;
        mvalueEventListener= onItemClickedListener;
    }

    @Override
    public Gallery_RecyclerView.GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.gallery_card,parent,false);
        GalleryViewHolder galleryViewHolder=new GalleryViewHolder(view);
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(Gallery_RecyclerView.GalleryViewHolder holder, int position) {
        //holder.name.setText(galleryModels.get(position).getName());
        //holder.image.setImageResource(Integer.parseInt(galleryModels.get(position).getImageUrl()));
        GalleryModel galleryModel=galleryModels.get(position);
        holder.name.setText(galleryModel.getName());
        Picasso.with(context).load(galleryModel.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return galleryModels.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener {
        TextView name;
        ImageView image;
        public GalleryViewHolder(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.image_name);
            image=(ImageView)itemView.findViewById(R.id.image_view_retreive);
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
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem click=contextMenu.add(Menu.NONE,1,1,"See Image");
            MenuItem download=contextMenu.add(Menu.NONE,2,2,"Download");
            MenuItem delete=contextMenu.add(Menu.NONE,3,3,"Delete");

            click.setOnMenuItemClickListener(this);
            download.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
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
    }

    public interface onItemClickedListener{
        void onItemClicked(int position);
        void onDeleteClicked(int position);
        void onDownloadClicked(int position);
    }

}

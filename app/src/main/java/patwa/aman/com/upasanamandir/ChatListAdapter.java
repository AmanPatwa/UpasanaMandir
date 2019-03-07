package patwa.aman.com.upasanamandir;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by dell on 29-01-2019.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>{

    private final String[] items;
    private ChatFragment listener;

    public ChatListAdapter(ChatFragment chatFragment, String[] items) {
        this.items = items;
        listener = chatFragment;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View chat_view=LayoutInflater.from(MainActivity.context).inflate(R.layout.chat_list_item,parent,false);
        return new ChatViewHolder(chat_view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.chat_list.setText(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView chat_list;
        public ChatViewHolder(View itemView) {
            super(itemView);
            chat_list=(TextView)itemView.findViewById(R.id.list_items);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition=getAdapterPosition();
            listener.onItemClick(adapterPosition);
        }
    }

    public interface OnChatViewClick
    {
        public void onItemClick(int position);
    }
}

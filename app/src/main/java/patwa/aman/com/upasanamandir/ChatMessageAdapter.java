package patwa.aman.com.upasanamandir;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dell on 06-02-2019.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChattingViewHolder>{

    private List<ChatModel> mMessage;
    private String currentUser;
    String currentUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference user=FirebaseDatabase.getInstance().getReference("users").child(currentUid);


    public ChatMessageAdapter(List<ChatModel> mMessage) {
        this.mMessage = mMessage;
    }


    @Override
    public ChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout,parent,false);
        ChattingViewHolder chattingViewHolder=new ChattingViewHolder(view);
        chattingViewHolder.params=(LinearLayout.LayoutParams)chattingViewHolder.message.getLayoutParams();
        chattingViewHolder.params=(LinearLayout.LayoutParams)chattingViewHolder.userName.getLayoutParams();
        //chattingViewHolder.params=(LinearLayout.LayoutParams)chattingViewHolder.image.getLayoutParams();
        return chattingViewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ChattingViewHolder holder, int position) {

        user.keepSynced(true);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser= dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Toast.makeText(ChatActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
        ChatModel c=mMessage.get(position);

        System.out.println("ChatMessageAdapter:"+c.getDisplay_name());
        System.out.println("ChatMessageAdapter:"+currentUser);

       boolean isItMe=c.getUid().equals(currentUid);
       setChatRowAppearance(isItMe,holder);

        holder.userName.setText(c.getDisplay_name());
        holder.message.setText(c.getMess());

    }

    private void setChatRowAppearance(boolean isItMe, ChattingViewHolder holder) {

        if(isItMe)
        {

            holder.params.gravity= Gravity.START;
            holder.message.setTextColor(Color.BLUE);
        }
        else
        {
            holder.params.gravity=Gravity.START;
            holder.message.setTextColor(Color.BLACK);
        }
        holder.message.setLayoutParams(holder.params);
        holder.userName.setLayoutParams(holder.params);
       // holder.image.setLayoutParams(holder.params);
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class ChattingViewHolder extends RecyclerView.ViewHolder {
        TextView userName,message;
        //CircleImageView image;
        private LinearLayout.LayoutParams params;

        public ChattingViewHolder(View itemView) {
            super(itemView);

            userName=(TextView) itemView.findViewById(R.id.message_userName);
            message=(TextView)itemView.findViewById(R.id.message_userInput);
           // image=(CircleImageView)itemView.findViewById(R.id.message_pic);
        }
    }
}

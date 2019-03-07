package patwa.aman.com.upasanamandir;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.firebase.auth.FirebaseAuth.*;

/**
 * Created by dell on 05-12-2018.
 */

public class ChatFragment extends Fragment implements ChatListAdapter.OnChatViewClick{

    private FirebaseAuth mAuth;
    private String value;
    private FirebaseUser currentUser;
    private RecyclerView chat_item;
    public static String[] values={"Upasana Mandir"};


    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        String disp=currentUser.getDisplayName();
        System.out.println("Current User:"+currentUser);
        System.out.println("current user disp"+disp);

        if(currentUser==null)
        {
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent signupIntent = new Intent(getActivity(), SignUpActivity.class);
        startActivity(signupIntent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        View view= inflater.inflate(R.layout.fragment_chat,container,false);
        Bundle bundle=getArguments();
        if(bundle!=null)
            value=bundle.getString("key");
        if(currentUser==null)
            sendToStart();


        ChatListAdapter adaptor=new ChatListAdapter(this,values);
        chat_item=(RecyclerView)view.findViewById(R.id.chat_recyclerView);
        chat_item.setHasFixedSize(true);
        chat_item.setLayoutManager(new LinearLayoutManager(getActivity()));
        chat_item.setAdapter(adaptor);

        return view;
    }


    @Override
    public void onItemClick(int position) {
        Intent chatIntent=new Intent(getActivity(),ChatActivity.class);
        chatIntent.putExtra("Current_User",currentUser.getDisplayName());
        startActivity(chatIntent);
    }
}
/*
<android.support.v7.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cardCornerRadius="15dp"
        android:padding="15dp"
        android:layout_margin="5dp">
 */
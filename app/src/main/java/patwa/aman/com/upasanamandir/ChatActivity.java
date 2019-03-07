package patwa.aman.com.upasanamandir;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String currentUid,dispCurrentUser;
    private RecyclerView chatRecycler;
    private ImageButton send,add;
    private EditText message;
    private DatabaseReference chat,user;
    private SwipeRefreshLayout mRefreshLayout;
    private UserModel model;
    private List<ChatModel> messageList=new ArrayList<>();
    private ChatMessageAdapter messageAdapter;

    private static final int TOTAL_ITEMS_TO_LOAD=15;
    private int mCurrentPage=1;
    String m;

    //Notification

    NotificationCompat.Builder notification;
    private static final int uniqueId=55555;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        toolbar=(Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("Upasana Mandir");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        chatRecycler=(RecyclerView)findViewById(R.id.chat_list);
        mRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.chat_message_swipe_layout);
        send=(ImageButton)findViewById(R.id.chat_send);
        add=(ImageButton)findViewById(R.id.chat_plus);
        message=(EditText)findViewById(R.id.chat_input);
        chat=FirebaseDatabase.getInstance().getReference("messages");
        user=FirebaseDatabase.getInstance().getReference("users").child(currentUid);
        chat.keepSynced(true);
        user.keepSynced(true);

        //Notification

        notification=new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);


       /* // text to speech

        mTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS){
                    int result=mTTS.setLanguage(Locale.ENGLISH);

                    if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(ChatActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ChatActivity.this, "Initialisation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dispCurrentUser=dataSnapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

        messageAdapter=new ChatMessageAdapter(messageList);


        chatRecycler.setHasFixedSize(true);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatRecycler.setAdapter(messageAdapter);
        
        loadmessages();




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m=message.getText().toString();
                message.setError(null);
                View focusView=null;
                boolean cancel=false;
                if(TextUtils.isEmpty(message.getText().toString().trim()))
                {
                    message.setError("Can't send empty message");
                    focusView=message;
                    cancel=true;
                }


                if(cancel)
                {
                    focusView.requestFocus();
                }
                else
                {
                    final HashMap<String ,String> chatmessage=new HashMap<>();
                    chatmessage.put("display_name",dispCurrentUser);
                    chatmessage.put("mess",m);
                    chatmessage.put("uid",currentUid);

                    final ChatModel model=new ChatModel(dispCurrentUser,message.getText().toString(),currentUid);
                    model.setDisplay_name(dispCurrentUser);
                    model.setMess(message.getText().toString());
                    model.setUid(currentUid);
                    System.out.println("ChatActivity:"+model.getMess());
                    System.out.println("ChatActivity"+model.getUid());

                    chat.push().setValue(chatmessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            //NOTIFICATION

                            notification.setSmallIcon(R.drawable.armehta);
                            notification.setTicker(dispCurrentUser+" send a message");
                            notification.setWhen(System.currentTimeMillis());
                            notification.setContentTitle("Title");
                            notification.setContentText(m);
                            Intent notifyIntent=new Intent(ChatActivity.this,ChatActivity.class);
                            PendingIntent pendingIntent=PendingIntent.getActivity(ChatActivity.this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                            notification.setContentIntent(pendingIntent);

                            //Send Notification

                            NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            nm.notify(uniqueId,notification.build());
                            System.out.println("Notification Manager"+nm);

                            message.setText("");
                           /*
                           TEXTTOSPEECH
                           mTTS.setPitch(50);
                            mTTS.speak(m,TextToSpeech.QUEUE_FLUSH,null);*/


                            if(!task.isSuccessful())
                            {
                                Toast.makeText(ChatActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage++;
                messageList.clear();
                loadmessages();

            }
        });

        /*ChildEventListener listener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                messList.add(dataSnapshot);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        chat.addChildEventListener(listener);
        options=new FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(chat,ChatModel.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<ChatModel, ChattingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(ChattingViewHolder holder, int position, ChatModel model) {
                model.setKey(options.getSnapshots().getSnapshot(position).getKey());
                System.out.println("Username:"+model.getUserName());
                holder.userName.setText(model.getUserName());
                holder.message.setText(model.getMessage());
            }

            @Override
            public ChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(ChatActivity.this).inflate(R.layout.message_layout,parent,false);
                ChattingViewHolder chattingViewHolder=new ChattingViewHolder(view);
                return chattingViewHolder;
            }
        };

        adapter.startListening();
        chatRecycler.setAdapter(adapter);*/
    }

    private void loadmessages() {

        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("messages");

        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatModel model = dataSnapshot.getValue(ChatModel.class);
                // System.out.println("Key"+model.getKey());
                System.out.println("values" + model.getDisplay_name() + "-" + model.getMess());
                messageList.add(model);
                messageAdapter.notifyDataSetChanged();

                chatRecycler.scrollToPosition(messageList.size() - 1);
                mRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

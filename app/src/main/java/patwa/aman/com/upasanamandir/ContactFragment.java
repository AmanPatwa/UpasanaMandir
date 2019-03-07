package patwa.aman.com.upasanamandir;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dell on 05-12-2018.
 */

public class ContactFragment extends Fragment implements RecyclerView_Adapter.ContactItemClickListner {
    RecyclerView recyclerView;
    //ArrayList<String> arrayList=new ArrayList();
    //Context context=MainActivity.context;
    String[] contact_name= new String[]{"Maneklal Nanchand Shah","Ashokbhai Rangildas Mehta", "Bhaveshbhai Babulal Shah","Manojbhai Tarachand Shah","Atulbhai Babulal Shah"};
    String[] contact= new String[]{"9867310070","9892374574", "9324207348","9820132006","9820959663"};
    int[] img= new int[]{
            R.drawable.mnshah,R.drawable.armehta,R.drawable.bhavesh,R.drawable.manoj,R.drawable.atul
    };


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_contact, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setHasFixedSize(true);

        RecyclerView_Adapter adapter=new RecyclerView_Adapter(contact_name,contact,img,this);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void contactItemClicked(int item_no) {
        //Toast.makeText(getActivity(),"safji",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+contact[item_no]));
            startActivity(intent);

    }

    /*<LinearLayout
    android:id="@+id/chat_linearLayout"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:layout_alignParentStart="true"
    android:layout_alignParentTop="true"
    android:layout_marginTop="524dp"
    android:orientation="horizontal"
    android:weightSum="10">

        <ImageButton
    android:id="@+id/chat_plus"
    android:layout_width="36dp"
    android:layout_height="match_parent"
    android:layout_gravity="left"
    android:layout_weight="1"
    android:src="@drawable/plus" />

        <EditText
    android:id="@+id/chat_send"
    android:layout_width="0dp"
    android:layout_height="fill_parent"
    android:imeActionId="@integer/register_form_finished"
    android:imeActionLabel="Send"
    android:imeOptions="actionUnspecified"
    android:inputType="textCapSentences|textAutoCorrect|textAutoComplete"
    android:maxLines="1"
    android:layout_weight="8" />


        <ImageButton
    android:id="@+id/chat_input"
    android:layout_width="36dp"
    android:layout_height="match_parent"
    android:layout_gravity="right"
    android:contentDescription="Send"
    android:layout_weight="1"
    android:src="@drawable/sendbutton" />


    </LinearLayout>

    <TextView
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/list_items"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Upasana Mandir"
        android:textSize="24sp"
        android:padding="10dp"
        android:textColor="@android:color/black"/>

android:background="@android:drawable/editbox_dropdown_light_frame"
<LinearLayout
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

    <de.hdodenhof.circleimageview.CircleImageView
        android:id="@+id/message_pic"
        android:layout_width="40dp"
        android:layout_height="40dp"
        android:layout_alignParentStart="true"
        android:layout_alignParentTop="true"
        android:src="@drawable/dummyimage" />
    */



}
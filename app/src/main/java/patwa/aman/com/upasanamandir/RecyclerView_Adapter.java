package patwa.aman.com.upasanamandir;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dell on 09-12-2018.
 */

public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.ContactViewHolder> {
     Context context=MainActivity.context;
    String[] mcontact_name, mcontact;
    int[] mimg;
    ContactItemClickListner mcontactItemClickListner;


    public RecyclerView_Adapter(String[] contact_name, String[] contact, int[] img, ContactFragment activity) {
        mcontact_name = contact_name;
        mcontact = contact;
        mimg = img;
        mcontactItemClickListner = activity;
    }

    public interface ContactItemClickListner {
        public void contactItemClicked(int item_no);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(context).inflate(R.layout.contact_display, parent,false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(contactView);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.setcontact_no(position);
        holder.setcontact_name(position);
        holder.setimg(position);
    }

    @Override
    public int getItemCount() {
        return mcontact.length;
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView contact_name;
        TextView contact_no;
        ImageView iv;

        public ContactViewHolder(View itemView) {
            super(itemView);

            contact_name = (TextView) itemView.findViewById(R.id.contactName);
            contact_no = (TextView) itemView.findViewById(R.id.contactNo);
            iv = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
          int clickedposition=getAdapterPosition();
          mcontactItemClickListner.contactItemClicked(clickedposition);
        }

        public void setcontact_no(int position) {
            contact_no.setText(mcontact[position]);
        }

        public void setimg(int position) {
            iv.setImageResource(mimg[position]);
        }

        public void setcontact_name(int position) {
            contact_name.setText(mcontact_name[position]);
        }
    }
}
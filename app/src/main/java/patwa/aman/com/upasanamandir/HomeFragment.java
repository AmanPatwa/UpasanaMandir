package patwa.aman.com.upasanamandir;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dell on 05-12-2018.
 */

public class HomeFragment extends Fragment {

    ImageView imageView;
    TextView home;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);

        imageView=(ImageView)view.findViewById(R.id.ms);
        home=(TextView)view.findViewById(R.id.welcome);

        DatabaseReference database=FirebaseDatabase.getInstance().getReference("home/Home");
        //DatabaseReference databaseReference=database.getReference("home");
        /*databaseReference.child("Home").setValue("      With God's grace and blessings of Saiyamaiklakshi Param Pujya Acharya Bhagvant Shreemad Vijay JAGACHCHANDRA  Suri Swarji maharajsaheb, dated Asad Sud Chaudas,Vikram Samvat 2065 an activity of knowledge &amp; understanding of panch pratikraman sutra course a plan of Pathshaala named UPASANA MANDIR has been established.\n" +
                "      This Paathshaala consists of boys with the age bar of minimum 8 years .With the aim WHERE A SAINT CANNOT REACH A PARTICULAR DESTINATION THERE A STUDENT TAKES A CHARGE AND TAKES CARE OF A SANGH At PARVA TITHIES AND PARVA PARYUSHAN . Recently we have 150 students who are ready with this course.");
        //databaseReference.child("Home").setValue("j");*/

        database.keepSynced(true);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value=dataSnapshot.getValue(String.class);
                home.setText(value);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* DatabaseReference course=database.getReference("Course");
        course.child("course").setValue("    In the 1st year of Upasana Mandir, the students learn Panch Pratikraman Sutra - Vidhi with Shudhi and Sampada. Along with this, the course also includes the learning of Paryushana Chaityavandan - Stuti - Stavan - Sajjay. Students have to compulsory do Chaudas Pratikraman, so that they learn to perform the Vidhi practically. After completing this course, an examiner takes exams of these students and only after his acceptance, they are awarded with a Bahuman of Rs.31,000.series.\n\n" +
                                "Here is list of highly qualified Professors:\n\n" +
                                "1.Panditvarya Shri Rameshbhai Vadilal Loladiya\n"  +
                                "2.Shri Chinulal Ambalal Shah\n" +
                                "3.Panditvarya Shri Nareshbhai Varaiya\n" +
                                "4.Panditvarya Shri Atulbhai R Loladiya\n"  +
                                "5.Panditvarya Shri Priyeshbhai Jashvantlal Shah\n" +
                                "6.Panditvarya Shri Chiragbhai Bipinchandra Vora\n" +
                                "7.Shri Gauravbhai Rathod - Music Teacher\n"  +
                                "8.Ms. Nancy Nareshbhai Shah - Dance Teacher\n\n" +
                                "Recently UPASANA MANDIR has started the TATVAGYAN course for the students who passed PANCH PRATIKAMAN in last 5 years rightly called PARYUSHANA SHRENI CLASS which is held every Friday 1 hour a week.");*/
       /*DatabaseReference db=database.getReference("Religious Activities");
        db.child("rel act").setValue("SINGING:\n" +
                "Students of Upasana Mandir are trained in singing to perform and contribute at various religious functions and programs.\n\n\n" +
                "MUSICAL INSTRUMENT PLAYING:\n" +
                "Students of Upasana Mandir are trained in playing various musical instrument including Tabla, Keyboard etc. This will enable them to perform and contribute at various religious functions and programs.\n\n\n" +
                "ACTING:\n" +
                "Students of Upasana Mandir are trained in acting. This includes performing mono acting as well as drama. This will enable them to perform and contribute at various religious stage functions and programs.\n\n\n" +
                "DANCE:\n" +
                "Students of Upasana Mandir are trained in dancing. This will enable them to perform and contribute at various religious stage functions and programs.\n");
*/

        return view;
    }
}

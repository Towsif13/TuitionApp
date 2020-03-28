package com.example.tuitionapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterPostTeacher extends RecyclerView.Adapter<AdapterPostTeacher.MyHolder> implements Filterable {
    Context context;
    List<Post> postList;
    List<Post> mDataFiltered;
    String myUid ;


    public AdapterPostTeacher (Context c , List<Post> p){

        this.context = c;
        this.postList = p;
        this.mDataFiltered = p;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate laout row_post.xml

        View view = LayoutInflater.from(context).inflate(R.layout.row_post_teacher,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        // bind data here
        final String id = mDataFiltered.get(position).getId();

        //get data
        final String postId= mDataFiltered.get(position).getPostId();

        String firstName = mDataFiltered.get(position).getFirstName();
        String lastName = mDataFiltered.get(position).getLastName();
        String Address = mDataFiltered.get(position).getAddress();
        String Date = mDataFiltered.get(position).getDate();
        String Days = mDataFiltered.get(position).getDays();
        String Year = mDataFiltered.get(position).getYear();
        String Dept = mDataFiltered.get(position).getDepartment();
        String PreferedMed = mDataFiltered.get(position).getPreferredMedium();

        String Notes = mDataFiltered.get(position).getNotes();
        String Region = mDataFiltered.get(position).getRegion();
        String Salary = mDataFiltered.get(position).getSalary();
        String sClass = mDataFiltered.get(position).getsClass();
        String Subjects = mDataFiltered.get(position).getSubjects();
        String Time = mDataFiltered.get(position).getTime();

        if(mDataFiltered.get(position).getProfileImage() != null) {
            Picasso.get().load(mDataFiltered.get(position).getProfileImage()).into(holder.ProfileImage);
        }


        //set Data
        holder.post_nameTV.setText(firstName+" "+lastName);
        holder.post_timeTV.setText(Time+"  "+Date);
        holder.post_subTV.setText(Subjects);
        holder.post_locTV.setText(Region);
        holder.post_daysTV.setText(Days);
        holder.YearTV.setText(Year);
        holder.preferedMedTv.setText(PreferedMed);
        holder.DeptTv.setText(Dept);
        holder.post_salTV.setText(Salary+"TK");
        if (Notes.length()<1){
            holder.post_notesTV.setVisibility(View.GONE);
            holder.note.setVisibility(View.GONE);
        }else {
            holder.post_notesTV.setText(Notes);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PublicTeacherProfileActivity.class);
                intent.putExtra("userid",mDataFiltered.get(position).getId());

                context.startActivity(intent);

            }
        });

        if(!myUid.equals(id)){
            holder.deleteButton.setVisibility(View.GONE);
        }else{
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletePost(postId);


                }
            });
        }



    }




    public void deletePost(String pId){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting .. ");
        Query fQuery = FirebaseDatabase.getInstance().getReference("TuitionPosts").child("TeacherPosts").orderByChild("postId").equalTo(pId);
        fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ds.getRef().removeValue();
                }
                Toast.makeText(context, "Deleted sucessfully", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                notifyItemRemoved(getItemCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override



    public int getItemCount() {
        if(mDataFiltered != null){
            return mDataFiltered.size();
        }



        return 0;

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key = charSequence.toString();
                if(key.isEmpty()){
                    mDataFiltered = postList;

                }else {
                    List <Post> listFiltered = new ArrayList<>();
                    for(Post row : postList){
                        if(row.getRegion().toLowerCase().contains(key.toLowerCase())|| row.getDepartment().toLowerCase().contains(key.toLowerCase())){
                            listFiltered.add(row);
                        }
                    }

                    mDataFiltered = listFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                mDataFiltered = (List<Post>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // creating adapter class for recyleviwew
    class MyHolder extends RecyclerView.ViewHolder{

        //button and views from row_post
        ImageView ProfileImage;
        ImageButton moreBtnTV,sendMsgBtn,sendReq ,deleteButton;
        TextView post_nameTV ,post_timeTV,post_idTV,post_locTV,post_categoryTV,post_salTV,post_genderTV ,post_classTv,post_daysTV,post_subTV,post_notesTV
                ,post_mediumTV,post_preferenceTV, note, YearTV, DeptTv,preferedMedTv;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            //init views
            ProfileImage = itemView.findViewById(R.id.profile_image);

//            moreBtnTV = itemView.findViewById(R.id.moreBtnTV);
//            sendMsgBtn = itemView.findViewById(R.id.sendMsgBtn);
//            sendReq = itemView.findViewById(R.id.sendReq);
            YearTV = itemView.findViewById(R.id.Yeartv);
            post_nameTV = itemView.findViewById(R.id.post_nameTV);
            post_timeTV = itemView.findViewById(R.id.post_timeTV);
////
            DeptTv = itemView.findViewById(R.id.deptTV);
            preferedMedTv = itemView.findViewById(R.id.PreferMediumTV);
            post_classTv = itemView.findViewById(R.id.class_TV);
            post_subTV = itemView.findViewById(R.id.subjectTV);
            post_mediumTV = itemView.findViewById(R.id.mediumTV);
            post_locTV = itemView.findViewById(R.id.locationTV);
            post_daysTV = itemView.findViewById(R.id.daysTV);
            post_preferenceTV = itemView.findViewById(R.id.preferenceTV);
            post_salTV = itemView.findViewById(R.id.salaryTV);
            post_notesTV = itemView.findViewById(R.id.post_notesTV);
            deleteButton = itemView.findViewById(R.id.deleteBtn);


            note = itemView.findViewById(R.id.note);



        }
    }
}

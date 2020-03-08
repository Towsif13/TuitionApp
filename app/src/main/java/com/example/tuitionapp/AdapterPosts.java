package com.example.tuitionapp;

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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> implements Filterable {

    Context context;
    List<Post> postList;
    List<Post> mDataFiltered;
    ArrayList<Post> posts;


    public AdapterPosts (Context c , List<Post> p){

        this.context = c;
        this.postList = p;
        this.mDataFiltered = p;

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //inflate laout row_post.xml

        View view = LayoutInflater.from(context).inflate(R.layout.row_post,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        // bind data here
        final String id = mDataFiltered.get(position).getId();
        Log.d("AdapterPosts","ji"+id);

                //get data

        String firstName = mDataFiltered.get(position).getFirstName();
        String lastName = mDataFiltered.get(position).getLastName();
        String Address = mDataFiltered.get(position).getAddress();
        String Date = mDataFiltered.get(position).getDate();
        String Days = mDataFiltered.get(position).getDays();
        String Gender = mDataFiltered.get(position).getPreferredGender();
        String Medium = mDataFiltered.get(position).getMedium();
        String Notes = mDataFiltered.get(position).getNotes();
        String Region = mDataFiltered.get(position).getRegion();
        String Salary = mDataFiltered.get(position).getSalary();
        String sClass = mDataFiltered.get(position).getsClass();
        String Subjects = mDataFiltered.get(position).getSubjects();
        String Time = mDataFiltered.get(position).getTime();

        // convert Time stap to dd/mm/yyyy hh:mm am/pm
        //Calendar calender = Calendar.getInstance(Locale.getDefault());
        //calender.setTimeInMillis(Long.parseLong(Time));
       // String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa",calender).toString();

        //set Data
        if(mDataFiltered.get(position).getProfileImage() != null) {
            Picasso.get().load(mDataFiltered.get(position).getProfileImage()).into(holder.ProfileImage);
        }

        holder.post_nameTV.setText(firstName+" "+lastName);
        holder.post_timeTV.setText(Time+"  "+Date);
        holder.post_classTv.setText(sClass);
        holder.post_subTV.setText(Subjects);
        holder.post_mediumTV.setText(Medium);
        holder.post_locTV.setText(Region);
        holder.post_daysTV.setText(Days);
        holder.post_preferenceTV.setText(Gender);
        holder.post_salTV.setText(Salary+"TK");
        if (Notes.length()<1){
            holder.post_notesTV.setVisibility(View.GONE);
            holder.note.setVisibility(View.GONE);
        }else {
            holder.post_notesTV.setText(Notes);
        }




        //holder.post_idTV.setText(firstNameName);

//        holder.post_locTV.setText(Address+Region);
//        holder.post_categoryTV.setText(Medium);
//        holder.post_salTV.setText(Salary);
//        holder.post_genderTV.setText(Gender);
//        holder.post_classTv.setText(sClass);
//        holder.post_daysTV.setText(Days);
//        holder.post_subTV.setText(Subjects);
//        holder.post_notesTV.setText(Notes);





        //userDpr later task
        try{


        }catch (Exception e){

        }


        // handle button click

        // will implement later
//        holder.moreBtnTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(context, "More ", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        holder.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Send msg ", Toast.LENGTH_SHORT).show();
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
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
                        if(row.getPreferredGender().toLowerCase().contains(key.toLowerCase())|| row.getsClass().toLowerCase().contains(key.toLowerCase())){
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
        ImageButton moreBtnTV,sendMsgBtn,sendReq;
        TextView post_nameTV ,post_timeTV,post_idTV,post_locTV,post_categoryTV,post_salTV,post_genderTV ,post_classTv,post_daysTV,post_subTV,post_notesTV
                ,post_mediumTV,post_preferenceTV, note;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            //init views
            ProfileImage = itemView.findViewById(R.id.profile_image);
//            moreBtnTV = itemView.findViewById(R.id.moreBtnTV);
//            sendMsgBtn = itemView.findViewById(R.id.sendMsgBtn);
//            sendReq = itemView.findViewById(R.id.sendReq);

            post_nameTV = itemView.findViewById(R.id.post_nameTV);
            post_timeTV = itemView.findViewById(R.id.post_timeTV);
////
            post_classTv = itemView.findViewById(R.id.class_TV);
            post_subTV = itemView.findViewById(R.id.subjectTV);
            post_mediumTV = itemView.findViewById(R.id.mediumTV);
            post_locTV = itemView.findViewById(R.id.locationTV);
            post_daysTV = itemView.findViewById(R.id.daysTV);
            post_preferenceTV = itemView.findViewById(R.id.preferenceTV);
            post_salTV = itemView.findViewById(R.id.salaryTV);
            post_notesTV = itemView.findViewById(R.id.post_notesTV);

            note = itemView.findViewById(R.id.note);



//            post_idTV = itemView.findViewById(R.id.post_idTV);
//            post_locTV = itemView.findViewById(R.id.post_locTV);
//            post_categoryTV = itemView.findViewById(R.id.post_categoryTV);
//            post_salTV = itemView.findViewById(R.id.post_salTV);
//            post_genderTV = itemView.findViewById(R.id.post_genderTV);
//            post_classTv = itemView.findViewById(R.id.post_classTv);
//            post_daysTV = itemView.findViewById(R.id.post_daysTV);
//            post_subTV = itemView.findViewById(R.id.post_subTV);
//            post_notesTV = itemView.findViewById(R.id.post_notesTV);





        }
    }


}

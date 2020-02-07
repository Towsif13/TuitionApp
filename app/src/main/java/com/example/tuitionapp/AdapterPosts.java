package com.example.tuitionapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {

    Context context;
    List<Post> postList;


    public AdapterPosts (Context c , List<Post> p){

        this.context = c;
        this.postList = p;

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
        //get data

        String firstNameName = postList.get(position).getFirstName();
        String lastName = postList.get(position).getLastName();

        String Address = postList.get(position).getAddress();
        String Date = postList.get(position).getDate();
        String Days = postList.get(position).getDays();
        String Gender = postList.get(position).getPreferredGender();
        String Medium = postList.get(position).getMedium();
        String Notes = postList.get(position).getNotes();
        String Region = postList.get(position).getRegion();
        String Salary = postList.get(position).getSalary();
        String sClass = postList.get(position).getsClass();
        String Subjects = postList.get(position).getSubjects();
        String Time = postList.get(position).getTime();

        // convert Time stap to dd/mm/yyyy hh:mm am/pm
        //Calendar calender = Calendar.getInstance(Locale.getDefault());
        //calender.setTimeInMillis(Long.parseLong(Time));
       // String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa",calender).toString();

        //set Data
        holder.post_nameTV.setText(firstNameName+" "+lastName);
        holder.post_timeTV.setText(Time);
        holder.post_idTV.setText(firstNameName);
        holder.post_locTV.setText(Address+Region);
        holder.post_categoryTV.setText(Medium);
        holder.post_salTV.setText(Salary);
        holder.post_genderTV.setText(Gender);
        holder.post_classTv.setText(sClass);
        holder.post_daysTV.setText(Days);
        holder.post_subTV.setText(Subjects);
        holder.post_notesTV.setText(Notes);





        //userDpr later task
        try{


        }catch (Exception e){

        }


        // handle button click

        // will implement later
        holder.moreBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "More ", Toast.LENGTH_SHORT).show();
            }
        });

        holder.sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Send msg ", Toast.LENGTH_SHORT).show();
            }
        });

        holder.sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "send req", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // creating adapter class for recyleviwew
    class MyHolder extends RecyclerView.ViewHolder{

        //button and views from row_post
        ImageButton moreBtnTV,sendMsgBtn,sendReq;
        TextView post_nameTV ,post_timeTV,post_idTV,post_locTV,post_categoryTV,post_salTV,post_genderTV ,post_classTv,post_daysTV,post_subTV,post_notesTV;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            //init views
            moreBtnTV = itemView.findViewById(R.id.moreBtnTV);
            sendMsgBtn = itemView.findViewById(R.id.sendMsgBtn);
            sendReq = itemView.findViewById(R.id.sendReq);
            post_nameTV = itemView.findViewById(R.id.post_nameTV);
            post_timeTV = itemView.findViewById(R.id.post_timeTV);
            post_idTV = itemView.findViewById(R.id.post_idTV);
            post_locTV = itemView.findViewById(R.id.post_locTV);
            post_categoryTV = itemView.findViewById(R.id.post_categoryTV);
            post_salTV = itemView.findViewById(R.id.post_salTV);
            post_genderTV = itemView.findViewById(R.id.post_genderTV);
            post_classTv = itemView.findViewById(R.id.post_classTv);
            post_daysTV = itemView.findViewById(R.id.post_daysTV);
            post_subTV = itemView.findViewById(R.id.post_subTV);
            post_notesTV = itemView.findViewById(R.id.post_notesTV);





        }
    }


}

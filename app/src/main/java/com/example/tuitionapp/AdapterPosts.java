package com.example.tuitionapp;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> implements Filterable {

    Context context;
    List<Post> postList;
    List<Post> mDataFiltered;


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
        final Post post = mDataFiltered.get(position);

        //get data

        String firstNameName = mDataFiltered.get(position).getFirstName();
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

                Intent intent = new Intent(context,PublicStudentProfileActivity.class);
                intent.putExtra("visit_user_id",post.getId());
                context.startActivity(intent);

               // Toast.makeText(context, "send req", Toast.LENGTH_SHORT).show();
            }
        });


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

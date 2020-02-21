package com.example.tuitionapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;


public class PostFragment extends Fragment {


    FirebaseAuth firebaseAuth;


    RecyclerView recyclerView;
    List<Post> postList;
    AdapterPosts adapterPosts;

    EditText seachInput;



    public PostFragment(){

        // required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);


        seachInput = view.findViewById(R.id.search_item);

        //init
        firebaseAuth =FirebaseAuth.getInstance();


        // recycler view and its properties

        recyclerView = view.findViewById(R.id.postRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        //set layout to recycleview
        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();
        loadPosts();
        seachInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                adapterPosts.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return  view;


    }
//    // inflate option menu
//    public void onCreatOptionsMenu(Menu menu, MenuInflater inflater){
//
//        inflater.inflate(R.menu.menu_options,menu);
//
//        //searchview to search posts by by category
//      //  MenuItem item =menu.findItem(R.id.searchAction);
//
//       // SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
////        search listener
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                //called when user press search button
////
////                if(!TextUtils.isEmpty(query)){
////
////                    searchPosts(query);
////                }else{
////                    loadPosts();
////                }
////
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String newText) {
////                //called as and when user oress any letter
////
////                if(!TextUtils.isEmpty(newText)){
////
////                    searchPosts(newText);
////                }else{
////                    loadPosts();
////                }
////                return false;
////            }
////        });
//
//
//        super.onCreateOptionsMenu(menu,inflater);
//
//    }




    private void loadPosts(){
        // path of all posts
       // String userId = mAuth.getCurrentUser().getUid();
        // FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child(userId).child(userId+"-"+date);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("StudentPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                   Post post = ds.getValue(Post.class);
                   postList.add(post);

                   //adapter
                    adapterPosts = new AdapterPosts(getActivity(),postList);
                    // set adapter to recycleview

                    recyclerView.setAdapter(adapterPosts);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error handlle
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // search option isnt fully done yet

    private void searchPosts(final String  query){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("StudentPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);


                    if(post.getsClass().toLowerCase().contains(query.toLowerCase())||post.getPreferredGender().toLowerCase().contains(query.toLowerCase())
                            ||post.getAddress().toLowerCase().contains(query.toLowerCase())){

                        postList.add(post);


                    }


                    //adapter
                    adapterPosts = new AdapterPosts(getActivity(),postList);
                    // set adapter to recycleview

                    recyclerView.setAdapter(adapterPosts);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error handlle
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}

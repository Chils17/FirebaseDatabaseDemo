package com.webmyne.firebasedatabasedemo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.webmyne.firebasedatabasedemo.R;
import com.webmyne.firebasedatabasedemo.adapter.UserAdapter;
import com.webmyne.firebasedatabasedemo.helper.AppConstants;
import com.webmyne.firebasedatabasedemo.model.User;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    private AppCompatTextView txtName;
    private RecyclerView rvUserList;
    private Context context;
    private ArrayList<User> userList;
    private UserAdapter adapter;
    private AppCompatTextView txtEmail;
    private SharedPreferences sharedPreferences;
    private AppCompatTextView txtAlert;
    private FloatingActionButton fab;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        getSupportActionBar().setTitle("User Details");

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userId = mDatabase.push().getKey();
        init();
        actionListener();
        initRecycler();
    }

    private void init() {
        context = UserListActivity.this;
        txtName = (AppCompatTextView) findViewById(R.id.txtName);
        txtEmail = (AppCompatTextView) findViewById(R.id.txtEmail);
        txtAlert = (AppCompatTextView) findViewById(R.id.txtAlert);
        rvUserList = (RecyclerView) findViewById(R.id.rvUserList);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void actionListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserDetail();
            }
        });
    }

    private void addUserDetail() {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra(AppConstants.Intent_From, AppConstants.Add);
        startActivity(intent);
    }

    private void initRecycler() {
        userList = new ArrayList<>();
        adapter = new UserAdapter(context, userList, new UserAdapter.OnclickItem() {
            @Override
            public void onClickItem(final User user) {
                final CharSequence[] items = {"Edit", "Delete"};
                new AlertDialog.Builder(context)
                        .setTitle("User Records")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                Log.e("item click", "" + i);
                                if (i == 0) {
                                    Intent intent = new Intent(context, AddActivity.class);
                                    intent.putExtra(AppConstants.Intent_From, AppConstants.Update);
                                    intent.putExtra(AppConstants.UserDetail, user);
                                    startActivity(intent);

                                    getDataFromFirebase();

                                    userList.add(user);

                                    adapter.notifyDataSetChanged();

                                } else if (i == 1) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                    alertDialog.setTitle("Confirm Delete...");
                                    alertDialog.setMessage("Are you sure you want delete this?");
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            mDatabase.child(user.getKey()).removeValue();

                                            getDataFromFirebase();

                                            checkVisibility(userList);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(context, "You clicked on YES", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });
                                    alertDialog.show();
                                }


                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
        rvUserList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rvUserList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(20, 10, 20, 10);
            }
        });
        rvUserList.setItemAnimator(new DefaultItemAnimator());
        rvUserList.setHasFixedSize(true);
        rvUserList.setAdapter(adapter);

        getDataFromFirebase();

    }


    private void getDataFromFirebase() {
        if (userList != null) {
            userList.clear();
            Log.e("list", userList.toString());

            mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    getAllTask(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    getAllTask(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    taskDeletion(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            checkVisibility(userList);
        }
    }

    private void getAllTask(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);

        userList.add(user);
        Log.e("name ", user.getName());
        adapter.setDataList(userList);
        adapter.notifyDataSetChanged();
        checkVisibility(userList);
    }

    private void taskDeletion(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getKey().equals(user)) {
                userList.remove(i);
            }
        }
        Log.e("name ", user.getName());
        adapter.setDataList(userList);
        adapter.notifyDataSetChanged();
        checkVisibility(userList);
    }


    private void checkVisibility(ArrayList<User> userList) {
        if (userList != null && userList.size() > 0) {
            txtAlert.setVisibility(View.GONE);
            rvUserList.setVisibility(View.VISIBLE);
        } else {
            txtAlert.setVisibility(View.VISIBLE);
            rvUserList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

package com.webmyne.firebasedatabasedemo.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.firebasedatabasedemo.R;
import com.webmyne.firebasedatabasedemo.model.User;

import java.util.ArrayList;

/**
 * Created by chiragpatel on 07-12-2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<User> userList;
    private OnclickItem onclickItem;


    public UserAdapter(Context context, ArrayList<User> userList, OnclickItem onclickItem) {
        this.context = context;
        this.userList = userList;
        this.onclickItem = onclickItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setValues(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setDataList(ArrayList<User> data) {
        userList = new ArrayList<>();
        userList = data;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private final AppCompatTextView txtName;
        private final AppCompatTextView txtEmail;
        private final AppCompatTextView txtMobile;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtMobile = itemView.findViewById(R.id.txtMobile);
        }

        public void setValues(final User user) {
//            Log.e("data", user.getName());
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());
            txtMobile.setText(user.getMobile());

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onclickItem.onClickItem(user);
                    return false;
                }
            });
        }
    }

    public interface OnclickItem {
        void onClickItem(User user);
    }
}

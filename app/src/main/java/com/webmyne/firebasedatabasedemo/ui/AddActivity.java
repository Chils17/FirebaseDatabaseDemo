package com.webmyne.firebasedatabasedemo.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webmyne.firebasedatabasedemo.R;
import com.webmyne.firebasedatabasedemo.helper.AppConstants;
import com.webmyne.firebasedatabasedemo.helper.InputValidation;
import com.webmyne.firebasedatabasedemo.model.User;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private NestedScrollView nestedScrollView;
    private AppCompatButton btnUpdate;
    private AppCompatButton btnAdd;
    private TextInputLayout textInputLayoutMobile;
    private TextInputEditText textInputEditTextMobile;
    private TextInputLayout textInputLayoutEmail;
    private TextInputEditText textInputEditTextEmail;
    private TextInputLayout textInputLayoutName;
    private TextInputEditText textInputEditTextName;
    private InputValidation inputValidation;
    private String key;
    private User user;
    private String intent_From;
    private DatabaseReference mDatabase;
    private User userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        key = mDatabase.push().getKey();
        init();
        getIntentData();
        actionListener();
    }


    private void getIntentData() {
        if (getIntent() != null) {
            intent_From = getIntent().getStringExtra(AppConstants.Intent_From);

            if (intent_From.equalsIgnoreCase(AppConstants.Update)) {
                Log.e("intent", intent_From);
                userDetail = (User) getIntent().getSerializableExtra(AppConstants.UserDetail);
                getSupportActionBar().setTitle("Edit Details");
                btnAdd.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);

                textInputEditTextName.setText(userDetail.getName());
                textInputEditTextEmail.setText(userDetail.getEmail());
                textInputEditTextMobile.setText(userDetail.getMobile());
            } else {
                Log.e("intent", intent_From);
                getSupportActionBar().setTitle("Add Details");
            }
        }
    }

    private void init() {
        context = AddActivity.this;
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        btnAdd = (AppCompatButton) findViewById(R.id.btnAdd);
        btnUpdate = (AppCompatButton) findViewById(R.id.btnUpdate);
        textInputLayoutMobile = (TextInputLayout) findViewById(R.id.textInputLayoutMobile);
        textInputEditTextMobile = (TextInputEditText) findViewById(R.id.textInputEditTextMobile);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);

        inputValidation = new InputValidation(context);
    }

    private void actionListener() {
        btnAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                addUserData();
                break;

            case R.id.btnUpdate:
                updateUserData();
                break;

        }
    }

    private void addUserData() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextMobile, textInputLayoutMobile, getString(R.string.error_message_mobile))) {
            return;
        }

        if (user == null) {
            String name = textInputEditTextName.getText().toString().trim();
            String email = textInputEditTextEmail.getText().toString().trim();
            String mobile = textInputEditTextMobile.getText().toString().trim();
            user = new User(key, name, email, mobile);

            mDatabase.child(user.getKey()).setValue(user);

            Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();

            emptyInputEditText();

            Intent intent = new Intent(context, UserListActivity.class);
            startActivity(intent);
            finish();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }


    }

    private void updateUserData() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextMobile, textInputLayoutMobile, getString(R.string.error_message_mobile))) {
            return;
        }

        if (mDatabase != null) {
            User user = new User(userDetail.getKey(),
                    textInputEditTextName.getText().toString().trim(),
                    textInputEditTextEmail.getText().toString().trim(),
                    textInputEditTextMobile.getText().toString().trim());

            mDatabase.child(user.getKey()).setValue(user);


            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            Intent intent = new Intent(context, UserListActivity.class);
            startActivity(intent);
            finish();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, "Error in update", Snackbar.LENGTH_LONG).show();
        }

    }

    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextMobile.setText(null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

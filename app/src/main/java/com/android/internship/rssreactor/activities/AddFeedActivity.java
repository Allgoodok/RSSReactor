package com.android.internship.rssreactor.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.internship.rssreactor.R;

public class AddFeedActivity extends AppCompatActivity {

    private ImageButton mAddFeed;
    private EditText mEditFeedName;
    private EditText mEditFeedURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);

        mAddFeed = (ImageButton) findViewById(R.id.button);
        mEditFeedName = (EditText) findViewById(R.id.editTextFeed);
        mEditFeedURL = (EditText) findViewById(R.id.editTextURL);

        mEditFeedName.addTextChangedListener(textWatcher);
        mEditFeedURL.addTextChangedListener(textWatcher);

        checkFieldsForEmptyValues(mAddFeed);

        mAddFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditFeedName.getText().toString();
                String url = mEditFeedURL.getText().toString();
                Intent intent = new Intent(AddFeedActivity.this, FeedsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("url", url);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues(mAddFeed);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private  void checkFieldsForEmptyValues(ImageButton b){

        String s1 = mEditFeedName.getText().toString();
        String s2 = mEditFeedURL.getText().toString();

        if(!(s1.equals("")) && !(s2.equals(""))) {
            b.setEnabled(true);
        } else {
            b.setEnabled(false);
        }
    }
}

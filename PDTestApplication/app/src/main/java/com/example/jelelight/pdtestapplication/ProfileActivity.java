package com.example.jelelight.pdtestapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private ImageView avatar;
    private TextView name,login,id,grav,url;
    private String imgUrl,uName,uLogin,uId,uGravatar,uUrl;
    private Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
        getExtra();
        bindView();
        setInformation();
    }

    private void getExtra(){
        if(getIntent().hasExtra("act_img")){
            imgUrl = getIntent().getStringExtra("act_img");
        }
        if(getIntent().hasExtra("act_id")){
            uId = getIntent().getStringExtra("act_id");
        }
        if(getIntent().hasExtra("act_login")){
            uLogin = getIntent().getStringExtra("act_login");
        }
        if(getIntent().hasExtra("act_display")){
            uName = getIntent().getStringExtra("act_display");
        }
        if(getIntent().hasExtra("act_gravatar")){
            uGravatar = getIntent().getStringExtra("act_gravatar");
        }
        if(getIntent().hasExtra("act_url")){
            uUrl = getIntent().getStringExtra("act_url");
        }
    }

    private void bindView(){
        avatar = findViewById(R.id.profile_avatar);
        name = findViewById(R.id.profile_name);
        id = findViewById(R.id.profile_id);
        login = findViewById(R.id.profile_login);
        grav = findViewById(R.id.profile_gravatar);
        url = findViewById(R.id.profile_url);

        backbtn = findViewById(R.id.back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setInformation(){
        new DownloadImageProfileTask(avatar).execute(imgUrl);

        name.setText(uName);
        id.setText(uId);
        login.setText(uLogin);
        grav.setText(uGravatar);
        url.setText(uUrl);
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uUrl));
                startActivity(browserIntent);
            }
        });
    }

    private class DownloadImageProfileTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageProfileTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

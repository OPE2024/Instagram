package com.onaopemipodimowo.instagram;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.parceler.Parcels;

import java.util.Date;

public class DetailsActivity extends AppCompatActivity {
    Post post;
    TextView tvDetailDescription;
    TextView tvDetailCreatedAt;
    TextView tvDetailsUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);

        tvDetailsUsername = findViewById(R.id.tvDetailsUsername);
        tvDetailDescription = findViewById(R.id.tvDetailsDescription);
        tvDetailCreatedAt = findViewById(R.id.tvDetailsCreatedAt);


        tvDetailsUsername.setText(post.getUser().getUsername());
        tvDetailDescription.setText(post.getDescription());
        tvDetailCreatedAt.setText(timeAgo);
    }
}
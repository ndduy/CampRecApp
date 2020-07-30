package com.example.camprecapp.features;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.camprecapp.R;
import com.example.camprecapp.models.Comment;
import com.example.camprecapp.models.Company;
import com.example.camprecapp.models.JobApplication;
import com.example.camprecapp.models.JobPost;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class JobApplicationDetailActivity extends AppCompatActivity {

    ImageView imgCurrentUser;
    TextView txtPostDesc, txtPostDateName, txtPostTitle;
    EditText editTextComment;
    Button btnAddComment;
    String PostKey;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static String COMMENT_KEY = "Comment";
    JobApplication jobApplication;
    Button openAttachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_application_detail);

        // ini Views
        RvComment = findViewById(R.id.rv_comment);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);

        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_detail_date_name);

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        openAttachment = findViewById(R.id.openAttachment);

        openAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = jobApplication.getDocumentUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        // add Comment button click listner

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnAddComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
                String comment_content = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                String uimg = firebaseUser.getPhotoUrl() == null ? null : firebaseUser.getPhotoUrl().toString();
                Comment comment = new Comment(comment_content, uid, uimg, uname);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("comment added");
                        editTextComment.setText("");
                        btnAddComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("fail to add comment : " + e.getMessage());
                    }
                });


            }
        });


        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);
        // get post id
        PostKey = getIntent().getExtras().getString("JobApplication");

        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(date);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(PostKey).get().continueWithTask(new Continuation<DocumentSnapshot, Task<List<DocumentSnapshot>>>() {
            @Override
            public Task<List<DocumentSnapshot>> then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                jobApplication = task.getResult().toObject(JobApplication.class);
                tasks.add(jobApplication.getJobpost().get());
                tasks.add(jobApplication.getCompany().get());
                Task combinedTask = Tasks.whenAllSuccess(tasks);
                return combinedTask;
            }
        }).addOnSuccessListener(new OnSuccessListener<List<DocumentSnapshot>>() {
            @Override
            public void onSuccess(List<DocumentSnapshot> documentSnapshots) {
                for (int i = 0; i < documentSnapshots.size(); i++) {
                    if (i % 2 == 0)
                        jobApplication.setJobPostData(documentSnapshots.get(i).toObject(JobPost.class));
                    else
                        jobApplication.setCompanyData(documentSnapshots.get(i).toObject(Company.class));
                }

                txtPostTitle.setText(jobApplication.getJobPostData().getTitle());
                txtPostDesc.setText(jobApplication.getJobPostData().getDescription());
                String date = timestampToString(getIntent().getExtras().getLong("postDate"));
                txtPostDateName.setText(date + " by " + jobApplication.getJobPostData().getCompanyName());

                if (jobApplication.getDocumentUrl() == null) {
                    openAttachment.setVisibility(View.GONE);
                } else {
                    openAttachment.setVisibility(View.VISIBLE);
                }
            }
        });


        // ini Recyclerview Comment
        iniRvComment();
    }

    private void iniRvComment() {

        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);

                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                RvComment.setAdapter(commentAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }


    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;


    }

}

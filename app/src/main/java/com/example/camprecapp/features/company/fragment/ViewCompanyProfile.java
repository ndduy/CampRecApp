package com.example.camprecapp.features.company.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camprecapp.features.MainActivity;
import com.example.camprecapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ViewCompanyProfile extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView txtViewName;
    TextView txtViewEmail;
    TextView txtViewPhoneNo;
    TextView txtViewAddress;
    TextView txtViewCity;
    TextView txtViewCompanyName;
    BottomNavigationView bottomNavigationView;
    Button btnLogOut, btnChangeProfilePic;
    StorageReference storageReference;
    ImageView profileImage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_view_company_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogOut = getView().findViewById(R.id.btnLogOut);
        txtViewName = getView().findViewById(R.id.txtViewCompanyUserName);
        txtViewEmail = getView().findViewById(R.id.txtViewComEmail);
        txtViewPhoneNo = getView().findViewById(R.id.txtViewComPhone);
        txtViewAddress = getView().findViewById(R.id.txtViewAddress);
        txtViewCity = getView().findViewById(R.id.txtViewCity);
        txtViewCompanyName = getView().findViewById(R.id.txtViewComName);
        bottomNavigationView = getView().findViewById(R.id.botComNavigationView);
        btnChangeProfilePic = getView().findViewById(R.id.btnUpdateProfilePic);
        profileImage = getView().findViewById(R.id.imgViewProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        viewProfile();

        btnChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        //change profile picture
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("profileImage/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(profileImage);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imgUri = data.getData();
                //profileImage.setImageURI(imgUri);

                uploadImageFirebase(imgUri);
            }
        }
    }

    void uploadImageFirebase(Uri imgUri) {
        //upload image to firebase storage
        final StorageReference fileRef = storageReference.child("profileImage/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerInside().into(profileImage);
                        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build());
                    }
                });
                //Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void viewProfile(){
        if (firebaseUser != null) {

            final String companyEmail = firebaseUser.getEmail();

            txtViewEmail.setText(companyEmail);
            FirebaseFirestore ff = FirebaseFirestore.getInstance();
            ff.collection("Company").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                        Map<String, Object> map = doc.getData();

                        txtViewName.setText(doc.getString("name"));
                        txtViewEmail.setText(doc.getString("email"));
                        txtViewPhoneNo.setText(doc.getString("phoneNumber"));
                        txtViewCompanyName.setText(doc.getString("companyName"));
                        txtViewAddress.setText(doc.getString("address"));
                        txtViewCity.setText(doc.getString("city"));
                    }
                }
            });
                    /*document(companyEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc = task.getResult();
                    Map<String, Object> map = doc.getData();

                    txtViewName.setText(doc.getString("name"));
                    txtViewEmail.setText(doc.getString("email"));
                    txtViewPhoneNo.setText(doc.getString("phoneNumber"));
                    txtViewCompanyName.setText(doc.getString("companyName"));
                    txtViewAddress.setText(doc.getString("address"));
                    txtViewCity.setText(doc.getString("city"));
                }
            });*/
            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    //whatever activity that was on this flag will be deleted when you press back button
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
    }
}

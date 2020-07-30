package com.example.camprecapp.features.company.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camprecapp.features.MainActivity;
import com.example.camprecapp.R;
import com.example.camprecapp.features.company.EditCompanyProfile;
import com.example.camprecapp.features.student.EditStudentProfile;
import com.example.camprecapp.models.Company;
import com.example.camprecapp.models.CompanyAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
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
    Button btnLogOut, btnChangeProfilePic, btnResetPassword, btnUpdateProfile;
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
        btnResetPassword = getView().findViewById(R.id.btnResetPassword2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        btnUpdateProfile = getView().findViewById(R.id.btnUpdateProfileCom);
        viewProfile();
        changeProfilePic();
        resetPassword();
        btnLogOut();
        updateProfileInfo();

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

                uploadImageFirebase(imgUri);
            }
        }
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                viewProfile();
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

    void changeProfilePic() {
        btnChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
    }

    void viewProfile() {
        if (firebaseUser != null) {

            final String companyEmail = firebaseUser.getEmail();

            txtViewEmail.setText(companyEmail);
            FirebaseFirestore ff = FirebaseFirestore.getInstance();
            ff.collection("CompanyAdmin").whereEqualTo("uId", firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);

                        txtViewName.setText(doc.getString("name"));
                        txtViewEmail.setText(doc.getString("email"));
                        txtViewPhoneNo.setText(doc.getString("phoneNumber"));

                        DocumentReference companyRef = doc.getDocumentReference("company");
                        companyRef.get().addOnSuccessListener(
                                new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot doc) {
                                        txtViewCompanyName.setText(doc.getString("name"));
                                        txtViewAddress.setText(doc.getString("address"));
                                        txtViewCity.setText(doc.getString("city"));
                                    }
                                }
                        );
                    }
                }
            });

        }
    }

    void btnLogOut() {
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

    void resetPassword() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final EditText resetPassword = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter New Password");
                passwordResetDialog.setView(resetPassword);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPassword = resetPassword.getText().toString();
                        firebaseUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Password Reset successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Password Reset Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close
                        onStop();
                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }

    void updateProfileInfo() {
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditCompanyProfile.class);
                i.putExtra("name", txtViewName.getText().toString());
                i.putExtra("email", txtViewEmail.getText().toString());
                i.putExtra("phone", txtViewPhoneNo.getText().toString());
                i.putExtra("address", txtViewAddress.getText().toString());
                i.putExtra("city", txtViewCity.getText().toString());
                i.putExtra("companyName", txtViewCompanyName.getText().toString());

                startActivityForResult(i, 1001);

            }
        });
    }
}

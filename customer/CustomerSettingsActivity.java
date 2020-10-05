package com.eitan.shopik.customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.eitan.shopik.Macros;
import com.eitan.shopik.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerSettingsActivity extends AppCompatActivity {

    private EditText mFirstNameField,mLastNameField,mAge,mAddress,mCity,mPhone;
    private MaterialButton submit;
    private Spinner gender_spinner;
    private ImageView bgImage;
    private CircleImageView mProfileImage,toolbar_pic;
    private String UserId;
    private DocumentReference customerFS;
    private String first_name;
    private String last_name;
    private String profileImageUrl;
    private String cover;
    private String age;
    private String address;
    private String city;
    private String phone;
    private String gender;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBar;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 ) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Uri resultUri = data.getData();
                mProfileImage.setImageURI(resultUri);
                toolbar_pic.setImageURI(resultUri);
                if (resultUri != null) {

                    final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(UserId);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resultUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);

                    Glide.with(getApplicationContext()).load(bitmap).into(mProfileImage);
                    Glide.with(getApplicationContext()).load(bitmap).into(toolbar_pic);

                    byte[] data2 = baos.toByteArray();

                    UploadTask uploadTask = filePath.putBytes(data2);

                    uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().
                            addOnSuccessListener(uri -> {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("imageUrl", uri.toString());
                        profileImageUrl = uri.toString();
                        customerFS.update(userInfo);
                    }));
                }
            }
        }
        if(requestCode == 2 ) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Uri resultUri = data.getData();
                bgImage.setImageURI(resultUri);
                if (resultUri != null) {

                    final StorageReference filePath = FirebaseStorage.getInstance().getReference().
                            child("coverImages").child(UserId);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().
                                getContentResolver(), resultUri);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);

                    Glide.with(getApplicationContext()).load(bitmap).into(bgImage);

                    byte[] data2 = baos.toByteArray();

                    UploadTask uploadTask = filePath.putBytes(data2);
                    uploadTask.addOnSuccessListener(taskSnapshot ->
                            filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("cover_photo", uri.toString());
                        cover = uri.toString();
                        customerFS.update(userInfo);
                    }));
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.fragment_settings);

        init();

        customerFS.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                age = documentSnapshot.get("age") != null ?
                        Objects.requireNonNull(documentSnapshot.get("age")).toString() : null;
                first_name = documentSnapshot.get("first_name") != null ?
                        Objects.requireNonNull(documentSnapshot.get("first_name")).toString() : null;
                last_name = documentSnapshot.get("last_name") != null ?
                        Objects.requireNonNull(documentSnapshot.get("last_name")).toString() : null;
                address = documentSnapshot.get("address") != null ?
                        Objects.requireNonNull(documentSnapshot.get("address")).toString() : null;
                city =  documentSnapshot.get("city") != null ?
                        Objects.requireNonNull(documentSnapshot.get("city")).toString() : null;
                phone = documentSnapshot.get("phone") != null ?
                        Objects.requireNonNull(documentSnapshot.get("phone")).toString() : null;
                profileImageUrl = documentSnapshot.get("imageUrl") != null ?
                        Objects.requireNonNull(documentSnapshot.get("imageUrl")).toString() : Macros.DEFAULT_PROFILE_IMAGE;
                cover = documentSnapshot.get("cover_photo") != null ?
                        Objects.requireNonNull(documentSnapshot.get("cover_photo")).toString() : Macros.DEFAULT_COVER_PHOTO;
                gender = documentSnapshot.get("gender") != null ?
                        Objects.requireNonNull(documentSnapshot.get("gender")).toString() : Macros.CustomerMacros.WOMEN;
            }
        }).addOnCompleteListener( task -> {
            mFirstNameField.setText(first_name);
            mLastNameField.setText(last_name);
            Glide.with(getApplicationContext()).load(profileImageUrl).into(mProfileImage);
            Glide.with(getApplicationContext()).load(profileImageUrl).into(toolbar_pic);
            Macros.Functions.GlidePicture(this,cover,bgImage);
            mAge.setText(age);
            mCity.setText(city);
            mAddress.setText(address);
            mPhone.setText(phone);
            gender_spinner.setSelection(gender.equals(Macros.CustomerMacros.WOMEN) ? 0 : 1);
            setTitle();
            setCollapsingBar();
        });

        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if(verticalOffset <= -300) {
                toolbar_pic.setVisibility(View.VISIBLE);
                mProfileImage.setVisibility(View.INVISIBLE);
            }
            else {
                toolbar_pic.setVisibility(View.INVISIBLE);
                mProfileImage.setVisibility(View.VISIBLE);
            }
        });

        mProfileImage.setOnLongClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,1);
            return true;
        });

        bgImage.setOnLongClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,2);
            return true;
        });

        setSpinner();
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        submit.setOnClickListener(v -> saveUserInformation());
    }

    private void setNavigationBarButtonsColor(int navigationBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            int flags = decorView.getSystemUiVisibility();
            if (isColorLight(navigationBarColor)) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            else {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            decorView.setSystemUiVisibility(flags);
        }
    }

    private boolean isColorLight(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }

    private void setCollapsingBar() {

        toolbar_pic.setBorderColor(Color.WHITE);

        final Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_medium);
        collapsingToolbar.setCollapsedTitleTypeface(typeface);
        collapsingToolbar.setExpandedTitleTypeface(typeface);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setContentScrimColor(getColor(R.color.CompanyProfileScrim));

        Glide.with(getApplicationContext()).asBitmap().load(cover).into(bgImage);
    }

    private void setTitle() {
        String name = first_name +" "+ last_name;
        collapsingToolbar.setTitle(name);
    }

    private void init() {

        setNavigationBarButtonsColor(getWindow().getNavigationBarColor());

        bgImage = findViewById(R.id.bgImage);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        appBar = findViewById(R.id.appbar);
        toolbar_pic = findViewById(R.id.toolbar_pic);
        mFirstNameField = findViewById(R.id.first_name);
        mLastNameField = findViewById(R.id.last_name);
        mProfileImage = findViewById(R.id.logo);
        mAge = findViewById(R.id.age);
        mAddress = findViewById(R.id.address);
        mCity = findViewById(R.id.city);
        mPhone = findViewById(R.id.phone);
        submit = findViewById(R.id.submit_btn);
        gender_spinner = findViewById(R.id.gender_switch);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        UserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        customerFS = FirebaseFirestore.getInstance().collection(Macros.CUSTOMERS).document(UserId);
    }

    private void saveUserInformation() {

        try {
            first_name = mFirstNameField.getText().toString();
            last_name = mLastNameField.getText().toString();
            city = mCity.getText().toString();
            phone = mPhone.getText().toString();
            age = mAge.getText().toString();
            address = mAddress.getText().toString();

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("first_name", first_name);
            userInfo.put("last_name", last_name);
            userInfo.put("age", age);
            userInfo.put("city", city);
            userInfo.put("address", address);
            userInfo.put("phone", phone);
            userInfo.put("gender", gender);

            customerFS.update(userInfo);
            setTitle();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.supportFinishAfterTransition();
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> gender_adapter = ArrayAdapter.
                createFromResource(CustomerSettingsActivity.this,
                        R.array.gender, R.layout.gender_item_drop_down);
        gender_adapter.setDropDownViewResource(R.layout.gender_item_drop_down);
        gender_spinner.setAdapter(gender_adapter);
    }
}

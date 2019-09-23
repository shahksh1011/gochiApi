package com.app.profileapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.profileapplication.models.User;
import com.app.profileapplication.utilities.CircleTransform;
import com.app.profileapplication.utilities.Parameters;
import com.app.profileapplication.utilities.TextValidator;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private static int SELECT_PICTURE = 1;

    @BindView(R.id.signUp_userProfileImageView)
    ImageView userProfileImageView;
    @BindView(R.id.signUp_firstNameEditText)
    EditText firstNameEditText;
    @BindView(R.id.signUp_lastNameEditText)
    EditText lastNameEditText;
    @BindView(R.id.signUp_emailEditText)
    EditText emailIdEditText;
    @BindView(R.id.signUp_passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.signUp_confirmPasswordEditText)
    EditText confirmPasswordEditText;
    @BindView(R.id.signUp_cityEditText)
    EditText cityEditText;
    @BindView(R.id.signUp_genderRadioGroup)
    RadioGroup genderRadioGroup;

    private Uri selectedImageURI;
    private User user;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        firstNameEditText.setText("");
        lastNameEditText.setText("");
        emailIdEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        cityEditText.setText("");

        firstNameEditText.addTextChangedListener(new TextValidator(firstNameEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (Parameters.EMPTY.equalsIgnoreCase(text))
                    textView.setError(Parameters.EMPTY_ERROR_MESSAGE);
            }
        });

        lastNameEditText.addTextChangedListener(new TextValidator(lastNameEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (Parameters.EMPTY.equalsIgnoreCase(text))
                    textView.setError(Parameters.EMPTY_ERROR_MESSAGE);
            }
        });

        emailIdEditText.addTextChangedListener(new TextValidator(emailIdEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (Parameters.EMPTY.equalsIgnoreCase(text))
                    textView.setError(Parameters.EMPTY_ERROR_MESSAGE);
            }
        });

        passwordEditText.addTextChangedListener(new TextValidator(passwordEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (Parameters.EMPTY.equalsIgnoreCase(text))
                    textView.setError(Parameters.EMPTY_ERROR_MESSAGE);
            }
        });

        confirmPasswordEditText.addTextChangedListener(new TextValidator(confirmPasswordEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (Parameters.EMPTY.equalsIgnoreCase(text))
                    textView.setError(Parameters.EMPTY_ERROR_MESSAGE);
                else if (!passwordEditText.getText().toString().equals(text))
                    textView.setError(Parameters.INCORRECT_CONFIRM_PASSWORD);
            }
        });

        cityEditText.addTextChangedListener(new TextValidator(cityEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (Parameters.EMPTY.equalsIgnoreCase(text))
                    textView.setError(Parameters.EMPTY_ERROR_MESSAGE);
            }
        });

        userProfileImageView.setOnClickListener(view -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, SELECT_PICTURE);
        });

    }

    @OnClick(R.id.signUp_signUpButton)
    public void submit(View view) {
        long size = 0;
        if (selectedImageURI != null) {
            File f = new File(selectedImageURI.getPath());
            size = f.length();
        }


        if (Parameters.EMPTY.equalsIgnoreCase(firstNameEditText.getText().toString()))
            firstNameEditText.setError(Parameters.EMPTY_ERROR_MESSAGE);
        else if (Parameters.EMPTY.equalsIgnoreCase(lastNameEditText.getText().toString()))
            lastNameEditText.setError(Parameters.EMPTY_ERROR_MESSAGE);
        else if (Parameters.EMPTY.equalsIgnoreCase(emailIdEditText.getText().toString()))
            emailIdEditText.setError(Parameters.EMPTY_ERROR_MESSAGE);
        else if (Parameters.EMPTY.equalsIgnoreCase(passwordEditText.getText().toString()))
            passwordEditText.setError(Parameters.EMPTY_ERROR_MESSAGE);
        else if (Parameters.EMPTY.equalsIgnoreCase(confirmPasswordEditText.getText().toString()))
            confirmPasswordEditText.setError(Parameters.EMPTY_ERROR_MESSAGE);
        else if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()))
            confirmPasswordEditText.setError(Parameters.INCORRECT_CONFIRM_PASSWORD);
        else if (Parameters.EMPTY.equalsIgnoreCase(cityEditText.getText().toString()))
            cityEditText.setError(Parameters.EMPTY_ERROR_MESSAGE);
        else if (selectedImageURI == null) {
            Toast.makeText(this, Parameters.UPLOAD_A_PROFILE_IMAGE, Toast.LENGTH_LONG).show();
        } else if (size >= 5242880) {
            Toast.makeText(this, Parameters.UPLOAD_IMAGE_LESS_THAN_5MB, Toast.LENGTH_LONG).show();
        } else {
            int checkedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(checkedRadioButtonId);

            user = new User(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                    emailIdEditText.getText().toString(),
                    cityEditText.getText().toString(), radioButton.getText().toString());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageURI = data.getData();
                Picasso.get()
                        .load(selectedImageURI)
                        .transform(new CircleTransform()).centerCrop().fit()
                        .into(userProfileImageView);
            }
        }
    }


    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}

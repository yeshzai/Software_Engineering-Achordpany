package com.example.achordpany.ui.auth;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.achordpany.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    FirebaseUser user;
    private Drawable defaultBackground;
    private boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        user = FirebaseAuth.getInstance().getCurrentUser();
        EditText changePassword_Password = findViewById(R.id.changePassword_Password);
        EditText changePassword_ConfirmPassword = findViewById(R.id.changePassword_ConfirmPassword);
        Button changePassword_Button = findViewById(R.id.changePassword_Button);
        defaultBackground = changePassword_Password.getBackground();

        // PASSWORD HIDE/VISIBLE
        changePassword_Password.setOnTouchListener((v, event) -> {

            changePassword_Password.setBackground(defaultBackground);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = changePassword_Password.getWidth();
                int paddingRight = changePassword_Password.getPaddingRight();
                float touchX = event.getX();

                if (event.getRawX() >= (changePassword_Password.getRight() - changePassword_Password.getCompoundDrawables()[2].getBounds().width())) {

                    if(!changePassword_Password.isFocused()) {
                        changePassword_Password.requestFocus();
                    }

                    if (isPasswordVisible) {    // HIDE
                        changePassword_Password.setTransformationMethod(new PasswordTransformationMethod());
                        changePassword_Password.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eyehide_black), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        changePassword_Password.setTransformationMethod(null);
                        changePassword_Password.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eye_black), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isPasswordVisible = !isPasswordVisible;
                    changePassword_Password.setSelection(changePassword_Password.getText().length());
                    return true;
                }
            }
            return false;
        });

        // PASSWORD HIDE/VISIBLE
        changePassword_ConfirmPassword.setOnTouchListener((v, event) -> {

            changePassword_ConfirmPassword.setBackground(defaultBackground);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = changePassword_ConfirmPassword.getWidth();
                int paddingRight = changePassword_ConfirmPassword.getPaddingRight();
                float touchX = event.getX();

                if (event.getRawX() >= (changePassword_ConfirmPassword.getRight() - changePassword_ConfirmPassword.getCompoundDrawables()[2].getBounds().width())) {

                    if(!changePassword_ConfirmPassword.isFocused()) {
                        changePassword_ConfirmPassword.requestFocus();
                    }

                    if (isPasswordVisible) {    // HIDE
                        changePassword_ConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                        changePassword_ConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eyehide_black), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        changePassword_ConfirmPassword.setTransformationMethod(null);
                        changePassword_ConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eye_black), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isPasswordVisible = !isPasswordVisible;
                    changePassword_ConfirmPassword.setSelection(changePassword_ConfirmPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        changePassword_Button.setOnClickListener(v -> {

            if(changePassword_Password.getText().toString().isEmpty())
                changePassword_Password.setBackgroundResource(R.drawable.edittext_error);
            else
                changePassword_Password.setBackground(defaultBackground);

            if(changePassword_ConfirmPassword.getText().toString().isEmpty())
                changePassword_ConfirmPassword.setBackgroundResource(R.drawable.edittext_error);
            else
                changePassword_Password.setBackground(defaultBackground);

            if(changePassword_Password.getText().toString().isEmpty() || changePassword_ConfirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(ChangePasswordActivity.this, "Please Fill Out All Fields!", Toast.LENGTH_SHORT).show();
            } else {
                if(changePassword_Password.getText().toString().equals(changePassword_ConfirmPassword.getText().toString())) {

                    changePassword_Password.setBackground(defaultBackground);
                    changePassword_ConfirmPassword.setBackground(defaultBackground);
                    changeUserPassword(changePassword_Password.getText().toString());

                } else {

                    Toast.makeText(ChangePasswordActivity.this, "Passwords Do Not Match!", Toast.LENGTH_SHORT).show();
                    changePassword_Password.setBackgroundResource(R.drawable.edittext_error);
                    changePassword_ConfirmPassword.setBackgroundResource(R.drawable.edittext_error);

                }
            }

        });

    }

    private void changeUserPassword(String new_password) {

        if (user != null) {
            user.updatePassword(new_password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                            Log.d("[CHANGE PASSWORD]", "[SUCCESS] Changed Successfully!");
                        } else {
                            Toast.makeText(getApplicationContext(), "Password Failed to Change!", Toast.LENGTH_SHORT).show();
                            Log.d("[CHANGE PASSWORD]", "[FAILED] Change Failed!");
                        }
                    });
        } else {
            Log.d("[CHANGE PASSWORD]", "[NaN] No Account Found!");
        }

    }

}
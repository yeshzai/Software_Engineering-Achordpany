/*
    This file will contain the credentials to be passed to different fragments.
*/

package com.example.achordpany.ui.signup;

import java.util.ArrayList;

public class SignUpCredentials {

    private static SignUpCredentials instance;
    private String credential_usernameText;
    private String credential_emailAddressText;
    private String credential_passwordText;
    private String credential_confirmPasswordText;
    private ArrayList<String> credential_genre = new ArrayList<>();

    private SignUpCredentials() {
    }

    public static SignUpCredentials getInstance() {
        if (instance == null) {
            instance = new SignUpCredentials();
        }
        return instance;
    }

    // SETTER
    public void set_credential_usernameText(String credential_usernameText) {
        this.credential_usernameText = credential_usernameText;
    }

    public void set_credential_emailAddressText(String credential_emailAddressText) {
        this.credential_emailAddressText = credential_emailAddressText;
    }

    public void set_credential_passwordText(String credential_passwordText) {
        this.credential_passwordText = credential_passwordText;
    }

    public void set_credential_confirmPasswordText(String credential_confirmPasswordText) {
        this.credential_confirmPasswordText = credential_confirmPasswordText;
    }

    public void set_credential_genre(ArrayList<String> credential_genre) {
        this.credential_genre = credential_genre;
    }

    // GETTER
    public String get_credential_usernameText() {
        return credential_usernameText;
    }

    public String get_credential_emailAddressText() {
        return credential_emailAddressText;
    }

    public String get_credential_passwordText() {
        return credential_passwordText;
    }

    public String get_credential_confirmPasswordText() {
        return credential_confirmPasswordText;
    }

    public ArrayList<String> get_credential_genre() {
        return credential_genre;
    }

}

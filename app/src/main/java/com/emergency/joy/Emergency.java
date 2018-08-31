package com.emergency.joy;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

public class Emergency extends Application {

    private SharedPreferences mySharedPreference;
    private SharedPreferences.Editor mySharedPreferenceEditor;
    private String MyPreference = "MyDataPreference";
    public String  SignIn = "SIGNIN";

    private boolean hasSignedIn(){
        return mySharedPreference.getBoolean(SignIn, false);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mySharedPreference = this.getSharedPreferences(MyPreference, MODE_PRIVATE);
        mySharedPreferenceEditor = mySharedPreference.edit();
        mySharedPreferenceEditor.apply();

        if(!hasSignedIn()){
              startActivity(new Intent(this, Login.class));
        }

    }
}

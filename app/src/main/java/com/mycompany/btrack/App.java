package com.mycompany.btrack;

import android.app.Application;

import com.firebase.client.Firebase;

public class App extends Application {

    // objects that persist throughout the application's lifecycle
    // firebase could database
    private static Firebase firebase;
    // user credentials
    private static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize firebase
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://burning-torch-5586.firebaseio.com/");
    }

    public final Firebase getFirebase() {
        return firebase;
    }

    public final User getUser() {
        return user;
    }

    public final void setUser(User user) {
        this.user = user;
    }
}

package com.example.bankingproject;

import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class database {

    private DatabaseReference dbReference;

    public database() {
        dbReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public void insertData(String username, String email, String password, DatabaseCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userId == null) {
            callback.onComplete(false);
            return;
        }

        struct userData = new struct(username, email, password);

        dbReference.child(userId).setValue(userData).addOnCompleteListener(task -> {
            callback.onComplete(task.isSuccessful());
        });
    }

    public void getUserData(String userId, ReadCallback callback) {
        dbReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                struct userData = task.getResult().getValue(struct.class);
                if (userData != null)
                {
                    callback.onComplete(true, userData);
                } else {
                    callback.onComplete(false, null);
                }
            } else {
                callback.onComplete(false, null);
            }
        });
    }

    public void updateUserData(String userId, String username, String email, String password, DatabaseCallback callback) {
        struct updatedUserData = new struct(username, email, password);

        dbReference.child(userId).setValue(updatedUserData).addOnCompleteListener(task -> {
            callback.onComplete(task.isSuccessful());
        });
    }

    public void deleteUserData(String userId, DatabaseCallback callback) {
        dbReference.child(userId).removeValue().addOnCompleteListener(task -> {
            callback.onComplete(task.isSuccessful());
        });
    }

    public void isValidUser(String emailOrUsername, String password, FirebaseCallback callback) {
        dbReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isValid = false;
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    struct userData = snapshot.getValue(struct.class);
                    if (userData != null &&
                            (userData.getEmail().equals(emailOrUsername) || userData.getUsername().equals(emailOrUsername)) &&
                            userData.getPassword().equals(password)) {
                        String userId = snapshot.getKey();
                        callback.onComplete(true, userId);
                        return;
                    }
                }
                callback.onComplete(false, null);
            } else {
                callback.onComplete(false, null);
            }
        });
    }

    public void logout(FirebaseLogoutCallback callback) {
        FirebaseAuth.getInstance().signOut();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            callback.onComplete(true);
        } else {
            callback.onComplete(false);
        }
    }


    public interface FirebaseCallback {
        void onComplete(boolean isValid, String userId);
    }

    public interface DatabaseCallback {
        void onComplete(boolean isSuccess);
    }

    public interface ReadCallback {
        void onComplete(boolean isSuccess, struct userData);
    }

    public interface FirebaseLogoutCallback {
        void onComplete(boolean isLoggedOut);
    }
}

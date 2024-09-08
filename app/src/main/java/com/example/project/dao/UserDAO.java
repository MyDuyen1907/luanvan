package com.example.project.dao;

import com.example.project.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDAO extends BaseDAO {

    FirebaseUser user;
    User userBase;

    public UserDAO (){
        super("user");
        this.userBase = new User();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        if (this.user != null) {
            System.out.println("ok");
        } else {
            // No user is signed in
            System.out.println("not ok");
        }
    }

    public void addUser(User user){
        this.db.collection(collectionName).document(this.user.getUid()).set(user);
    }

}
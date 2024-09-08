package com.example.project.dao;

import com.example.project.model.Account;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountDAO extends BaseDAO{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public AccountDAO(){
        super("account");
    }

    public void addAccount(Account account){
        this.db.collection(collectionName).add(account);
    }

    // Phương thức để thêm tài khoản với UID làm document ID
    public void addAccountWithID(String uid, Account account) {
        db.collection("account").document(uid).set(account)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }
}


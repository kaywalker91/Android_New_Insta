package com.kaywalker.new_insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaywalker.new_insta.Model.UserAccount;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText et_email,et_pw;
    private Button btn_regi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Ins_Users");

        et_email = findViewById(R.id.et_email);
        et_pw = findViewById(R.id.et_pw);

        btn_regi = findViewById(R.id.btn_regi);
        btn_regi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String strEmail = et_email.getText().toString();
                String strPw = et_pw.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setUser_email(firebaseUser.getEmail());
                            account.setUser_pw(strPw);

                            databaseReference.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else{
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
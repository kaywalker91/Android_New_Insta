package com.kaywalker.new_insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText et_email,et_pw;
    private Button btn_mvRegi,btn_mvPw,btn_login;
    private ImageView signInButton;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private GoogleApiClient googleApiClient;

    private static final int REQ_SIGN_GOOGLE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance();

        signInButton = findViewById(R.id.btn_google);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Ins_Users");

        et_email = findViewById(R.id.et_email);
        et_pw = findViewById(R.id.et_pw);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strEmail = et_email.getText().toString();
                String strPw = et_pw.getText().toString();

                if(strEmail.equals("") || strPw.equals("")){
                    Toast.makeText(MainActivity.this, "공란을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }else{

                    auth.signInWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this, CenterActivity.class);
                                startActivity(intent);
                                finish();
                            } else{
                                Toast.makeText(MainActivity.this, "로그인에 실패하였습니다!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        btn_mvRegi = findViewById(R.id.btn_mvRegi);
        btn_mvRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_mvPw = findViewById(R.id.btn_mvPw);
        btn_mvPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PwActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //구글 로그인 인증을 요청 했을 때 결과값을 돌려받는 곳
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account);// 로그인 결과값을 출력하라는 메소드
            }
        }

    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){// 로그인이 성공했으면
                            Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            intent.putExtra("nickName",account.getDisplayName());
                            intent.putExtra("photoUrl",String.valueOf(account.getPhotoUrl()));
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
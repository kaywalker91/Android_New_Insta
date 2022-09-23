package com.kaywalker.new_insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaywalker.new_insta.Model.ChatDTO;

public class ChatActivity extends AppCompatActivity {

    private String CHAT_NAME;
    private String USER_NAME;

    private ImageButton chat_send;
    private ListView chat_view;
    private EditText chat_edit;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chat_view = (ListView)findViewById(R.id.chat_view);
        chat_send = (ImageButton)findViewById(R.id.chat_send);
        chat_edit = (EditText)findViewById(R.id.chat_edit);

        Intent intent = getIntent();
        CHAT_NAME = intent.getStringExtra("chatName");
        USER_NAME = intent.getStringExtra("userName");

        openChat(CHAT_NAME);

        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chat_edit.getText().toString().equals(""))
                    return;

                ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString());
                databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat);
                chat_edit.setText("");

            }
        });

        chat_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(chat_edit.getWindowToken(), 0);    //hide keyboard
                    chat_send.setFocusable(true);

                    if(chat_edit.getText().toString().equals("")){

                        Toast.makeText(ChatActivity.this, "메세지를 입력해주세요!", Toast.LENGTH_SHORT).show();

                    }else{

                        ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString());
                        databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat);
                        chat_edit.setText("");

                    }

                    return true;

                }
                return false;
            }
        });

    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter){

        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.add(chatDTO.getUserName() + " : " +chatDTO.getMessage());

    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter){

        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUserName() + " : " +chatDTO.getMessage());

    }

    private void openChat(String chatName) {

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);

        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                addMessage(snapshot, adapter);
                Log.e("LOG", "s:" +previousChildName);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                removeMessage(snapshot, adapter);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
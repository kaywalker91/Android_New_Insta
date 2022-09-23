package com.kaywalker.new_insta.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaywalker.new_insta.R;

import java.util.ArrayList;

public class Frag_Board extends Fragment {

    private View view;

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_board, container, false);

        //컴포넌트 변수에 담기
        EditText nameEdit = view.findViewById(R.id.name_edit);
        ImageButton addBtn = view.findViewById(R.id.add_btn);
        listView = view.findViewById(R.id.list_View);

        //어뎁터 초기화
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        //데이터베이스 초기화
        firebaseDatabase = FirebaseDatabase.getInstance();
        //레퍼런스 초기화
        databaseReference = firebaseDatabase.getReference().child("Data");

        //데이터 조회
        getValue();

        //데이터 등록
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //입력값 변수에 담기
                String sName = nameEdit.getText().toString();
                //키 생성
                String sKey = databaseReference.push().getKey();

                //sKey가 null이 아니면 sKey값으로 데이터를 저장한다.
                if(sKey != null){

                    databaseReference.child(sKey).child("value").setValue(sName);
                    //입력창 초기화
                    nameEdit.setText("");

                }
            } //onClick
        });//setOnClickListener

        return view;
    }

    private void getValue() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //리스트 초기화
                arrayList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    //데이터 가져오기(value 이름으로 된 값을 변수에 담는다.
                    String sValue = dataSnapshot.child("value").getValue(String.class);
                    //리스트에 변수를 담는다.
                    arrayList.add(sValue);
//                    Collections.sort(arrayList, myComparator);

                }

                //리스트뷰 어뎁터 설정
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }//getValue

}

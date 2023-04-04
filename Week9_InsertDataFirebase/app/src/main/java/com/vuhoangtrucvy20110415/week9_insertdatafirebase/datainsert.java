package com.vuhoangtrucvy20110415.week9_insertdatafirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class datainsert extends AppCompatActivity {
EditText txtname, txtage, txtphone, txtheight;
Button btnsave;
DatabaseReference reff;
Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datainsert);
        txtname=(EditText)findViewById(R.id.name);
        txtage=(EditText)findViewById(R.id.age);
        txtphone=(EditText) findViewById(R.id.phone);
        txtheight=(EditText) findViewById(R.id.height);
        btnsave=(Button) findViewById(R.id.idBtnSave);
        member=new Member();
        reff= FirebaseDatabase.getInstance("https://test-firebase-942af-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Member");
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int agea=Integer.parseInt(txtage.getText().toString().trim());
                Float hit= Float.parseFloat((txtheight.getText().toString().trim()));
                Long phn=Long.parseLong(txtphone.getText().toString().trim());
                member.setName(txtname.getText().toString().trim());
                member.setAge(agea);
                member.setHt(hit);
                member.setPh(phn);
                reff.push().setValue(member);
                Toast.makeText(datainsert.this,"data inserted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
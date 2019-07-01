package com.example.project1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddContact extends AppCompatActivity implements View.OnClickListener {
    //public MainActivity MainActivity;
    final int ADD_CONTACT_PHOTO=4;
    private Intent contact = new Intent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        Button addphoto = (Button) findViewById(R.id.addedphoto);
        final EditText addname = (EditText) findViewById(R.id.addedname);
        final EditText addnumber = (EditText) findViewById(R.id.addednumber);

        Button contactsave = (Button) findViewById(R.id.contactsave);

        addphoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ADD_CONTACT_PHOTO);
            }
        });

        contactsave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String addedname;
                String addednumber;
                addname.setInputType ( InputType. TYPE_TEXT_FLAG_NO_SUGGESTIONS );
                addnumber.setInputType ( InputType. TYPE_TEXT_FLAG_NO_SUGGESTIONS );
                addedname = addname.getText().toString();
                addednumber = addnumber.getText().toString();
                contact.putExtra("str_name", addedname);
                contact.putExtra("str_number", addednumber);
                setResult(RESULT_OK, contact);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case ADD_CONTACT_PHOTO:
                    if (resultCode == RESULT_OK && data != null) {
                        if (data.getData() != null) {
                            InputStream in = getContentResolver().openInputStream(data.getData());
                            Bitmap img = BitmapFactory.decodeStream(in);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            img.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] imageBytes = byteArrayOutputStream.toByteArray();
                            in.close();
                            contact.putExtra("str_photo", Base64.encodeToString(imageBytes, Base64.NO_WRAP));
                            Button addphoto = (Button) findViewById(R.id.addedphoto);
                            addphoto.setText("사진 선택 완료");
                        }
                    } else {
                        Toast.makeText(AddContact.this, "사진 선택을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v){

    }
}
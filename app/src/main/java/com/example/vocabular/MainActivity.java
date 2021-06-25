package com.example.vocabular;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textObjectPartA;
    TextView textObjectPartB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonPlay = (Button)findViewById(R.id.saveButton);
        buttonPlay.setOnClickListener(this);
        textObjectPartA = (TextView)findViewById(R.id.editText);
        textObjectPartB = (TextView)findViewById(R.id.textView);
    }

    @Override
    public void onClick(View view) {
        CharSequence word;
        word = textObjectPartA.getText();
        textObjectPartB.setText(word);
    }
}
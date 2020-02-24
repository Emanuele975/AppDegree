package com.example.user.appdegree.activity.utility;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View;

import com.example.user.appdegree.R;

public class MyKeyboard extends LinearLayout implements View.OnClickListener {
    private Button button1,button2,button3,button4,button5,button6,button7,button8,button9,button0
            ,Delete,Invia;

    private SparseArray<String> keyValues= new SparseArray<>();
    private InputConnection inputConnection;



    public MyKeyboard (Context context){
        this(context,null,0);
    }

    public MyKeyboard (Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public MyKeyboard (Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context,attrs);
    }


    private void init (Context context,AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.keyboard,this,true);
        Delete=findViewById(R.id.Button_delete);
        Delete.setOnClickListener(this);
        Invia=findViewById(R.id.Button_invio);
        Invia.setOnClickListener(this);
        button0= findViewById(R.id.Button0);
        button0.setOnClickListener(this);
        button1= findViewById(R.id.Button1);
        button1.setOnClickListener(this);
        button2= findViewById(R.id.Button2);
        button2.setOnClickListener(this);
        button3= findViewById(R.id.Button3);
        button3.setOnClickListener(this);
        button4= findViewById(R.id.Button4);
        button4.setOnClickListener(this);
        button5= findViewById(R.id.Button5);
        button5.setOnClickListener(this);
        button6= findViewById(R.id.Button6);
        button6.setOnClickListener(this);
        button7= findViewById(R.id.Button7);
        button7.setOnClickListener(this);
        button8= findViewById(R.id.Button8);
        button8.setOnClickListener(this);
        button9= findViewById(R.id.Button9);
        button9.setOnClickListener(this);


        keyValues.put(R.id.Button0,"0");
        keyValues.put(R.id.Button1,"1");
        keyValues.put(R.id.Button2,"2");
        keyValues.put(R.id.Button3,"3");
        keyValues.put(R.id.Button4,"4");
        keyValues.put(R.id.Button5,"5");
        keyValues.put(R.id.Button6,"6");
        keyValues.put(R.id.Button7,"7");
        keyValues.put(R.id.Button8,"8");
        keyValues.put(R.id.Button9,"9");

    }


    @Override
    public void onClick(View view) {
        if ( inputConnection==null)
            return;
        if(view.getId()== R.id.Button_delete){
            CharSequence selectedText=inputConnection.getSelectedText(0);

            if(TextUtils.isEmpty(selectedText)){
                inputConnection.deleteSurroundingText(1,0);
            } else {
                inputConnection.commitText("",1);
            }
        } else{

            String value = keyValues.get(view.getId());
            inputConnection.commitText(value,1);

        }

    }

    public void setInputConnection(InputConnection ic){
        inputConnection=ic;
    }
}

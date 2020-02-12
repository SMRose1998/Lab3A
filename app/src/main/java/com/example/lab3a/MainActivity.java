package com.example.lab3a;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    String currentInput= "";
    ArrayList<String> previousInputs;
    ArrayList<Operator> operators;

    //Main View for current input and output
    TextView viewMain;

    private enum Operator{
        add("add"),
        sub("subtract"),
        mult("multiply"),
        div("divide");

        String str;
        Operator(String msg){ str = msg;}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        previousInputs = new ArrayList<String>();
        operators = new ArrayList<Operator>();

        viewMain = findViewById(R.id.text_main);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickValue(View v){
        Button self = (Button) v;
        String input = self.getText().toString();
        currentInput += input;
        updateMainInput();
    }

    public void onClickAdd(View v){
        operators.add(Operator.add);
        switchInputs();
    }

    public void onClickSub(View v){
        operators.add(Operator.sub);
        switchInputs();
    }

    public void onClickMult(View v){
        operators.add(Operator.mult);
        switchInputs();
    }

    public void onClickDiv(View v){
        operators.add(Operator.div);
        switchInputs();
    }

    public void onClickEquals(View v){
        switchInputs();
        Iterator<Operator> itterateOp = operators.iterator();
        Iterator<String> itterateInputs = previousInputs.iterator();



        //PEMDAS
        while(itterateOp.hasNext()){

        }
    }

    private void switchInputs(){
        previousInputs.add(currentInput);
        currentInput = "";
    }

    private void updateMainInput(){
        viewMain.setText(currentInput);
    }
}

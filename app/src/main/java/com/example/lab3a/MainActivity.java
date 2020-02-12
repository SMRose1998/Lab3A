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

import java.math.BigDecimal;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    String currentInput;
    String previousInputString;
    ArrayList<String> previousInputs;
    ArrayList<Operator> operators;

    //Main View for current input and output
    TextView viewMain;

    //View for previous inputs
    TextView viewPrevious;

    //boolean that checks for divide by zero error
    boolean divideByZero, nextClickClear;

    private enum Operator {
        add("+"),
        sub("-"),
        mult("x"),
        div("/");

        String string;

        Operator(String str) {
            string = str;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reset();

        viewMain = findViewById(R.id.text_main);
        viewPrevious = findViewById(R.id.text_previous);


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

    public void onClickClear(View v){
        clearView();
        reset();
    }

    public void onClickValue(View v) {
        if(nextClickClear)
            clearView();
        Button self = (Button) v;
        String input = self.getText().toString();
        currentInput += input;
        updateMainInput();
        updatePreviousInputText(input);
    }



    public void onClickAdd(View v) {
        onOperatorClick(Operator.add);
    }

    public void onClickSub(View v) {
        onOperatorClick(Operator.sub);
    }

    public void onClickMult(View v) {
        onOperatorClick(Operator.mult);
    }

    public void onClickDiv(View v) {
        onOperatorClick(Operator.div);
    }

    private void onOperatorClick(Operator op){
        //Checks that there is some number to operate on
        //Otherwise, operate on the previous result
        if(nextClickClear) {
            String inpt = viewMain.getText().toString();
            currentInput = inpt;
            previousInputString += inpt;
        }

        //Add the clicked operator to the list
        operators.add(op);

        // Push current input to list
        switchInputs();
    }

    public void onClickEquals(View v) {
        previousInputs.add(currentInput);
        previousInputString+=" =";

        operate(Operator.mult);
        operate(Operator.div);
        operate(Operator.add);
        operate(Operator.sub);

        String result;
        if(!divideByZero) {
            result = previousInputs.get(0);
        }else{
            result = "ERROR: Cannot Divide By Zero";
        }

        viewMain.setText(result);
        viewPrevious.setText(previousInputString);

        reset();

        // Set nextClickClear to clear display on next click
        nextClickClear = true;


    }

    private void operate(Operator operator){
        //Multiplication
        while (operators.contains(operator)) {

            //Get index of operator
            int index = operators.indexOf(operator);

            //Get the firt and second numbers
            BigDecimal firstNumber = new BigDecimal(previousInputs.get(index));
            BigDecimal secondNumber = new BigDecimal(previousInputs.get(index+1));

            //Do Operation
            BigDecimal result = BigDecimal.ZERO;
            switch(operator){
                case mult:
                    result = firstNumber.multiply(secondNumber);
                    break;
                case div:
                    if(secondNumber==BigDecimal.ZERO){
                        divideByZero = true;
                    }else {
                        result = firstNumber.divide(secondNumber);
                    }
                    break;
                case sub:
                    result = firstNumber.subtract(secondNumber);
                    break;
                case add:
                    result = firstNumber.add(secondNumber);
                    break;

            }


            //Set the index to the result
            previousInputs.set(index, result.toString());
            //Remove the second number
            previousInputs.remove(index + 1);
            // Remove the operator
            operators.remove(index);
        }
    }

    private void switchInputs() {

        //Set the input string to be viewed
        previousInputString += " "+operators.get(operators.size() - 1).string + " ";

        //Set the previous input and clear the current input
        previousInputs.add(currentInput);
        currentInput = "";

        //Update previous string view
        viewPrevious.setText(previousInputString);
    }

    private void updatePreviousInputText(String number){
        previousInputString+= number;
        viewPrevious.setText(previousInputString);
    }

    private void updateMainInput() {
        viewMain.setText(currentInput);
    }

    private void reset(){
        previousInputs = new ArrayList<String>();
        operators = new ArrayList<Operator>();
        previousInputString = "";
        currentInput = "";
        divideByZero = false;
    }

    private void clearView(){
        viewMain.setText("");
        viewPrevious.setText("");
        nextClickClear = false;
    }
}
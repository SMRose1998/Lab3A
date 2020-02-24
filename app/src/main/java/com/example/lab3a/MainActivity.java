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
import java.math.MathContext;
import java.math.RoundingMode;
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
        div("/"),
        mod("%");

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

        // Initialize
        reset();

        // Get the main outputs
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

        // Limit number of digits to 10
        if(currentInput.length()<10 ||
          (currentInput.contains(".") && currentInput.length()<11)) {
            if (nextClickClear)
                clearView();
            Button self = (Button) v;
            String input = self.getText().toString();
            if(input.equals(".")&&currentInput.contains(".")){
                return;
            }
            currentInput += input;
            updateMainInput();
            updatePreviousInputText(input);
        }
    }

    public void onClickSqrt(View v){

        if(nextClickClear) {
            //resultAsCurrentInput();
            return;
        }

        //Check for negative
        if(currentInput.contains("-")){
            clearView();
            reset();
            viewMain.setText("ERROR: Negative Square Root");
            return;
        }

        //Calculate the Sqrt
        String root = String.valueOf(Math.sqrt(Double.valueOf(currentInput)));


        if(root.contains(".")) {
            //Cut down sqrt to 5 decimal places
            if (root.substring(root.indexOf("."),root.length()).length() > 5) {
                root = root.substring(0, root.indexOf(".")+6);
            }

            //Cut off extra 0 tacked on to the end of int result
            if (root.substring(root.indexOf("."),root.length()).equals(".0")){
                root = root.substring(0,root.length()-2);
            }
        }

        //Update input String
        previousInputString = previousInputString.substring(
                0, previousInputString.length()-currentInput.length());

        //Add sqrt number
        previousInputString+="√"+currentInput;

        //Set the previous input and clear the current input
        previousInputs.add(root);
        currentInput = "";


        //Update previous string view
        viewPrevious.setText(previousInputString);
        viewMain.setText("");


    }

    /*public void onClickModOLD(View v){
        if(nextClickClear) {
            resultAsCurrentInput();
        }

        //Devisor
        String n = previousInputs.get(previousInputs.size()-1);

        //Calculte the mod
        BigDecimal firstNumber = new BigDecimal(n);
        BigDecimal secondNumber = new BigDecimal(currentInput);
        String result = firstNumber.remainder(secondNumber).toString();

        //Set the previous input string
        previousInputString.substring(0,previousInputString.length()-n.length());
        previousInputString+=result;

        //Remove the last number from previous inputs
        previousInputs.remove(previousInputs.size()-1);

        //put in the new last input
        previousInputs.add(result);

        //Update previous string view
        currentInput = "";
        viewPrevious.setText(previousInputString);
        viewMain.setText("");

    }*/

    public void onClickNeg(View v){

        // Makes sure there is a number to make negative
        // If empty, then return
        if(currentInput.equals("")){
            return;
        }

        //Checks if the result is displayed
        if(nextClickClear) {
            resultAsCurrentInput();
        }else{
            // Make it so that the code goes back and deletes the same number of chars (minus one for the sub sign) as the current input
            // Update the Substring to show the minus sign
            previousInputString = previousInputString.substring(
                    0, previousInputString.length()-currentInput.length()
            );
        }

        //Check if number is an int to avoid annoying decimals
        if (currentInput.contains(".")){
            float current = Float.valueOf(currentInput);
            current*= -1;
            currentInput = String.valueOf(current);
        }else{
            int current = Integer.valueOf(currentInput);
            current*= -1;
            currentInput = String.valueOf(current);
        }
        updateMainInput();


        updatePreviousInputText(currentInput);
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

    public void onClickMod(View v){
        onOperatorClick(Operator.mod);
    }

    private void onOperatorClick(Operator op){

        // Checks that some input has been entered
        if(currentInput.equals("") && !nextClickClear){
            return;
        }

        //Checks that there is some number to operate on
        //Otherwise, operate on the previous result
        if(nextClickClear) {
            resultAsCurrentInput();
            previousInputString += currentInput;
        }

        //Add the clicked operator to the list
        operators.add(op);

        // Push current input to list
        switchInputs();
    }

    private void resultAsCurrentInput(){
        String inpt = viewMain.getText().toString();
        currentInput = inpt;
    }
    public void onClickEquals(View v) {

        // Prevent null operation
        if(currentInput.equals("") && operators.size()>0){
            return;
        }

        if(nextClickClear){
            return;
        }

        addToPreviousInputs(currentInput);
        previousInputString+=" =";

        operate(Operator.mult);
        operate(Operator.div);
        operate(Operator.mod);
        operate(Operator.add);
        operate(Operator.sub);

        String result;
        if(!divideByZero) {
            result = previousInputs.get(0);
        }else{
            result = "ERROR: Divided By Zero";
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

            //Get the first and second numbers
            BigDecimal firstNumber = new BigDecimal(previousInputs.get(index));
            BigDecimal secondNumber = new BigDecimal(previousInputs.get(index+1));

            //Do Operation
            BigDecimal result = BigDecimal.ZERO;
            switch(operator){
                case mult:
                    result = firstNumber.multiply(secondNumber);
                    break;
                case div:
                    if(secondNumber.equals(BigDecimal.ZERO)){
                        divideByZero = true;
                    }else {
                        result = firstNumber.divide(secondNumber, 5, RoundingMode.HALF_UP);
                    }
                    break;
                case sub:
                    result = firstNumber.subtract(secondNumber);
                    break;
                case add:
                    result = firstNumber.add(secondNumber);
                    break;
                case mod:
                    result = firstNumber.remainder(secondNumber);
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
        addToPreviousInputs(currentInput);
        currentInput = "";

        //Update previous string view
        viewPrevious.setText(previousInputString);
    }

    private void addToPreviousInputs(String input){
        if(!currentInput.equals("")){
            previousInputs.add(currentInput);
        }
    }

    private void updatePreviousInputText(String number){

        //Check for negative sign and subtraction
        //If subtracting by a negative add in parentheses for clarity
        if(!number.contains(".") &&
           !number.contains("√")  &&
           Double.valueOf(number)<0 &&
           operators.size()>0 &&
           operators.get(operators.size()-1).equals(Operator.sub)){
                    previousInputString+= "("+number+")";
        }else{
            previousInputString+= number;
        }
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
package app.ClosedLoopDev.HCF.heroescrossfit_2.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class DisplayMyPRsActivity extends Activity {

    boolean addNewPRBool;
    boolean tryingToChangeToAddNewPR;
    String nameToString;
    String nameToStringPost;
    String valueToString;
    String valueToStringPost;
    String buttonPRString;
    String PRString;
    ArrayList<String> PRArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewPRBool = true;
        setContentView(R.layout.activity_display_my_prs);
        try {
            readFromFile("Saved_PRs_Doc_Plaything");
        } catch (IOException e) {
            e.printStackTrace();
        }
        addButtonsToView(PRArray);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_my_prs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPRDetail(View v){
        Button currentButton = (Button) v;
        buttonPRString = currentButton.getText().toString();
        nameToStringPost = buttonPRString.substring(0, buttonPRString.indexOf('\n'));
        valueToStringPost = buttonPRString.substring(buttonPRString.indexOf('\n')+1, buttonPRString.length()).trim();
    }

    public void saveInfo(String WOD, String filename){
        if(WOD.isEmpty()){
            WOD = "<empty>";
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getFilesDir()+filename, true));
            writer.write(WOD);
            writer.write("\n"+"endOfPR");
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile(String fn) throws IOException {
        FileReader fileReader = new FileReader(getFilesDir() + fn);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String receiveString = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((receiveString = bufferedReader.readLine()) != null) {
            if(receiveString.equals("endOfPR")){
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                PRArray.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
            else{
                stringBuilder.append(receiveString +"\n");
            }
        }
    }

    public void addButtonsToView(ArrayList<String> PRsToAdd){
        for(int o=0;o<PRsToAdd.size();o++){
            Button newPRSave;
            SpannableStringBuilder spanSinBuild = new SpannableStringBuilder();
            nameToString = PRsToAdd.get(o).substring(0,PRsToAdd.get(o).indexOf('\n'));
            SpannableString nameTxt = new SpannableString(nameToString);
            nameTxt.setSpan(new AbsoluteSizeSpan(22, true), 0, nameTxt.length(), 0);
            spanSinBuild.append(nameTxt);
            valueToString = PRsToAdd.get(o).substring(PRsToAdd.get(o).indexOf('\n'), PRsToAdd.get(o).length());
            SpannableString valueTxt = new SpannableString(valueToString);
//            conditionString.setSpan(new AbsoluteSizeSpan(14, true), 0, conditionString.length(), 0);
//            spanSinBuild.append(conditionString);
            valueTxt.setSpan(new AbsoluteSizeSpan(14, true), 0, valueTxt.length(), 0);
            spanSinBuild.append(valueTxt);
            newPRSave = (Button) getLayoutInflater().inflate(R.layout.bbt_for_prs, null);
            newPRSave.setText(spanSinBuild, TextView.BufferType.SPANNABLE);
            LinearLayout addedPRsRelativeLayout = (LinearLayout) findViewById(R.id.linearLayoutAfterAddingPRs);
            addedPRsRelativeLayout.addView(newPRSave);
        }
    }

    public void onAddPR(View v){
        Button addNewPR = (Button) v;
        if(addNewPR.getText().toString().equals("Save")) {
            EditText PRNameEditText = (EditText) findViewById(R.id.PRNameEditText);
            EditText PRValueEditText = (EditText) findViewById(R.id.PRValueEditText);
            TextView PRNameViewText = (TextView) findViewById(R.id.PRNameString);
            TextView PRValueViewText = (TextView) findViewById(R.id.PRValueString);
            nameToString = PRNameEditText.getText().toString();
            valueToString = PRValueEditText.getText().toString();
            PRString = nameToString+"\n"+valueToString;
            saveInfo(PRString,"Saved_PRs_Doc_Plaything");
            try {
                readFromFile("Saved_PRs_Doc_Plaything");
            } catch (IOException e) {
                e.printStackTrace();
            }
            addButtonsToView(PRArray);
            PRNameViewText.setVisibility(View.GONE);
            PRValueViewText.setVisibility(View.GONE);
            PRNameEditText.setVisibility(View.GONE);
            PRNameEditText.setText("");
            PRValueEditText.setVisibility(View.GONE);
            PRValueEditText.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
            addNewPR.setText("Add New PR");
            tryingToChangeToAddNewPR = true;
        }
        if(addNewPRBool) {
            TextView PRNameViewText = (TextView) findViewById(R.id.PRNameString);
            TextView PRValueViewText = (TextView) findViewById(R.id.PRValueString);
            EditText PRNameEditText = (EditText) findViewById(R.id.PRNameEditText);
            EditText PRValueEditText = (EditText) findViewById(R.id.PRValueEditText);
            PRNameViewText.setVisibility(View.VISIBLE);
            PRValueViewText.setVisibility(View.VISIBLE);
            PRNameEditText.setVisibility(View.VISIBLE);
            PRValueEditText.setVisibility(View.VISIBLE);
            PRNameEditText.setFocusableInTouchMode(true);
            PRNameEditText.requestFocus();
            PRNameEditText.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            addNewPR.setText("Save");
            addNewPRBool = false;
        }
        if(tryingToChangeToAddNewPR == true){
            tryingToChangeToAddNewPR = false;
            addNewPRBool = true;
        }
    }
}

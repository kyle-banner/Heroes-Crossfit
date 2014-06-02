package app.ClosedLoopDev.HCF.heroescrossfit_2.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class DisplaySavedWODDetailActivity extends Activity {

    String fullWODText, savedDate, condStrToStr;
    TextView WODDetailTextView;
    String strBeforeEdit, strAfterEdit;
    ArrayList<String> temp_Array;
    ArrayList<ArrayList<String>> allStrArray = new ArrayList<ArrayList<String>>();
    int changeStringInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_saved_woddetail);
        fullWODText = getIntent().getExtras().getString("WODFullTextKey");
        Bundle extras = getIntent().getExtras();
        allStrArray = (ArrayList<ArrayList<String>>) extras.getSerializable("AllStringsArray");
//        Collections.reverse(allStrArray);
        WODDetailTextView = (TextView) findViewById(R.id.WODDetailText);
        WODDetailTextView.setText(fullWODText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_saved_woddetail, menu);
        return true;
    }

    public void checkArray(String checkString){
        for(int y=0;y<allStrArray.size();y++){
            ArrayList<String> allStrArrayArrayComponent = allStrArray.get(y);
            if(allStrArrayArrayComponent.get(1).equals(checkString)){
                changeStringInt = y;
                savedDate = allStrArrayArrayComponent.get(0);
                condStrToStr = allStrArrayArrayComponent.get(2);
                temp_Array = new ArrayList<String>();
                temp_Array.add(0, savedDate);
                temp_Array.add(1, strAfterEdit);
                temp_Array.add(2, condStrToStr);
                allStrArray.set(changeStringInt, temp_Array);
                break;
            }
        }
//        allIdentifiersString.add(0, savedDate);
//        allIdentifiersString.add(1, stringBuilder.toString());
//        allIdentifiersString.add(2, condStrToStr);
//        ArrayList<String> newIdentifiersArrayList = new ArrayList<String>(allIdentifiersString);
//        savedWODList.add(mapCounter, newIdentifiersArrayList);

        String bullshit = "bs";
    }

    public void cancelGoesBackToSavedWODs(View v){
        Intent intentForSavedWODsTheSecond = new Intent(this, DisplaySavedWODsActivity.class);
        startActivity(intentForSavedWODsTheSecond);
    }

    //USE THIS CODE TO DELETE FROM ARRAY AND FILE!!
    public void onEdit(View v){
        Button editButton = (Button) v;
        if(editButton.getText().toString().equals("Save")) {
            EditText editWODDetail = (EditText) findViewById(R.id.WODDetailTextEdit);
            strAfterEdit = editWODDetail.getText().toString();
            checkArray(strBeforeEdit);
            File file = new File(getFilesDir()+"WODs_Saved_Doc_Plaything");
            boolean deleted = file.delete();
            if(deleted){
                for (int q = 0; q < allStrArray.size(); q++) {
                    BufferedWriter writer = null;
                    try {
                        writer = new BufferedWriter(new FileWriter(getFilesDir() + "WODs_Saved_Doc_Plaything", true));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ArrayList<String> temp_decl = allStrArray.get(q);
                    if (writer != null) {
                        try {
                            writer.write(temp_decl.get(1));
                            writer.write("\n" + "endOfWOD");
                            writer.newLine();
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            Intent intentForSavedWODs = new Intent(this, DisplaySavedWODsActivity.class);
            startActivity(intentForSavedWODs);

        }
        if(editButton.getText().toString().equals("Edit")) {
            EditText editWODDetail = (EditText) findViewById(R.id.WODDetailTextEdit);
            strBeforeEdit = fullWODText;
            editWODDetail.setVisibility(View.VISIBLE);
            editWODDetail.setText(fullWODText);
            editWODDetail.setFocusableInTouchMode(true);
            editWODDetail.requestFocus();
            editWODDetail.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            editButton.setText("Save");
            WODDetailTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        switch(item.getItemId()){
//            case android.R.id.home:
//                Intent intentForSavedWODs = new Intent(this, DisplaySavedWODsActivity.class);
//////            intentForSavedWODs.putExtra("comingFromMainScreen", false);
//                startActivity(intentForSavedWODs);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}

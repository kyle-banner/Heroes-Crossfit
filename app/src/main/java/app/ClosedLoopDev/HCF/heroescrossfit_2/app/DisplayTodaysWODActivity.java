package app.ClosedLoopDev.HCF.heroescrossfit_2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


public class DisplayTodaysWODActivity extends Activity {

    String todaysWODString;
    String savedWOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_todays_wod);
        todaysWODString = getIntent().getExtras().getString("WODTextKey");
        TextView displayWOD = (TextView) findViewById(R.id.todaysWODText);
        displayWOD.setText(todaysWODString);
    }


    public void inputResults(View v){
        EditText et = (EditText) v;
        et.setFocusableInTouchMode(true);
        et.setFocusable(true);
        et.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_todays_wod, menu);
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

    public void onSave(View view) {
        EditText WODtext = (EditText) findViewById(R.id.resultText);
        savedWOD = todaysWODString + "\n" + "Result: " + WODtext.getText().toString();
//        while ((receiveString = bufferedReader.readLine()) != null) {
//            if(receiveString.contains("Conditioning")){
//                conditioningLine = conditioningIterator;
//            }
//
//            if(conditioningLine != 0) {
//                if (conditioningIterator > conditioningLine) {
//                    if (receiveString.equals("endOfWOD")) {
//
//                    } else {
//                        conditioningString.append(receiveString + ", ");
//                    }
//                }
//            }
//            conditioningIterator++;
//    }
        Intent saveTodaysWOD = new Intent(this, DisplaySavedWODsActivity.class);
        saveTodaysWOD.putExtra("WODTextKey", savedWOD);
        saveTodaysWOD.putExtra("WODDateKey", savedWOD.substring(0,savedWOD.indexOf('\n')));
        saveTodaysWOD.putExtra("cameFromTodaysWOD", true);
        startActivity(saveTodaysWOD);
//        }
    }
}

package app.ClosedLoopDev.HCF.heroescrossfit_2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class DisplayTypeInWODActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_type_in_wod);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_type_in_wod, menu);
        return true;
    }

    public void onSaveTypedIn(View v){
        EditText dateStringResultET = (EditText) findViewById(R.id.dateTextResult);
        String dateString = dateStringResultET.getText().toString();
        EditText WODStringResultET = (EditText) findViewById(R.id.WODTextResult);
        String WODString = WODStringResultET.getText().toString();
        EditText resultStringResultET = (EditText) findViewById(R.id.resultTextResult);
        String resultString = resultStringResultET.getText().toString();
        Intent intentToSaveTypedInWOD = new Intent(this, DisplaySavedWODsActivity.class);
//                Intent intentForTypeInWOD = new Intent(this, DisplayTypeInWODActivity.class);
        intentToSaveTypedInWOD.putExtra("cameFromTypedInWOD", true);
        intentToSaveTypedInWOD.putExtra("dateString", dateString);
        intentToSaveTypedInWOD.putExtra("WODString", WODString);
        intentToSaveTypedInWOD.putExtra("resultString", resultString);
        startActivity(intentToSaveTypedInWOD);
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
}

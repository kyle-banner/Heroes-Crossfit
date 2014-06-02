package app.ClosedLoopDev.HCF.heroescrossfit_2.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    String todaysWODText = "";
    String createdString = "";
    String dateString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        setProgressBarIndeterminateVisibility(true);
        new posted().execute();
    }

    public class posted extends AsyncTask<Void, Void, Void> {

        String brClearAdjust = "";
        String conditioningString = "";
        Boolean notCurrentDate = false;
        String dayPostedStr = "";
        public void onPreExecute() {
            super.onPreExecute();
        }

        public Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect("http://www.heroescrossfit.com").get();
                String page2str = document.toString();
                Document docParsed = Jsoup.parse(page2str);
                docParsed.outputSettings(new Document.OutputSettings().prettyPrint(false));
                Element todaysWODFirst = docParsed.select("div[class=jsn-article]").first();
                brClearAdjust = todaysWODFirst.toString();
                brClearAdjust = brClearAdjust.replaceAll("<br clear=\"none\" />", "\n");
                todaysWODText = Jsoup.clean(brClearAdjust, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
                todaysWODText = todaysWODText.replace("&nbsp;", "\n");
                todaysWODText = todaysWODText.replaceAll("  ", "");
                todaysWODText = todaysWODText.replaceAll("\n ","\n");
                todaysWODText = todaysWODText.replaceAll(" \n","\n");
                todaysWODText = todaysWODText.replaceAll("&quot;", "\"");
                todaysWODText = todaysWODText.replaceAll("Written by Lance Looper","");
                todaysWODText = todaysWODText.replaceAll("Created on ", "Last Updated: ");
                StringBuilder todaysWODTextSB = new StringBuilder();
                todaysWODTextSB.append(todaysWODText);
                int lengthOfStrBuilder = todaysWODTextSB.length();
                for(int i=0;i<lengthOfStrBuilder;i++){
                    if(todaysWODTextSB.charAt(i)=='\n'){
                        if(i>1) {
                            if (todaysWODTextSB.charAt(i - 1) == '\n') {
                                todaysWODTextSB.deleteCharAt(i-1);
                                lengthOfStrBuilder--;
                                i--;
                            }
                        }
                    }
                }
                int createdStringStart = todaysWODTextSB.indexOf("Last Updated: ");
                int createdStringEnd = 0;
                for(int j=createdStringStart;j<todaysWODTextSB.length();j++){
                    if(todaysWODTextSB.charAt(j)=='\n'){
                        createdStringEnd = j;
                        break;
                    }
                }
                createdString = todaysWODTextSB.substring(createdStringStart, createdStringEnd);
                todaysWODText = todaysWODTextSB.toString();
                todaysWODText = todaysWODText.replaceAll(createdString, "");
                todaysWODText = todaysWODText.replaceAll("\nConditioning", "\n\nConditioning");
                todaysWODText = todaysWODText.replaceAll("\n\n\n","\n\n");
                todaysWODText = todaysWODText.trim();
                conditioningString = createShortConditioningText(todaysWODText);
                dateString = todaysWODText.substring(0,todaysWODText.indexOf('\n'));
                dateString = createShortDateText(dateString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String createShortConditioningText(String fullText){
            String retStr = "";
            String[] condStrArray;
            int conditioningIndex = 0;

            condStrArray = fullText.split("\n");
            for(int y=0;y<condStrArray.length;y++){
                if(condStrArray[y].contains("Conditioning")){
                    conditioningIndex = y;
                }
                if(conditioningIndex != 0) {
                    if (y > conditioningIndex){
                        retStr += condStrArray[y] + ", ";
                    }
                }
            }

            retStr=retStr.trim();

            if(retStr.length()>3){
                if(retStr.charAt(retStr.length()-1) == ','){
                    retStr = retStr.substring(0,retStr.length()-1);
                }
            }

            return retStr;
        }

        public String createShortDateText(String longDateText){
            String dayStr = "";
            String monthStr = "";
            String monthStrCurrent = "";
            String dayStrCurrent = "";
            String retStr = "";
            DateFormat dateFormat = new SimpleDateFormat("M.d");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
            if(longDateText.contains("Sunday")){
                retStr = "Sun";
                dayPostedStr = "Sunday's ";
            }
            else if(longDateText.contains("Monday")){
                retStr = "Mon";
                dayPostedStr = "Monday's ";
            }
            else if(longDateText.contains("Tuesday")){
                retStr = "Tue";
                dayPostedStr = "Tuesday's ";
            }
            else if(longDateText.contains("Wednesday")){
                retStr = "Wed";
                dayPostedStr = "Wednesday's ";
            }
            else if(longDateText.contains("Thursday")){
                retStr = "Thu";
                dayPostedStr = "Thursday's ";
            }
            else if(longDateText.contains("Friday")){
                retStr = "Fri";
                dayPostedStr = "Friday's ";
            }
            else if(longDateText.contains("Saturday")){
                retStr = "Sat";
                dayPostedStr = "Saturday's ";
            }
            for(int h=0;h<longDateText.length();h++){
                if(longDateText.charAt(h)=='.'){
                    monthStr = longDateText.substring(h-2, h).trim();
                    dayStr = longDateText.substring(h+1, longDateText.length()).trim();
                }
            }
            for(int h=0;h<currentDate.length();h++){
                if(currentDate.charAt(h)=='.'){
                    if(h-2>0) {
                        monthStrCurrent = currentDate.substring(h - 2, h).trim();
                    }
                    else{
                        monthStrCurrent = currentDate.substring(h - 1, h).trim();
                    }
                    dayStrCurrent = currentDate.substring(h+1, currentDate.length()).trim();
                }
            }
            if(!monthStr.equals(monthStrCurrent)){
                notCurrentDate = true;
            }
            if(!dayStr.equals(dayStrCurrent)){
                notCurrentDate = true;
            }
            if(monthStr.equals("1")){
                retStr += "\nJan";
            }
            else if(monthStr.equals("2")){
                retStr += "\nFed";
            }
            else if(monthStr.equals("3")){
                retStr += "\nMar";
            }
            else if(monthStr.equals("4")){
                retStr += "\nApr";
            }
            else if(monthStr.equals("5")){
                retStr += "\nMay";
            }
            else if(monthStr.equals("6")){
                retStr += "\nJun";
            }
            else if(monthStr.equals("7")){
                retStr += "\nJul";
            }
            else if(monthStr.equals("8")){
                retStr += "\nAug";
            }
            else if(monthStr.equals("9")){
                retStr += "\nSep";
            }
            else if(monthStr.equals("10")){
                retStr += "\nOct";
            }
            else if(monthStr.equals("11")){
                retStr += "\nNov";
            }
            else if(monthStr.equals("12")){
                retStr += "\nDec";
            }
            retStr += "\n"+dayStr;
            return retStr;
        }

        public void onPostExecute(Void result) {
            if(createdString.isEmpty()){
                new posted().execute();
            }
            else{
                SpannableStringBuilder spanSinBuild = new SpannableStringBuilder();
                if(notCurrentDate == true){
                    SpannableString WODtxt = new SpannableString(dayPostedStr+"WOD");
                    WODtxt.setSpan(new AbsoluteSizeSpan(20, true),0,WODtxt.length(),0);
                    spanSinBuild.append(WODtxt);
                }
                else {
                    SpannableString WODtxt = new SpannableString("Today's WOD");
                    WODtxt.setSpan(new AbsoluteSizeSpan(20, true),0,WODtxt.length(),0);
                    spanSinBuild.append(WODtxt);
                }

                SpannableString created = new SpannableString("\n"+conditioningString);
                created.setSpan(new AbsoluteSizeSpan(12, true), 0,created.length(),0);
                spanSinBuild.append(created);

                Button todaysWODButton = (Button)findViewById(R.id.todaysWOD);
                todaysWODButton.setText(spanSinBuild, TextView.BufferType.SPANNABLE);
                LinearLayout todaysWODLL = (LinearLayout) findViewById(R.id.todaysWOD_main_activity_LL);
                Button dateButton = (Button) todaysWODLL.findViewById(R.id.date_button_main_activity);
                if(notCurrentDate == true){
                    dateButton.setText(dateString);
                    dateButton.setBackgroundColor(Color.YELLOW);
                }
                else {
                    dateButton.setText(dateString);
                }
                dateButton.setVisibility(View.VISIBLE);
//                todaysWODButton.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_launcher, 0, 0, 0);
                setProgressBarIndeterminateVisibility(false);
            }
        }
    }

    public void onMyPRClick(View v){
        Intent intentForPRActivity = new Intent(this, DisplayMyPRsActivity.class);
        startActivity(intentForPRActivity);
    }

    public void todaysWODClicked(View v){
        if(!createdString.isEmpty()){
            Intent intentForTodaysWOD = new Intent(this, DisplayTodaysWODActivity.class);
            intentForTodaysWOD.putExtra("WODTextKey", todaysWODText);
            startActivity(intentForTodaysWOD);
        }
    }

    public void onTypeInWOD(View v){
        Intent intentForTypeInWOD = new Intent(this, DisplayTypeInWODActivity.class);
        startActivity(intentForTypeInWOD);
    }

    public void onMySavedWODs(View v){
        Intent intentForSavedWODs = new Intent(this, DisplaySavedWODsActivity.class);
        intentForSavedWODs.putExtra("comingFromMainScreen", true);
        startActivity(intentForSavedWODs);
    }
}

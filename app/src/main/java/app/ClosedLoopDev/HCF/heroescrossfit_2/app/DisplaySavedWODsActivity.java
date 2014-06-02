package app.ClosedLoopDev.HCF.heroescrossfit_2.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class DisplaySavedWODsActivity extends Activity {

    String savedWODTextWithResult = "";
    String savedDate = "";
    String saveDateTI = "";
    String saveWODTI = "";
    String saveResultsTI = "";
    String allStrTI = "";
    boolean cameFromTodaysWOD = false;
    boolean cameFromTypedInWOD = false;
    boolean cameFromMainScreen = false;
    ArrayList<String> allIdentifiersString = new ArrayList<String>();
    ArrayList<ArrayList<String>> savedWODList = new ArrayList<ArrayList<String>>();
    Integer mapCounter = 0;
    LinearLayout savedWODView;
//    private static final String DEBUG_TAG = "Gestures";
//    private static final int SWIPE_MIN_DISTANCE = 120;
//    private static final int SWIPE_MAX_OFF_PATH = 250;
//    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
//    private GestureDetector gestureDetector;
//    View.OnTouchListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Gesture detection
//        gestureDetector = new GestureDetector(this, new MyGestureDetector());
//        gestureListener = new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureDetector.onTouchEvent(event);
//            }
//        };
        setContentView(R.layout.activity_display_saved_wods);
//        mDetector = new GestureDetectorCompat(this, this);
        savedWODView = (LinearLayout) findViewById(R.id.savedWODSLinearLayout);
        if (getIntent().getExtras() != null) {
            cameFromMainScreen = getIntent().getExtras().getBoolean("comingFromMainScreen");
            if (cameFromMainScreen) {
                try {
                    readFromFile("WODs_Saved_Doc_Plaything");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cameFromMainScreen = false;
            cameFromTodaysWOD = getIntent().getExtras().getBoolean("cameFromTodaysWOD");
            if (cameFromTodaysWOD) {
                savedWODTextWithResult = getIntent().getExtras().getString("WODTextKey");
                savedDate = getIntent().getExtras().getString("WODDateKey");
                saveInfo(savedWODTextWithResult, "WODs_Saved_Doc_Plaything");
                try {
                    readFromFile("WODs_Saved_Doc_Plaything");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cameFromTodaysWOD = false;
            cameFromTypedInWOD = getIntent().getExtras().getBoolean("cameFromTypedInWOD");
            if(cameFromTypedInWOD){
                saveDateTI = getIntent().getExtras().getString("dateString");
                saveWODTI = getIntent().getExtras().getString("WODString");
                saveResultsTI = getIntent().getExtras().getString("resultString");
                allStrTI = saveDateTI+"\n"+saveWODTI+"\n"+saveResultsTI;
                saveInfo(allStrTI, "WODs_Saved_Doc_Plaything");
                try {
                    readFromFile("WODs_Saved_Doc_Plaything");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cameFromTypedInWOD = false;
        } else {
            try {
                readFromFile("WODs_Saved_Doc_Plaything");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    public void saveInfo(String WOD, String filename) {
        if (WOD.isEmpty()) {
            WOD = "<empty>";
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getFilesDir() + filename, true));
            writer.write(WOD);
            writer.write("\n" + "endOfWOD");
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int previousArrayLength;

    private void readFromFile(String fn) throws IOException {
        FileReader fileReader = new FileReader(getFilesDir() + fn);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String receiveString = "";
        int conditioningIterator = 0;
        int conditioningLine = 0;
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder conditioningString = new StringBuilder();
        String condStrToStr;

        while ((receiveString = bufferedReader.readLine()) != null) {
            if (stringBuilder.length() == 0) {
                savedDate = receiveString;
            }
            if (receiveString.equals("endOfWOD")) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                condStrToStr = "";
                if (conditioningString.length() != 0) {
                    if (conditioningString.charAt(conditioningString.length() - 2) == ',') {
                        condStrToStr = conditioningString.deleteCharAt(conditioningString.length() - 2).toString();
                    }
                    condStrToStr = condStrToStr.trim();
                    condStrToStr = condStrToStr.replaceAll(", , ", ", ");
                } else {
                    String tempStr = stringBuilder.toString();
                    if (tempStr.contains(savedDate)) {
                        tempStr = tempStr.replace(savedDate, "");
                    }
                    tempStr = tempStr.replace("\n", ", ");
                    tempStr = tempStr.replace(", , ", ", ");
                    condStrToStr = tempStr;
                    conditioningString.append(condStrToStr);
                    if (condStrToStr.length() > 3) {
                        if (conditioningString.charAt(conditioningString.length() - 2) == ',') {
                            condStrToStr = conditioningString.deleteCharAt(conditioningString.length() - 2).toString(); //problem line
                        }
                        if(conditioningString.charAt(0) == ','){
                            condStrToStr = conditioningString.deleteCharAt(0).toString(); //problem line
                        }
                    }
                    condStrToStr = condStrToStr.trim();
                    condStrToStr = condStrToStr.replaceAll("\\n\\n", "\n");
                }
                allIdentifiersString.add(0, savedDate);
                allIdentifiersString.add(1, stringBuilder.toString());
                allIdentifiersString.add(2, condStrToStr);
                ArrayList<String> newIdentifiersArrayList = new ArrayList<String>(allIdentifiersString);
                savedWODList.add(mapCounter, newIdentifiersArrayList);
                conditioningString.setLength(0);
                stringBuilder.setLength(0);
                conditioningLine = 0;
                conditioningIterator = 0;
                allIdentifiersString.clear();
                condStrToStr = "";
                mapCounter++;
            } else {
                stringBuilder.append(receiveString + "\n");
            }

            if (receiveString.contains("Conditioning")) {
                conditioningLine = conditioningIterator;
            }

            if (conditioningLine != 0) {
                if (conditioningIterator > conditioningLine) {
                    if (receiveString.equals("endOfWOD")) {

                    } else {
                        conditioningString.append(receiveString + ", ");
                    }
                }
            }
            conditioningIterator++;

        }
        Collections.reverse(savedWODList);
        addButtonsToView(savedWODList);
        Collections.reverse(savedWODList);
//        if(Math.abs(previousArrayLength-savedWODList.size())%2==1){
//            Collections.reverse(savedWODList);
//        }
        previousArrayLength = savedWODList.size();
    }

    int deletedCount = 0;

    public void deleteSavedWOD(View v){
        LinearLayout LLForSavedWODsReceiver = (LinearLayout) v.getParent();
        int idForDividerLine = LLForSavedWODsReceiver.getNextFocusDownId();
        Button tFPOB = (Button) LLForSavedWODsReceiver.findViewById(R.id.textFieldPartOfButton);
        String strBefore = tFPOB.getText().toString();
        deletedCount++;
        removeFromArray(strBefore);
        boolean deleted = deleteOldMakeNewFile();
        if(deleted){
            LinearLayout bigParent = (LinearLayout) LLForSavedWODsReceiver.getParent();
            View dividerLineBelowLL = findViewById(idForDividerLine);
            bigParent.removeView(LLForSavedWODsReceiver);
            bigParent.removeView(dividerLineBelowLL);
//            Collections.reverse(savedWODList);
            //previously deleted method...
        }
    }

    public void removeFromArray(String checkString){
        for(int y=0;y<savedWODList.size();y++){
            ArrayList<String> allStrArrayArrayComponent = savedWODList.get(y);
            if(allStrArrayArrayComponent.get(2).equals(checkString)){
                savedWODList.remove(y);
                break;
            }
        }

    }

    public boolean deleteOldMakeNewFile() {
        File file = new File(getFilesDir() + "WODs_Saved_Doc_Plaything");
        boolean deleted = file.delete();
        if (deleted) {
//            Collections.reverse(savedWODList);
            for (int q = 0; q < savedWODList.size(); q++) {
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(getFilesDir() + "WODs_Saved_Doc_Plaything", true));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<String> temp_decl = savedWODList.get(q);
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
        return deleted;
    }

    public String determineDateText(String dateString){
        String dayStr = "";
        String monthStr = "";
        String retStr = "";
        String grabDayNumber = "";
        boolean foundNumber = false;
        if(dateString.contains("201")){
            int indexOfYear = dateString.indexOf("201");
            if(indexOfYear+4<dateString.length()-1) {
                dateString = dateString.substring(0, indexOfYear) + dateString.substring(indexOfYear + 4, dateString.length() - 1);
            }
        }
        if(dateString.contains("Sun")){
            retStr = "Sun";
        }
        else if(dateString.contains("sun")){
            retStr = "Sun";
        }
        else if(dateString.contains("Mon")){
            retStr = "Mon";
        }
        else if(dateString.contains("mon")){
            retStr = "Mon";
        }
        else if(dateString.contains("Tue")){
            retStr = "Tue";
        }
        else if(dateString.contains("tue")){
            retStr = "Tue";
        }
        else if(dateString.contains("Wed")){
            retStr = "Wed";
        }
        else if(dateString.contains("wed")){
            retStr = "Wed";
        }
        else if(dateString.contains("Thu")){
            retStr = "Thu";
        }
        else if(dateString.contains("thu")){
            retStr = "Thu";
        }
        else if(dateString.contains("Fri")){
            retStr = "Fri";
        }
        else if(dateString.contains("fri")){
            retStr = "Fri";
        }
        else if(dateString.contains("Sat")){
            retStr = "Sat";
        }
        else if(dateString.contains("sat")){
            retStr = "Sat";
        }
        for(int h=0;h<dateString.length();h++){
            if(dateString.charAt(h)=='.'){
                if(h-2>0) {
                    monthStr = dateString.substring(h - 2, h).trim();
                }
                if(dateString.length()>h+1) {
                    dayStr = dateString.substring(h + 1, dateString.length());
                }
            }
        }
        for(int h=0;h<dateString.length();h++){
            if(dateString.charAt(h)=='/'){
                if(h-2>0) {
                    monthStr = dateString.substring(h - 2, h).trim();
                }
                else if(h-1>=0){
                    monthStr = dateString.substring(h - 1, h).trim();
                }
                if(dateString.length()>h+1) {
                    dayStr = dateString.substring(h + 1, dateString.length());
                }
            }
        }

        if(dateString.contains("Jan")){
            retStr += "\nJan";
        }
        else if(dateString.contains("jan")){
            retStr += "\nJan";
        }
        else if(dateString.contains("Feb")){
            retStr += "\nFeb";
        }
        else if(dateString.contains("feb")){
            retStr += "\nFeb";
        }
        else if(dateString.contains("Mar")){
            retStr += "\nMar";
        }
        else if(dateString.contains("mar")){
            retStr += "\nMar";
        }
        else if(dateString.contains("Apr")){
            retStr += "\nApr";
        }
        else if(dateString.contains("apr")){
            retStr += "\nApr";
        }
        else if(dateString.contains("may")){
            retStr += "\nMay";
        }
        else if(dateString.contains("May")){
            retStr += "\nMay";
        }
        else if(dateString.contains("Jun")){
            retStr += "\nJun";
        }
        else if(dateString.contains("jun")){
            retStr += "\nJun";
        }
        else if(dateString.contains("Jul")){
            retStr += "\nJul";
        }
        else if(dateString.contains("jul")){
            retStr += "\nJul";
        }
        else if(dateString.contains("Aug")){
            retStr += "\nAug";
        }
        else if(dateString.contains("aug")){
            retStr += "\nAug";
        }
        else if(dateString.contains("Sep")){
            retStr += "\nSep";
        }
        else if(dateString.contains("sep")){
            retStr += "\nSep";
        }
        else if(dateString.contains("Oct")){
            retStr += "\nOct";
        }
        else if(dateString.contains("oct")){
            retStr += "\nOct";
        }
        else if(dateString.contains("Nov")){
            retStr += "\nNov";
        }
        else if(dateString.contains("nov")){
            retStr += "\nNov";
        }
        else if(dateString.contains("Dec")){
            retStr += "\nDec";
        }
        else if(dateString.contains("dec")){
            retStr += "\nDec";
        }

        if(monthStr.equals("1")){
            if(!retStr.isEmpty()) {
                retStr += "\nJan";
            }
            else{
                retStr = "Jan";
            }
        }
        else if(monthStr.equals("2")){
            if(!retStr.isEmpty()) {
                retStr += "\nFeb";
            }
            else{
                retStr = "Feb";
            }
        }
        else if(monthStr.equals("3")){
            if(!retStr.isEmpty()) {
                retStr += "\nMar";
            }
            else{
                retStr = "Mar";
            }
        }
        else if(monthStr.equals("4")){
            if(!retStr.isEmpty()) {
                retStr += "\nApr";
            }
            else{
                retStr = "Apr";
            }
        }
        else if(monthStr.equals("5")){
            if(!retStr.isEmpty()) {
                retStr += "\nMay";
            }
            else{
                retStr = "May";
            }
        }
        else if(monthStr.equals("6")){
            if(!retStr.isEmpty()) {
                retStr += "\nJun";
            }
            else{
                retStr = "Jun";
            }
        }
        else if(monthStr.equals("7")){
            if(!retStr.isEmpty()) {
                retStr += "\nJul";
            }
            else{
                retStr = "Jul";
            }
        }
        else if(monthStr.equals("8")){
            if(!retStr.isEmpty()) {
                retStr += "\nAug";
            }
            else{
                retStr = "Aug";
            }
        }
        else if(monthStr.equals("9")){
            if(!retStr.isEmpty()) {
                retStr += "\nSep";
            }
            else{
                retStr = "Sep";
            }
        }
        else if(monthStr.equals("10")){
            if(!retStr.isEmpty()) {
                retStr += "\nOct";
            }
            else{
                retStr = "Oct";
            }
        }
        else if(monthStr.equals("11")){
            if(!retStr.isEmpty()) {
                retStr += "\nNov";
            }
            else{
                retStr = "Nov";
            }
        }
        else if(monthStr.equals("12")){
            if(!retStr.isEmpty()) {
                retStr += "\nDec";
            }
            else{
                retStr = "Dec";
            }
        }
        if(!dayStr.isEmpty()) {
            retStr += "\n" + dayStr;
        }
        else{
            grabDayNumber = dateString.substring(dateString.length()-3,dateString.length()).trim();
            if(isInteger(grabDayNumber)){
                foundNumber = true;
                retStr += "\n"+grabDayNumber;
            }
            if(foundNumber == false) {
                grabDayNumber = dateString.substring(dateString.length() - 2, dateString.length()).trim();
                if (isInteger(grabDayNumber)) {
                    foundNumber = true;
                    retStr += "\n"+grabDayNumber;
                }
            }
        }
        return retStr;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public void addButtonsToView(ArrayList<ArrayList<String>> listOfListOfWODs) {
        for (int k = 0; k < listOfListOfWODs.size(); k++) {
            final ViewGroup newWODSave;
            final Button deleteNewWODSave;
            SpannableStringBuilder spanSinBuild = new SpannableStringBuilder();
            ArrayList<String> temp = savedWODList.get(k);
            String date = temp.get(0);
            String allStr = temp.get(1);
            String condStr = temp.get(2);
            String dateTextShortened = determineDateText(date);
//            SpannableString dayTxt = new SpannableString(date);
//            dayTxt.setSpan(new AbsoluteSizeSpan(22, true), 0, dayTxt.length(), 0);
//            spanSinBuild.append(dayTxt);
            newWODSave = (ViewGroup) getLayoutInflater().inflate(R.layout.borderlessbuttontemplate, null);
            newWODSave.setId(savedWODView.generateViewId());
            SpannableString conditionString = new SpannableString(condStr);
            conditionString.setSpan(new AbsoluteSizeSpan(14, true), 0, conditionString.length(), 0);
            spanSinBuild.append(conditionString);
            final Button textFieldofNewWODSave = (Button) newWODSave.findViewById(R.id.textFieldPartOfButton);
            final Button dateFieldofNewWODSave = (Button) newWODSave.findViewById(R.id.date_button);
            dateFieldofNewWODSave.setText(dateTextShortened);
            textFieldofNewWODSave.setText(spanSinBuild, TextView.BufferType.SPANNABLE);
            textFieldofNewWODSave.setOnTouchListener(new OnSwipeTouchListener(this) {
                public void onSwipeRight() {
//                    Toast.makeText(DisplaySavedWODsActivity.this, "You Swiped Right.\n"+textFieldofNewWODSave.getText().toString(), Toast.LENGTH_SHORT).show();
                    newWODSave.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
                }
                public void onSwipeLeft() {
//                    Toast.makeText(DisplaySavedWODsActivity.this, "You Swiped Left.\n"+textFieldofNewWODSave.getText().toString(), Toast.LENGTH_SHORT).show();
                    newWODSave.findViewById(R.id.delete_button).setVisibility(View.GONE);
                }

                public void onClick() {
                    for (int k = 0; k < savedWODList.size(); k++) {
                        ArrayList<String> listOfListOfWODs = savedWODList.get(k);
                        String WODButtonTextFromlOLOW = listOfListOfWODs.get(2);
                        if (WODButtonTextFromlOLOW.equals(textFieldofNewWODSave.getText().toString())) {
                            Intent startWODDetail = new Intent(DisplaySavedWODsActivity.this, DisplaySavedWODDetailActivity.class);
                            startWODDetail.putExtra("WODFullTextKey", listOfListOfWODs.get(1));
                            startWODDetail.putExtra("AllStringsArray", savedWODList);
                            startActivity(startWODDetail);
                        }
                    }
                    String bullshit = "bs";
                }
            });
            dateFieldofNewWODSave.setOnTouchListener(new OnSwipeTouchListener(this) {
                public void onSwipeRight() {
//                    Toast.makeText(DisplaySavedWODsActivity.this, "You Swiped Right.\n"+textFieldofNewWODSave.getText().toString(), Toast.LENGTH_SHORT).show();
                    newWODSave.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
                }
                public void onSwipeLeft() {
//                    Toast.makeText(DisplaySavedWODsActivity.this, "You Swiped Left.\n"+textFieldofNewWODSave.getText().toString(), Toast.LENGTH_SHORT).show();
                    newWODSave.findViewById(R.id.delete_button).setVisibility(View.GONE);
                }

                public void onClick() {
                    for (int k = 0; k < savedWODList.size(); k++) {
                        ArrayList<String> listOfListOfWODs = savedWODList.get(k);
                        String WODButtonTextFromlOLOW = listOfListOfWODs.get(2);
                        if (WODButtonTextFromlOLOW.equals(textFieldofNewWODSave.getText().toString())) {
                            Intent startWODDetail = new Intent(DisplaySavedWODsActivity.this, DisplaySavedWODDetailActivity.class);
                            startWODDetail.putExtra("WODFullTextKey", listOfListOfWODs.get(1));
                            startWODDetail.putExtra("AllStringsArray", savedWODList);
                            startActivity(startWODDetail);
                        }
                    }
                    String bullshit = "bs";
                }
            });
            View separator = new View(this);
            separator.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    4));
            separator.setBackgroundColor(Color.rgb(90, 90, 90));
            savedWODView.addView(newWODSave);
            savedWODView.addView(separator);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_saved_wods, menu);
        return true;
    }

//    public class ParcelData implements Parcelable {
//        ArrayList<ArrayList<String>> savedWODList;
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel parcel, int i) {
//
//        }
////        int id;
////        String name;
////        String desc;
////        String[] cities = {"suwon", "delhi"};
//    }

    public void getWODDetail(View v) {
        View childView = v;
        Button WODButton = (Button) childView;
        String WODButtonText = WODButton.getText().toString();
        for (int k = 0; k < savedWODList.size(); k++) {
            ArrayList<String> listOfListOfWODs = savedWODList.get(k);
            String WODButtonTextFromlOLOW = listOfListOfWODs.get(0) + "\n" + listOfListOfWODs.get(2);
            if (WODButtonTextFromlOLOW.equals(WODButtonText)) {
                Intent startWODDetail = new Intent(this, DisplaySavedWODDetailActivity.class);
                startWODDetail.putExtra("WODFullTextKey", listOfListOfWODs.get(1));
                startWODDetail.putExtra("AllStringsArray", savedWODList);
                startActivity(startWODDetail);
            }
        }
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

//    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            try {
//                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                    return false;
//                // right to left swipe
//                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    Toast.makeText(DisplaySavedWODsActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
//                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    Toast.makeText(DisplaySavedWODsActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                // nothing
//            }
//            return false;
//        }
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
//    }
}

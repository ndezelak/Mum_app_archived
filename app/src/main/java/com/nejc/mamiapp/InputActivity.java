package com.nejc.mamiapp;


/**
 * @author Nejc
 * <p/>
 * Description:
 * Main activity for making inputs to the database
 * All the GUI elements are initialized, callback methods are set.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/*********** REVISION HISTORY *****************
 *      10/07/2016: Added some comments
 *
 *
 *
 *
 /***********************************************/


public class InputActivity extends AppCompatActivity {
    PendingIntent sendNotification;
    PendingIntent reNotify;
    Context context;
    SharedPreferences pref;

    // Define all the Widgets from the .XML
    ListView listview = null;
    TextView text = null;
    Spinner spinnerDay = null;
    Spinner spinnerMonth = null;


    @Override
    // Display current month and day and highlight that day in the listview
    protected void onResume() {
        Log.i("Database", "You are inside on Resume");
        super.onResume();
        AlarmManager manager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        setCurrentMonthAndDay();
/*
        // Here you cancel all the Alarms and initialize a new one at 9 pm next day
        Log.d("Alarm", "You are inside the onResume Callback");

        //  Get the Alarm manager
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Intent that starts a service
        Intent intent = new Intent(this, CreateNotificationService.class);


        // Close the repeating Alarm after you've started this activity.
        reNotify = PendingIntent.getService(this, 1, intent, 0);

        if (reNotify != null) {
            manager.cancel(reNotify);
            Log.d("Alarm", "Repeating alarm has been canceled");
        }


        // If the specified pending intent is not yet active, null is returned
       // sendNotification = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_NO_CREATE);

        //If the pending intent is already in use, cancel it first.
        sendNotification = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        manager.cancel(sendNotification);
        sendNotification = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        // If the Pending Intent doesn't exist anymore then create a new one
        // Here the long alarm is initialized and started. All other alarms should
        // be inactive up to this point.

        // New Calendar object. It is used to get information regarding day, day of the week, ...
        // using current offset of the epcho (Linux time since 1 January 1970)
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 5);


        manager.set(AlarmManager.RTC, calendar.getTimeInMillis(), sendNotification);
        Log.d("Alarm", "New nonrepeating alarm has been created");
        Log.d("Alarm", "System clock in miliseconds: "+Long.toString(System.currentTimeMillis()));
*/
    }

    // Used to store adapters globally via making a class instance that saves them.
    public class AdapterContainer {
        private BaseAdapter adapter;

        public AdapterContainer(BaseAdapter adapter) {
            this.adapter = adapter;

        }

        public BaseAdapter getArrayAdapters() {

            return this.adapter;
        }

    }

    AdapterContainer adapterContainer;


    @Override
    // Initializes all the GUI components
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Database", "You are inside on Create");
        Log.i("Database", "Application context inside onCreate is: " + getApplicationContext().toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        context = getApplicationContext();
        pref = context.getSharedPreferences("Constants", context.MODE_PRIVATE);

        // All GUI objects
        listview = (ListView) findViewById(R.id.listView);
        text = (TextView) findViewById(R.id.TextDay);
        spinnerDay = (Spinner) findViewById(R.id.spinner_day);
        spinnerMonth = (Spinner) findViewById(R.id.spinner_month);

        //startService(intent);

        //********Spinner Day Adapter initialization***************************************************//

        // This if statement might be redundant
        if (spinnerDay.getAdapter() == null) {

            // Once again you define all the types dynamically
            // TO DO: Define types statically!
            ArrayList<String> listTypes = new ArrayList<String>();
            listTypes.add("Prazno");
            listTypes.add("Šank dopoldan");
            listTypes.add("Šank popoldan");
            listTypes.add("Kuhinja dopoldan");
            listTypes.add("Kuhinja popoldan");
            listTypes.add("Prosto");
            listTypes.add("Dopust");

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, listTypes);
            spinnerDay.setAdapter(spinnerAdapter);
            Log.i("Spinner Type", Float.toString(spinnerDay.getAlpha()));
        }

        //*********Spinner Month Adapter Initialization*************************************************//

        //Put months into an arrayList
        String[] monthsStrings = {"Januar", "Februar", "Marec", "April", "Maj", "Junij", "Julij", "Avgust", "September", "Oktober", "November", "December"};
        List<String> months = new ArrayList<String>(Arrays.asList(monthsStrings));


        //Set the arrayAdapter for the spinner using a standard layout
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, months);
        spinnerMonth.setAdapter(adapterMonth);


        //**************Listview Adapter initialization ********************//
        ArrayList<String> arrayList = new ArrayList<String>();

        arrayList.add("Ponedeljek");
        arrayList.add("Torek");
        arrayList.add("Sreda");
        arrayList.add("Četrtek");
        arrayList.add("Petek");
        arrayList.add("Sobota");
        arrayList.add("Nedelja");


        // Here you use your special Adapter that extends BaseAdapter for populating the Listview
        // Inside a Callback method you read out of the Database and populate all the rows with correct data.
        // But you needed to downcast it to BaseAdapter as the Listview expects an Adapter like that.
        final BaseAdapter adapter = new CustomAdapter(this, arrayList);
        listview.setAdapter(adapter);

        // Save this adapter to the adapterContainer
        adapterContainer = new AdapterContainer(adapter);

        //*********************Month Spinner Initialization********************************************************//
        // Spinner onItem selected listener. It is used to save current month to the preference file
        // and determine size of the listview (using a lookup table)
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Determine the selected month and month length in days
                //TODO: Use GregorianCalendar class to determine month size
                int month = (int) id + 1;
                int length = 0;
                if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    length = 31;
                }
                if (month == 4 || month == 6 || month == 9 || month == 11) {
                    length = 30;
                }
                if (month == 2) length = 29;


                //Add these values to the preference file that keeps track of selected month and its length.
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Constants", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences.edit();
                editor1.putInt("month", month);
                editor1.putInt("length", length);
                editor1.commit();


                //Initialize the particular month of the year in the database
                // TODO: This might take quite a long time? Use a profiler to check this assumption.
                for (int i = 1; i <= length; i++) {

                    DataBaseHelper.initializeDatabase(i, month, 2016, openOrCreateDatabase("Workdays", getApplicationContext().MODE_PRIVATE, null), "workdays");
                }

                // Everytime a new month in the spinner is selected, redraw Listview.
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //********* Preference File Initialization *************************************************//
        //Set Preference's month to the currently selected one
        // TODO: Isn't this already done while spinner is initialized?
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("month", (int) spinnerMonth.getSelectedItemId() + 1);
        editor.commit();


        //*********** ListView initialization ************************************************//
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Here you should set the Textview showing current settings for a particular date
                //You should also set the spinner to the particular setting

                // Selected day
                int day = position + 1;

                //get Preference file
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Constants", getApplicationContext().MODE_PRIVATE);

                //Current selected month
                int month = pref.getInt("month", 1);


                //Save the last selected day into the Preference File. Is this even important?
                SharedPreferences.Editor editor1 = pref.edit();
                editor1.putInt("day", day);
                editor1.commit();

                // Set Textview to the current selected date.
                text.setText("Datum: " + Integer.toString(day) + "." + Integer.toString(month) + ".2016");


                //Set the cursor to the saved type for the particular date.
                SQLiteDatabase base = context.openOrCreateDatabase("Workdays", context.MODE_PRIVATE, null);

                Cursor cursor = base.rawQuery("SELECT * FROM " + "Workdays" + " WHERE day=" +
                        Integer.toString(day) + " AND month=" + Integer.toString(month) + " AND year=" + Integer.toString(2016), null);
                cursor.moveToFirst();
                int Type = 0;

                // Read type from the database
                if (cursor != null) {
                    Type = cursor.getInt(3);
                }

                if (!cursor.isClosed()) cursor.close();


                // Here you actually set the selection
                spinnerDay.setSelection(Type);


            }


        });

        // ********* Spinner for work item initialization******************************//
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                // Open Database
                SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("Workdays", context.MODE_PRIVATE, null);
                int i = 0;

                // Get the current selected day from the preferences.
                int Year = 2016;
                SharedPreferences pref = context.getSharedPreferences("Constants", context.MODE_PRIVATE);
                int Month = pref.getInt("month", 1);
                int Day = pref.getInt("day", 1);

                //Save the selected type (which is exactly the position) to the Database
                sqLiteDatabase.execSQL("UPDATE workdays SET type=" + Integer.toString(position) + " WHERE day=" + Integer.toString(Day) + " AND month=" + Integer.toString(Month) + " AND year=" + Integer.toString(Year));

                // Notify the Listview Adapter for a redraw.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //*********** Reset Button OnClick Listener **********************************************//
        Button resetMonth = new Button(this);
        resetMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int month = pref.getInt("month", 1);
                int year = 2016;

                // Here you use a Wrapper for making the code look cleaner.
                DataBaseHelper.setBackDatabase(month, year, context.openOrCreateDatabase("Workdays", context.MODE_PRIVATE, null), "workdays");

                ListView listView = (ListView) findViewById(R.id.listView);
                adapter.notifyDataSetChanged();
            }
        });


    }//End of onCreate


    public void setCurrentMonthAndDay() {
        // In this function you should set month spinner to the current month &
        // and choose the current day from the list view
        int currentday = 0;
        int currentmonth = 0;
        int type = 0;
        Calendar currentDate = Calendar.getInstance();
        currentmonth = currentDate.get(Calendar.MONTH);
        currentday = currentDate.get(Calendar.DAY_OF_MONTH);

        Log.i("Database", "Application context inside onResume is: " + getApplicationContext().toString());
        SQLiteDatabase base = getApplicationContext().openOrCreateDatabase("Workdays", getApplicationContext().MODE_PRIVATE, null);


        Cursor cursor = base.rawQuery("SELECT * FROM " + "Workdays" + " WHERE day=" +
                Integer.toString(currentday) + " AND month=" + Integer.toString(currentmonth + 1) + " AND year=" + Integer.toString(2016), null);
        cursor.moveToFirst();


        if (cursor.getCount() != 0) {
            // This is the day type stored as an integer
            type = cursor.getInt(3);
            Log.i("Database", "Selected type from database: " + Integer.toString(type));
        }

        spinnerDay.setSelection(type);
        spinnerMonth.setSelection(currentmonth);
        text.setText("Datum: " + Integer.toString(currentday) + "." + Integer.toString(currentmonth + 1) + ".2016");


        // After you have changed the spinners you should
        // redraw all the Listview lines.
        // Temporal solution! Listview adapter is saved inside
        // the custom class AdapterContainer
        adapterContainer.getArrayAdapters().notifyDataSetChanged();

    }


}// End of Activity

package com.laurinka.defineYourDay.app;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        sharedPreferences = getSharedPreferences("com.laurinka.defineYourDay",
                MODE_PRIVATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNumber(View view) {
        boolean commit = sharedPreferences.edit().putString(getCurrentDate(), findNumber()).commit();
        if (!commit) {
            throw new IllegalStateException("wtf!");
        }
        updateText();
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");

    static protected String getCurrentDate() {
        Date date = new Date();
        return sdf.format(date);
    }
    protected String findNumber() {
        EditText editText = findEditText();
        return editText.getText().toString();
    }

    protected void updateText() {
        DateFormat nice = DateFormat.getDateInstance();
        Date today;
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            Date tmpDate = instance.getTime();
            String format = sdf.format(tmpDate);
            String string = sharedPreferences.getString(format, "Day sucked!");
            sb.append(nice.format(tmpDate) + ": "  + string);
            sb.append("\n");
            instance.add(Calendar.DAY_OF_YEAR, -1);
        }
        TextView list = (TextView) findViewById(R.id.list);
        list.setText(sb.toString());
    }
    protected EditText findEditText() {
        return (EditText) findViewById(R.id.input);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}

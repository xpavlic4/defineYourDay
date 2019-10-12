package com.laurinka.defineYourDay.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

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
        updateText();
    }

    /**
     * Fills old statuses when started.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateText();
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

    /**
     * Saves number into shared preferences
     * and update list of statuses accordingly.
     * @param view
     */
    public void saveNumber(View view) {
        String currentDate = getCurrentDate();
        String status = findStatus();
        Log.i(this.getLocalClassName(), currentDate + ":" + status);
        boolean commit = sharedPreferences.edit().putString(currentDate, status).commit();
        if (!commit) {
            throw new IllegalStateException("wtf!");
        }
        updateText();
        clearStatus();
        findSaveButton().requestFocus();
    }
    // use 20131224 format
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    static protected String getCurrentDate() {
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * Clears status after saving.
     */
    protected void clearStatus() {
        EditText editText = findEditText();
        editText.setText("");
    }

    /**
     * Finds status.
     * @return
     */
    protected String findStatus() {
        EditText editText = findEditText();
        Editable text = editText.getText();
        if (null == text) {
            return "";
        }
        return text.toString();
    }

    /**
     * Concatenate last 5 statuses and displays them.
     */
    protected void updateText() {
        DateFormat nice = DateFormat.getDateInstance();
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            Date tmpDate = instance.getTime();
            String format = sdf.format(tmpDate);

           Log.i(this.getLocalClassName(), "Asking..." + format);
            String string = sharedPreferences.getString(format, "Day sucked!");
            sb.append(nice.format(tmpDate));
            sb.append(": ");
            sb.append(string);
            sb.append("\n");
            instance.add(Calendar.DAY_OF_YEAR, -1);
        }
        TextView list = (TextView) findViewById(R.id.list);
        if (null != list) {
            list.setText(sb.toString());
        }
    }

    /**
     * Finds save button.
     * @return
     */
    protected Button findSaveButton() {
        return (Button) findViewById(R.id.sendMessage);
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

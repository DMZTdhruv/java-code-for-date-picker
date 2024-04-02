package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private EditText editTextTodo;
    private Button addButton;
    private LinearLayout tasksContainer;
    private TextView textViewDateTime;
    private Button buttonPickDateTime;
    private Calendar calendar;


    // Key for SharedPreferences
    private static final String PREF_TASKS = "tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textViewDateTime = findViewById(R.id.textViewDateTime);
        buttonPickDateTime = findViewById(R.id.buttonPickDateTime);

        calendar = Calendar.getInstance();

        buttonPickDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        // Initialize views
        editTextTodo = findViewById(R.id.editText2);
        addButton = findViewById(R.id.button2);
        tasksContainer = findViewById(R.id.taskContainer);

        // Load tasks from SharedPreferences
        loadTasks();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = editTextTodo.getText().toString().trim();
                if (!taskName.isEmpty()) {
                    addTask(taskName);
                    editTextTodo.setText("");
                    saveTasks(); // Save tasks after adding a new one
                    Toast.makeText(MainActivity.this, "New task added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDateTimePicker() {
        // Get current date and time
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create and show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Create and show TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);

                                        // Format the selected date and time
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy 'at' h:mm a", Locale.getDefault());
                                        String dateTime = dateFormat.format(calendar.getTime());

                                        // Display toast message with selected date and time
                                        String message = "Hi! Your cab booking is done as per your request " + dateTime + ".";
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                                    }
                                }, hourOfDay, minute, false);
                        timePickerDialog.show();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void addTask(String taskText) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(0, 0, 0, dpToPx(8));
        linearLayout.setLayoutParams(layoutParams);

        CheckBox checkBox = new CheckBox(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        checkBox.setLayoutParams(layoutParams1);
        linearLayout.addView(checkBox);

        // creating task view for the description
        TextView textView = new TextView(this);
        textView.setText(taskText);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        textViewParams.setMargins(dpToPx(8), 0, 0, 0);
        textView.setLayoutParams(textViewParams);
        linearLayout.addView(textView);

        tasksContainer.addView(linearLayout);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // Save tasks to SharedPreferences
    private void saveTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> taskSet = new HashSet<>();
        for (int i = 0; i < tasksContainer.getChildCount(); i++) {
            LinearLayout taskLayout = (LinearLayout) tasksContainer.getChildAt(i);
            TextView textView = (TextView) taskLayout.getChildAt(1); // Assuming the TextView is always the second child
            String taskText = textView.getText().toString();
            taskSet.add(taskText);
        }
        sharedPreferences.edit().putStringSet(PREF_TASKS, taskSet).apply();
    }

    // Load tasks from SharedPreferences
    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> taskSet = sharedPreferences.getStringSet(PREF_TASKS, new HashSet<String>());
        for (String taskText : taskSet) {
            addTask(taskText);
        }
    }
}


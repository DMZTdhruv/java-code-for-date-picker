import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewDateTime;
    private Button buttonPickDateTime;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewDateTime = findViewById(R.id.textViewDateTime);
        buttonPickDateTime = findViewById(R.id.buttonPickDateTime);

        calendar = Calendar.getInstance();

        buttonPickDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
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
}



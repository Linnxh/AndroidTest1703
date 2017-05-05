package com.test.lxh.calender;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.test.lxh.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LXH on 17/5/5.
 */

public class CalendarActivity extends AppCompatActivity {
    CalenderView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_calender);

        calendar = (CalenderView) findViewById(R.id.myCalender);
        calendar.setDateLongPressListener(new CalenderView.dateLongPressListener() {
            @Override
            public void onDateLongPressListener(Date date, int position) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
                Toast.makeText(CalendarActivity.this, format.format(date), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

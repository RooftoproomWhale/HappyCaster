package com.hci.happycaster.ActivityClass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hci.happycaster.CalendarUtil.CurrentDayDecorator;
import com.hci.happycaster.CalendarUtil.OneDayDecorator;
import com.hci.happycaster.CalendarUtil.SaturdayDecorator;
import com.hci.happycaster.CalendarUtil.SundayDecorator;
import com.hci.happycaster.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PeriodAnalysisActivity extends AppCompatActivity {

    private Spinner Cityspinner;
    private ArrayAdapter<CharSequence> adspin;
    private Button button;
    private Intent intent;
    private MaterialCalendarView calendarview;
    private String City;
    private Date startdate, enddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_analysis);

        button = (Button)findViewById(R.id.analysisButton);
        Cityspinner = (Spinner)findViewById(R.id.citySpinner);
        calendarview = (MaterialCalendarView)findViewById(R.id.CalendarView);

        adspin = ArrayAdapter.createFromResource(this,R.array.city,android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Cityspinner.setAdapter(adspin);

        Cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                {
                    City = "??????";
                }
                if(i == 1)
                {
                    City = "??????";
                }
                if(i == 2)
                {
                    City = "??????";
                }
                if(i == 3)
                {
                    City = "??????";
                }
                if(i == 4)
                {
                    City = "??????";
                }
                if(i == 5)
                {
                    City = "??????";
                }
                if(i == 6)
                {
                    City = "??????";
                }
                System.out.println("city : "+City);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        calendarview.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        calendarview.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OneDayDecorator());

        calendarview.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                startdate = date.getDate();
                enddate = date.getDate();
                System.out.println("Date : "+startdate +"/"+ enddate);
            }
        });
        calendarview.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                if(dates.size() <= 7)
                {
                    System.out.println("select : "+calendarview.getSelectedDates());
                    startdate = dates.get(0).getDate();
                    enddate = dates.get(dates.size()-1).getDate();
                    System.out.println("Date : "+startdate +"/"+ enddate);
                }
                else
                {
                    Toast.makeText(PeriodAnalysisActivity.this,"????????? ?????? ??????????????????.",Toast.LENGTH_SHORT).show();
                    startdate = null;
                    enddate = null;
                    calendarview.clearSelection();
                    calendarview.invalidate();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((startdate != null && enddate != null)) {
                    calendarview.clearSelection();
                    calendarview.invalidate();
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    intent = new Intent(getBaseContext(),PeriodDetailActivity.class);
                    intent.putExtra("City",City);
                    intent.putExtra("startDay",sf.format(startdate));
                    intent.putExtra("endDay",sf.format(enddate));
                    startActivity(intent);
                    startdate = null;
                    enddate = null;
                }
                else
                {
                    Toast.makeText(PeriodAnalysisActivity.this,"????????? ??????????????????.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar??? ???????????? ?????? CustomEnabled??? true ????????? ?????? ?????? ?????? false ?????????
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //????????? ???????????? ??? ??????????????? ????????? ???????????????.
        actionBar.setDisplayShowTitleEnabled(false);        //???????????? ???????????? ????????? ??????????????? ???????????????.
        actionBar.setDisplayShowHomeEnabled(false);            //??? ???????????? ?????????????????????.

        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_realtime_pp_analysis));

        return true;
    }
}

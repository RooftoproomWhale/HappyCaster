package com.hci.happycaster.ActivityClass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hci.happycaster.R;

public class MainActivity extends AppCompatActivity {
    private Button LiveBtn, LocBtn, PeriodBtn;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LiveBtn = (Button)findViewById(R.id.LiveBtn);
        LocBtn = (Button)findViewById(R.id.LocBtn);
        PeriodBtn = (Button)findViewById(R.id.PeriodBtn);
    }
    public void mOnClick(View v)
    {
         switch (v.getId())
         {
             case R.id.LiveBtn:
                 intent = new Intent(getBaseContext(),RealTimeMapActivity.class);
                 startActivity(intent);
                 break;
             case R.id.LocBtn:
                 intent = new Intent(getBaseContext(),LocAnalysisActivity.class);
                 startActivity(intent);
                 break;
             case R.id.PeriodBtn:
                 intent = new Intent(getBaseContext(),PeriodAnalysisActivity.class);
                 startActivity(intent);
                 break;
         }
    }
}

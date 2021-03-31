package com.hci.happycaster.CalendarUtil;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.hci.happycaster.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

/**
 * Created by w on 2017-11-09.
 */
public class CurrentDayDecorator implements DayViewDecorator {

    private Drawable drawable;

    CalendarDay currentDay = CalendarDay.from(new Date());

    public CurrentDayDecorator(Activity context) {
        drawable = ContextCompat.getDrawable(context, R.drawable.first_day_month);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(currentDay);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
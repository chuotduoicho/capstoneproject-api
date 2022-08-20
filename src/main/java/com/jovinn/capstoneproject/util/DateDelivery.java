package com.jovinn.capstoneproject.util;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class DateDelivery {
    public Date expectDate(Integer expectTimeToDelivery, Integer totalDeliveryTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(expectTimeToDelivery, totalDeliveryTime);
        return cal.getTime();
    }

    public Date expectDateCompleteAuto(Date date, Integer countDayAfterDelivery) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, countDayAfterDelivery);
        return cal.getTime();
    }
}

package com.kspt.it.services.forecast;

import com.kspt.it.services.forecast.real.ForecastStatisticsExtrapolationService;
import javafx.util.Pair;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

public class ForecastStatisticsExtropalationServiceTest {

    @Test
    public void testExtrapolateStatistics()
            throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = dateFormat.parse("29-12-2015");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        List<Pair<Double, Long>> inputList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            calendar.add(Calendar.DATE, 1);
            inputList.add(new Pair<Double, Long>(new Double(i), calendar.getTime().getTime()));
        }

        List<Pair<Double, Long>> outputList = ForecastStatisticsExtrapolationService
                .extrapolateStatistics(inputList, 9);

        for (Pair<Double, Long> pair : outputList) {
            System.out.println(pair.getKey() + " " + new Date(pair.getValue()));
        }
    }
}
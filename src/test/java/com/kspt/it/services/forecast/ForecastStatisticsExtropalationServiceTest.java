package com.kspt.it.services.forecast;

import com.kspt.it.services.forecast.real.ForecastStatisticsExtropalationService;
import javafx.util.Pair;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForecastStatisticsExtropalationServiceTest {

  @Test
  public void testExtrapolateStatistics()
  throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date date = dateFormat.parse("29-12-2015");
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    List<Pair<Float, Date>> inputList = new ArrayList<>();

    for (int i = 0; i < 6; i++) {
      calendar.add(Calendar.DATE, 1);
      inputList.add(new Pair((float) i, calendar.getTime()));
    }

    List<Pair<Float, Date>> outputList = ForecastStatisticsExtropalationService
        .extrapolateStatistics(inputList, 5);

    for (Pair<Float, Date> pair : outputList) {
      System.out.println(pair.getKey() + " " + pair.getValue());
    }
  }
}
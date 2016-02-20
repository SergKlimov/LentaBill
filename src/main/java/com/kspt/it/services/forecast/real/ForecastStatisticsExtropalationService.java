package com.kspt.it.services.forecast.real;

import javafx.util.Pair;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ForecastStatisticsExtropalationService {
  /**
   * Функция, экстрополирующая численный ряд
   * @param inputList список пар Значение/Дата с данными за предыдущее время
   * @param stepSize переменная, задающая на сколько шагов производится экстраполяция
   * @return список пар Значение/Дата с предсказанным рядом
   */
  public static List<Pair<Float, Date>> extrapolateStatistics(
      List<Pair<Float, Date>> inputList,
      int stepSize) {
    DataSet observedData = new DataSet();

    Date maxDate = new Date(Long.MIN_VALUE);

    for (Pair<Float, Date> pair : inputList) {
      DataPoint dp = new Observation(pair.getKey());
      dp.setIndependentValue("date", pair.getValue().getTime());
      observedData.add(dp);

      if (maxDate.before(pair.getValue())) {
        maxDate = pair.getValue();
      }
    }

    observedData.sort("date");

    ForecastingModel forecastingModel = Forecaster.getBestForecast(observedData);

    if (stepSize < 1) {
      stepSize = 15;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(maxDate);

    DataSet requiredDataPoints = new DataSet();

    for (int i = 0; i < stepSize; i++) {
      calendar.add(Calendar.DATE, 1);
      DataPoint dp = new Observation(0.0);
      dp.setIndependentValue("date", calendar.getTime().getTime());
      requiredDataPoints.add(dp);
    }

    forecastingModel.forecast(requiredDataPoints);

    List<Pair<Float, Date>> returnList = new ArrayList<>();

    Iterator<DataPoint> it = requiredDataPoints.iterator();

    while (it.hasNext()) {
      DataPoint dp = it.next();
      Double doubleTime = dp.getIndependentValue("date");
      Date date = new Date(doubleTime.longValue());
      returnList.add(new Pair(dp.getDependentValue(), date));
    }

    return returnList;
  }
}

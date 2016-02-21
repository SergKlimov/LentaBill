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
import java.util.List;

public class ForecastStatisticsExtropalationService {
    /**
     * Функция, экстрополирующая численный ряд
     *
     * @param inputList список пар Значение/Дата с данными за предыдущее время
     * @param stepSize  переменная, задающая на сколько шагов производится экстраполяция. Если ее значние меньше
     *                  единицы, то экстраполяция производится на 15 шагов вперед.
     * @return список пар Значение/Дата с предсказанным рядом
     */
    public static List<Pair<Float, Date>> extrapolateStatistics(
            final List<Pair<Float, Date>> inputList,
            int stepSize) {
        DataSet observedData = new DataSet();
        Date maxDate = new Date(Long.MIN_VALUE);
        final String tagString = "date";

        for (Pair<Float, Date> pair : inputList) {
            DataPoint dp = new Observation(pair.getKey());
            dp.setIndependentValue(tagString, pair.getValue().getTime());
            observedData.add(dp);

            if (maxDate.before(pair.getValue())) {
                maxDate = pair.getValue();
            }
        }

        observedData.sort(tagString);

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
            dp.setIndependentValue(tagString, calendar.getTime().getTime());
            requiredDataPoints.add(dp);
        }

        forecastingModel.forecast(requiredDataPoints);

        List<Pair<Float, Date>> returnList = new ArrayList<>();

        for (DataPoint dp : requiredDataPoints) {
            Double doubleTime = dp.getIndependentValue(tagString);
            Date date = new Date(doubleTime.longValue());
            Float val = (float) dp.getDependentValue();
            returnList.add(new Pair<>(val, date));
        }
        return returnList;
    }
}

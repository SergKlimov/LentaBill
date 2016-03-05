package com.kspt.it.services.forecast.real;

import javafx.util.Pair;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;

import java.util.ArrayList;
import java.util.List;

public class ForecastStatisticsExtrapolationService {
    /**
     * Функция, экстрополирующая численный ряд
     *
     * @param inputList список пар Значение/Дата (в формате Unix Timestamp) с данными за предыдущее время
     * @param stepSize  переменная, задающая на сколько шагов производится экстраполяция. Если ее значние меньше
     *                  единицы, то экстраполяция производится на 15 шагов вперед.
     * @return список пар Значение/Дата с предсказанным рядом
     */
    public static List<Pair<Double, Long>> extrapolateStatistics(
            final List<Pair<Double, Long>> inputList,
            int stepSize) {
        DataSet observedData = new DataSet();
        Long maxDate = Long.MIN_VALUE;
        final String tagString = "date";

        for (Pair<Double, Long> pair : inputList) {
            DataPoint dp = new Observation(pair.getKey());
            dp.setIndependentValue(tagString, pair.getValue().doubleValue());
            observedData.add(dp);

            if (maxDate < pair.getValue()) {
                maxDate = pair.getValue();
            }
        }

        observedData.sort(tagString);

        ForecastingModel forecastingModel = Forecaster.getBestForecast(observedData);

        if (stepSize < 1) {
            stepSize = 15;
        }

        DataSet requiredDataPoints = new DataSet();

        for (int i = 0; i < stepSize; i++) {
            DataPoint dp = new Observation(0.0);
            maxDate += daysToMilliseconds(1);
            dp.setIndependentValue(tagString, maxDate.doubleValue());
            requiredDataPoints.add(dp);
        }

        forecastingModel.forecast(requiredDataPoints);

        List<Pair<Double, Long>> returnList = new ArrayList<>();

        for (DataPoint dp : requiredDataPoints) {
            Double doubleTime = dp.getIndependentValue(tagString);
            Long date = doubleTime.longValue();
            Double val = dp.getDependentValue();
            returnList.add(new Pair<>(val, date));
        }
        return returnList;
    }

    public static Long daysToMilliseconds(final int days) {
        return (long) (days * 24 * 60 * 60 * 1000);
    }
}

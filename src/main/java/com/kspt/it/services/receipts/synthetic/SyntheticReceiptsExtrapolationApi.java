package com.kspt.it.services.receipts.synthetic;

import com.kspt.it.services.forecast.real.ForecastStatisticsExtrapolationService;
import com.kspt.it.services.receipts.ReceiptsExtrapolationAllShopsResult;
import com.kspt.it.services.receipts.ReceiptsExtrapolationApi;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SyntheticReceiptsExtrapolationApi implements ReceiptsExtrapolationApi {

    private final int observedDaySize = 30;

    private List<Pair<Double, Long>> observedDays;

    private long startDay;

    public SyntheticReceiptsExtrapolationApi() {

        this.observedDays = new ArrayList<>();
        this.startDay = System.currentTimeMillis() -
            TimeUnit.DAYS.toMillis(observedDaySize);

        for (int i = 0; i < this.observedDaySize; i++) {
            observedDays.add(new Pair<>(Math.random() * 100, this.startDay));
            this.startDay += TimeUnit.DAYS.toMillis(1);
        }
    }

    @Override
    public List<ReceiptsExtrapolationAllShopsResult> extrapolateInAllShops() {
        List<Pair<Double, Long>> futureDays =
                ForecastStatisticsExtrapolationService.extrapolateStatistics(this.observedDays, 0);

        List<ReceiptsExtrapolationAllShopsResult> retList =
                this.observedDays.stream().map(observed -> new ReceiptsExtrapolationAllShopsResult(observed.getValue(),
                        observed.getKey())).collect(Collectors.toList());
        retList.addAll(futureDays.stream().map(observed -> new ReceiptsExtrapolationAllShopsResult(observed.getValue(),
                observed.getKey())).collect(Collectors.toList()));

        return retList;
    }
}

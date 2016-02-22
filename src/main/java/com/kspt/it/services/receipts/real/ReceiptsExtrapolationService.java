package com.kspt.it.services.receipts.real;

import com.google.inject.Inject;
import com.kspt.it.dao.aggregation.receipts.ReceiptsExtrapolationDAO;
import com.kspt.it.services.receipts.ReceiptsExtrapolationAllShopsResult;
import com.kspt.it.services.receipts.ReceiptsExtrapolationApi;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ReceiptsExtrapolationService implements ReceiptsExtrapolationApi{
    private final ReceiptsExtrapolationDAO dao;

    @Inject
    public ReceiptsExtrapolationService(final ReceiptsExtrapolationDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<ReceiptsExtrapolationAllShopsResult> extrapolateInAllShops() {
        return dao.extrapolateByAllShops().stream()
                .map(are -> new ReceiptsExtrapolationAllShopsResult(
                        LocalDate.of(are.getYear(), are.getMonth(), are.getDay())
                                .atStartOfDay()
                                .toEpochSecond(ZoneOffset.UTC),
                        are.getTotalSum())
                ).collect(toList());
    }
}

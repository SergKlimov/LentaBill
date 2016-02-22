package com.kspt.it.dao.aggregation.receipts;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;

import javax.inject.Inject;
import java.util.List;

public class ReceiptsExtrapolationDAO {
    private final EbeanServer ebean;

    @Inject
    public ReceiptsExtrapolationDAO(final EbeanServer ebean) {
        this.ebean = ebean;
    }

    public List<ReceiptsExtrapolationAllShopsEntry> extrapolateByAllShops() {
        //TODO write query
        final String query = "SELECT ";
        final RawSql sql = RawSqlBuilder.parse(query).create();
        return ebean.find(ReceiptsExtrapolationAllShopsEntry.class)
                .setRawSql(sql)
                .findList();
    }
}

package com.kspt.it.dao.meta;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import javax.inject.Inject;

public class ChecksMetaDAO {

  private final EbeanServer ebean;

  @Inject
  public ChecksMetaDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public FirstCheckOrigin getFirstCheckOrigin() {
    final String query = "SELECT "
        + "MIN(as_millisecond) "
        + "FROM "
        + "date_dimensions";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(FirstCheckOrigin.class)
        .setRawSql(sql)
        .findUnique();
  }
}

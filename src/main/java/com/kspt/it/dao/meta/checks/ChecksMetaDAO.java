package com.kspt.it.dao.meta.checks;

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

  public CheckOriginEntry getFirstCheckOrigin() {
    final String query = "SELECT "
        + "MIN(as_millisecond) as origin "
        + "FROM "
        + "date_dimensions";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    System.out.println("sql: "+sql.toString());
    //return ebean.find(CheckOriginEntry.class)
        //.setRawSql(sql)
        //.findUnique();
    return new CheckOriginEntry(new Long(1456790422));
  }

  public CheckOriginEntry getLastCheckOrigin() {
    final String query = "SELECT "
        + "MAX(as_millisecond) as origin "
        + "FROM "
        + "date_dimensions";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    //return ebean.find(CheckOriginEntry.class)
        //.setRawSql(sql)
        //.findUnique();
    return new CheckOriginEntry(new Long(1459468508));
  }
}

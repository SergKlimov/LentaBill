package com.kspt.it.dao.meta;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;
import javax.inject.Inject;
import javax.persistence.Entity;

public class ChecksMetaDAO {

  private final EbeanServer ebean;

  @Inject
  public ChecksMetaDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public CheckOriginEntry getFirstCheckOrigin() {
    final String query = "SELECT "
        + "MIN(as_millisecond) "
        + "FROM "
        + "date_dimensions";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(CheckOriginEntry.class)
        .setRawSql(sql)
        .findUnique();
  }

  public CheckOriginEntry getLastCheckOrigin() {
    final String query = "SELECT "
        + "MAX(as_millisecond) "
        + "FROM "
        + "date_dimensions";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(CheckOriginEntry.class)
        .setRawSql(sql)
        .findUnique();
  }

  @Entity
  @Sql
  public static class CheckOriginEntry {

    private final Long origin;

    public CheckOriginEntry(final Long origin) {
      this.origin = origin;
    }

    public Long getOrigin() {
      return origin;
    }
  }
}

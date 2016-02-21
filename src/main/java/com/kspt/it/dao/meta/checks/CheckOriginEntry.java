package com.kspt.it.dao.meta.checks;

import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

@Entity
@Sql
public class CheckOriginEntry {

  private final Long origin;

  public CheckOriginEntry(final Long origin) {
    this.origin = origin;
  }

  public Long getOrigin() {
    return origin;
  }
}

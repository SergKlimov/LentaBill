package com.kspt.it.dao.meta;

import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

@Entity
@Sql
public class CheckOrigin {

  private final Long origin;

  public CheckOrigin(final Long origin) {
    this.origin = origin;
  }

  public Long getOrigin() {
    return origin;
  }
}

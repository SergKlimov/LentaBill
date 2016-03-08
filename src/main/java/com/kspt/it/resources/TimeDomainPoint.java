package com.kspt.it.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class TimeDomainPoint {
  private Long ts;

  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double v;

  public TimeDomainPoint(final Long ts, final Double v) {
    this.ts = ts;
    this.v = v;
  }

  public TimeDomainPoint() {
  }
}

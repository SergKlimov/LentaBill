package com.kspt.it.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
class TimeDomainPoint {
  @XmlElement(name = "ts")
  private Long origin;

  @XmlElement(name = "v")
  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double value;

  public TimeDomainPoint(final Long origin, final Double value) {
    this.origin = origin;
    this.value = value;
  }

  public TimeDomainPoint() {
  }
}

@XmlRootElement(name = "points")
@XmlAccessorType(XmlAccessType.FIELD)
class TimeDomainPoints {

  @XmlElement(name = "p")
  @XmlElementWrapper(name = "ps")
  private List<TimeDomainPoint> points;

  public TimeDomainPoints(final List<TimeDomainPoint> points) {
    this.points = points;
  }

  public TimeDomainPoints() {
  }
}
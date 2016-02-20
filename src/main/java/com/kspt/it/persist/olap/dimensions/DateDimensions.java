package com.kspt.it.persist.olap.dimensions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "date_dimensions")
public class DateDimensions {

  @Id
  private final Integer id;

  @Column(nullable = false, length = 4)
  private final Integer year;

  @Column(nullable = false, length = 2)
  private final Integer month;

  @Column(nullable = false, length = 2)
  private final Integer day;

  @Column(nullable = false, length = 2)
  private final Integer hour;

  @Column(nullable = false, length = 2)
  private final Integer minute;

  @Column(nullable = false, length = 2)
  private final Integer second;

  @Column(nullable = false)
  private final Long asMillisecond;

  public DateDimensions(
      final Integer id,
      final Integer year,
      final Integer month,
      final Integer day,
      final Integer hour,
      final Integer minute,
      final Integer second,
      final Long asMillisecond) {
    this.id = id;
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
    this.asMillisecond = asMillisecond;
  }

  public Integer getId() {
    return id;
  }

  public Integer getYear() {
    return year;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getDay() {
    return day;
  }

  public Integer getHour() {
    return hour;
  }

  public Integer getMinute() {
    return minute;
  }

  public Integer getSecond() {
    return second;
  }

  public Long getAsMillisecond() {
    return asMillisecond;
  }
}
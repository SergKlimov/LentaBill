package com.kspt.it;

import static java.util.Objects.requireNonNull;

public class Tuple2<T1, T2> {
  public final T1 _1;

  public final T2 _2;

  public Tuple2(final T1 _1, final T2 _2) {
    this._1 = requireNonNull(_1);
    this._2 = requireNonNull(_2);
  }
}

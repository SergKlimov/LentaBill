package com.kspt.it;

import static java.util.Objects.requireNonNull;

public class Tuple3<T1, T2, T3> {
  public final T1 _1;

  public final T2 _2;

  public final T3 _3;

  public Tuple3(final T1 _1, final T2 _2, final T3 _3) {
    this._1 = requireNonNull(_1);
    this._2 = requireNonNull(_2);
    this._3 = requireNonNull(_3);
  }
}

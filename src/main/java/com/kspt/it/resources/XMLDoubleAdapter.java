package com.kspt.it.resources;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XMLDoubleAdapter extends XmlAdapter<String, Double> {
  @Override
  public Double unmarshal(final String v)
  throws Exception {
    return null;
  }

  @Override
  public String marshal(final Double v)
  throws Exception {
    return String.format("%.2f", v);
  }
}

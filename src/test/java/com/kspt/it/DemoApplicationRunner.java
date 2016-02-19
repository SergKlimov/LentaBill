package com.kspt.it;

import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoApplicationRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationRunner.class);

  public static void main(String[] args)
  throws Exception {
    try {
      final String ymlConfigFile = Resources.getResource("config.yml").getFile();
      new DemoApplication().run("server", ymlConfigFile);
    } catch (Exception e) {
      LOGGER.error("Error occurred", e);
      throw new RuntimeException(e);
    }
  }
}
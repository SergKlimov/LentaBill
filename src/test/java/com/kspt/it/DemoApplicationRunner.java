package com.kspt.it;

import com.google.common.io.Resources;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.setup.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoApplicationRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationRunner.class);

  public static void main(String[] args)
  throws Exception {
    try {
      final String ymlConfigFile = Resources.getResource("config.yml").getFile();
      new TestDemoApplication().run("server", ymlConfigFile);
    } catch (Exception e) {
      LOGGER.error("Error occurred", e);
      throw new RuntimeException(e);
    }
  }
}

class TestDemoApplication extends DemoApplication {
  @Override
  public void initialize(final Bootstrap<DemoApplicationConfig> bootstrap) {
    GuiceBundle<DemoApplicationConfig> bundle = GuiceBundle.<DemoApplicationConfig>newBuilder()
        .addModule(Modules.override(new GuiceModule()).with(new TestGuiceModule()))
        .build(Stage.PRODUCTION);
    bootstrap.addBundle(bundle);
  }
}
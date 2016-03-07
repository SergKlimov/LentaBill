package com.kspt.it;

import com.google.common.io.Resources;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoApplicationRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationRunner.class);

  private static final String RESOURCES_PACKAGE = "com.kspt.it.resources";

  public static void main(String[] args)
  throws Exception {
    setupSwagger();
    try {
      final String ymlConfigFile = Resources.getResource("config.yml").getFile();
      new TestDemoApplication().run("server", ymlConfigFile);
    } catch (Exception e) {
      LOGGER.error("Error occurred", e);
      throw new RuntimeException(e);
    }
  }

  private static void setupSwagger() {
    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setBasePath("/api");
    beanConfig.setVersion("1.0");
    beanConfig.setSchemes(new String[]{"http"});
    beanConfig.setHost("localhost:8080");
    beanConfig.setResourcePackage(RESOURCES_PACKAGE);
    beanConfig.setScan(true);
  }
}

class TestDemoApplication extends DemoApplication {
  @Override
  public void initialize(final Bootstrap<DemoApplicationConfig> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/assets/", "/", "main.html"));
    GuiceBundle<DemoApplicationConfig> bundle = GuiceBundle.<DemoApplicationConfig>newBuilder()
        .addModule(Modules.override(new GuiceModule()).with(new TestGuiceModule()))
        .build(Stage.DEVELOPMENT);
    bootstrap.addBundle(bundle);
  }

  @Override
  protected void setupJersey(final Environment environment) {
    super.setupJersey(environment);
    environment.jersey().register(ApiListingResource.class);
    environment.jersey().register(SwaggerSerializers.class);
  }
}
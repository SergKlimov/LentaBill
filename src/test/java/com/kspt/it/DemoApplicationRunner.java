package com.kspt.it;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.kspt.it.persist.data.CashMachineEntry;
import com.kspt.it.persist.data.CheckEntry;
import com.kspt.it.persist.data.ProductEntry;
import com.kspt.it.persist.data.SupplierDimensions;
import com.kspt.it.persist.olap.dimensions.DateDimensions;
import com.kspt.it.persist.olap.dimensions.StoreEntry;
import com.kspt.it.persist.olap.facts.CheckFactEntry;
import com.kspt.it.persist.olap.facts.ProductFactEntry;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.lang.System.currentTimeMillis;
import static java.time.ZoneId.systemDefault;

public class DemoApplicationRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationRunner.class);

  private static final String RESOURCES_PACKAGE = "com.kspt.it.resources";

  private static EbeanServer ebean;

  public static void main(String[] args)
  throws Exception {
    //localEbean lEbean = new localEbean(ebean);


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
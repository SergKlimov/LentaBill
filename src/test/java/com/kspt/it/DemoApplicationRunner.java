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
      //ebean = lEbean.getEbean();
      ebean = mySqlEbeanServer();
      //setupEbean();

      //read products from db
      List<ProductEntry> peList = new ArrayList<>();
      peList.addAll(ebean.find(ProductEntry.class).setRawSql(RawSqlBuilder.parse("SELECT "
              + "products.id AS id, "
              + "products.name AS name, "
              + "products.value AS value FROM products"
      ).create()).findList());
      for(ProductEntry pe : peList){
        System.out.println("product " + pe.getId().toString() + " : " + pe.getName() + " value: " + pe.getValue());
      }

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

  private static void setupEbean(){
    createProducts();
    createDateDimensions();
    createSupplierDimensions();
    createChecks();
    createFacts();
  }

  private static void createFacts() {
    System.out.println("creating facts");
    createCheckFacts();
    createProductFacts();
  }

  private static void createProductFacts() {
    ebean.find(CheckEntry.class).findList().stream()
            .flatMap(ch -> ebean.find(ProductEntry.class).findList().stream()
                    .map(p -> {
                      final SupplierDimensions supplier = ebean.find(SupplierDimensions.class)
                              .where()
                              .eq("cash_machine_id", ch.getCashMachine().getId())
                              .findUnique();
                      final Instant i = Instant.ofEpochMilli(ch.getOrigin());
                      final LocalDateTime ldt = LocalDateTime.ofInstant(i, systemDefault());
                      final DateDimensions date = ebean.find(DateDimensions.class)
                              .where()
                              .eq("year", ldt.getYear())
                              .eq("month", ldt.getMonth())
                              .eq("day", ldt.getDayOfMonth())
                              .eq("hour", ldt.getHour())
                              .eq("minute", ldt.getMinute())
                              .eq("second", ldt.getSecond())
                              .findUnique();
                      final Double v = ch.getValue();
                      return new ProductFactEntry(null, date, supplier, p, v, v / (2 * p.getValue()));
                    })).forEach(ebean::save);
  }

  private static void createCheckFacts() {
    ebean.find(CheckEntry.class).findList().stream()
            .map(ch -> {
              final SupplierDimensions supplier = ebean.find(SupplierDimensions.class)
                      .where()
                      .eq("cash_machine_id", ch.getCashMachine().getId())
                      .findUnique();
              final Instant i = Instant.ofEpochMilli(ch.getOrigin());
              final LocalDateTime ldt = LocalDateTime.ofInstant(i, systemDefault());
              final DateDimensions date = ebean.find(DateDimensions.class)
                      .where()
                      .eq("year", ldt.getYear())
                      .eq("month", ldt.getMonth())
                      .eq("day", ldt.getDayOfMonth())
                      .eq("hour", ldt.getHour())
                      .eq("minute", ldt.getMinute())
                      .eq("second", ldt.getSecond())
                      .findUnique();
              return new CheckFactEntry(null, date, supplier, ch.getValue());
            }).forEach(ebean::save);
  }

  private static void createDateDimensions() {
    ebean.find(CheckEntry.class).findList().stream()
            .map(CheckEntry::getOrigin)
            .map(Instant::ofEpochMilli)
            .map(i -> LocalDateTime.ofInstant(i, systemDefault()))
            .map(ldt -> new DateDimensions(
                    null,
                    ldt.getYear(),
                    ldt.getMonthValue(),
                    ldt.getDayOfMonth(),
                    ldt.getHour(),
                    ldt.getMinute(),
                    ldt.getSecond(),
                    ldt.toInstant(ZoneOffset.UTC).toEpochMilli())).forEach(ebean::save);
  }

  private static void createChecks() {
    System.out.println("creating checks");
    final List<CashMachineEntry> crs = ebean.find(CashMachineEntry.class).findList();
    IntStream.range(0, crs.size())
            .mapToObj(i -> new Tuple2<>(i, crs.get(i)))
            .map(t2 -> new CheckEntry(null, currentTimeMillis() + t2._1 * 1000, t2._2, 100.0))
            .forEach(ebean::save);
  }

  private static void createProducts() {
    System.out.println("creating products");
    final ProductEntry p1 = new ProductEntry(null, "p1", 1.0);
    final ProductEntry p2 = new ProductEntry(null, "p2", 5.0);
    ebean.save(p1);
    ebean.save(p2);
  }

  private static void createSupplierDimensions() {
    createStores();
    createCashMachines();
    fillSupplierDimensions();
  }

  private static void fillSupplierDimensions() {
    ebean.find(CashMachineEntry.class).findList().stream()
            .map(cm -> new SupplierDimensions(null, cm.getStore(), cm))
            .forEach(ebean::save);
  }

  private static void createStores() {
    System.out.println("creating stores");
    final StoreEntry s1 = new StoreEntry(null, "store1");
    final StoreEntry s2 = new StoreEntry(null, "store2");
    ebean.save(s1);
    ebean.save(s2);
  }

  private static void createCashMachines() {
    ebean.find(StoreEntry.class).findList().stream()
            .flatMap(s -> Lists
                    .newArrayList(
                            new CashMachineEntry(null, UUID.randomUUID().toString(), s),
                            new CashMachineEntry(null, UUID.randomUUID().toString(), s)
                    ).stream()
            ).forEach(ebean::save);
  }

  static EbeanServer mySqlEbeanServer() {
    final String databaseName = "adler1_lenta2";
    final ServerConfig config = new ServerConfig();
    config.setName(databaseName);
    // Define DataSource parameters
    DataSourceConfig h2DataSourceConfig = new DataSourceConfig();
    h2DataSourceConfig.setDriver("com.mysql.jdbc.Driver");
    h2DataSourceConfig.setUsername("adler1_lenta2");
    h2DataSourceConfig.setPassword("lenta2pass");
    h2DataSourceConfig.setUrl(String.format("jdbc:mysql://jacket.beget.ru:3306/%s", databaseName));

    System.out.println(String.format("server url: jdbc:mysql://jacket.beget.ru:3306/%s", databaseName));

    config.setDataSourceConfig(h2DataSourceConfig);
    // specify a JNDI DataSource
    // config.setDataSourceJndiName("someJndiDataSourceName");
    // set DDL options...
    config.setDdlGenerate(false);
    config.setDdlRun(false);
    config.setDefaultServer(false);
    config.setRegister(false);
    // create the EbeanServer instance
    final EbeanServer ebeanServer = EbeanServerFactory.create(config);
    return ebeanServer;
  }

  static EbeanServer ebeanServer() {
    //final String databaseName = UUID.randomUUID().toString().replaceAll("-", "");
    final String databaseName = "test";
    final ServerConfig config = new ServerConfig();
    config.setName(databaseName);
    // Define DataSource parameters
    DataSourceConfig h2DataSourceConfig = new DataSourceConfig();
    h2DataSourceConfig.setDriver("org.h2.Driver");
    h2DataSourceConfig.setUsername("sa");
    h2DataSourceConfig.setPassword("");
    h2DataSourceConfig.setUrl(String.format("jdbc:h2:mem:%s;MODE=MySQL", databaseName));

    System.out.println("server url:" + String.format("jdbc:h2:mem:%s", databaseName));

    config.setDataSourceConfig(h2DataSourceConfig);
    // specify a JNDI DataSource
    // config.setDataSourceJndiName("someJndiDataSourceName");
    // set DDL options...
    config.setDdlGenerate(true);
    config.setDdlRun(false);
    config.setDefaultServer(false);
    config.setRegister(false);
    // create the EbeanServer instance
    final EbeanServer ebeanServer = EbeanServerFactory.create(config);
    DdlGenerator ddl = new DdlGenerator();
    ddl.setup((SpiEbeanServer) ebeanServer, new H2Platform(), config);
    // Drop
    ddl.runScript(false, ddl.generateDropDdl());
    // Create
    ddl.runScript(false, ddl.generateCreateDdl());
    ((SpiEbeanServer) ebeanServer).clearQueryStatistics();
    ebeanServer.getServerCacheManager().clearAll();
    return ebeanServer;
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
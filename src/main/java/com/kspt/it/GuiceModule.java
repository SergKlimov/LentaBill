package com.kspt.it;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kspt.it.dao.aggregation.ByDateAndProductAggregationDAO;
import com.kspt.it.dao.aggregation.ByDateAndStoreAggregationDAO;
import com.kspt.it.dao.aggregation.ByDateAndStoreAndProductAggregationDAO;
import com.kspt.it.dao.aggregation.ByStoreAndProductAggregationDAO;
import com.kspt.it.dao.meta.ChecksMetaDAO;
import com.kspt.it.dao.meta.StoresMetaDAO;
import com.kspt.it.services.aggregation.ByDateAndProductAggregationApi;
import com.kspt.it.services.aggregation.ByDateAndStoreAggregationApi;
import com.kspt.it.services.aggregation.ByDateAndStoreAndProductAggregationApi;
import com.kspt.it.services.aggregation.ByStoreAndProductAggregationApi;
import com.kspt.it.services.aggregation.real.ByDateAndProductAggregationService;
import com.kspt.it.services.aggregation.real.ByDateAndStoreAggregationService;
import com.kspt.it.services.aggregation.real.ByDateAndStoreAndProductAggregationService;
import com.kspt.it.services.aggregation.real.ByStoreAndProductAggregationService;
import com.kspt.it.services.aggregation.synthetic.SyntheticByDateAndProductAggregationApi;
import com.kspt.it.services.aggregation.synthetic.SyntheticByDateAndStoreAggregationApi;
import com.kspt.it.services.aggregation.synthetic.SyntheticByDateAndStoreAndProductAggregationApi;
import com.kspt.it.services.aggregation.synthetic.SyntheticByStoreAndProductAggregationApi;
import com.kspt.it.services.meta.MetaRetrievingApi;
import com.kspt.it.services.meta.real.MetaRetrievingService;
import com.kspt.it.services.meta.synthetic.SyntheticMetaRetrievingApi;
import com.typesafe.config.Config;
import static com.typesafe.config.ConfigFactory.parseResources;
import com.typesafe.config.ConfigParseOptions;
import static java.lang.Thread.currentThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class GuiceModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(GuiceModule.class);

  @Provides
  @Singleton
  public ByDateAndStoreAggregationApi provideChecksAggregationApi(final Config c,
      final Injector i) {
    final String type = c.getString("services.aggregation.type");
    if (type.equals("real")) {
      return new ByDateAndStoreAggregationService(
          i.getInstance(ByDateAndStoreAggregationDAO.class));
    } else {
      final Config serviceConfig = c.getConfig("services_types." + type);
      final int storesCount = serviceConfig.getInt("stores_count");
      return new SyntheticByDateAndStoreAggregationApi(storesCount);
    }
  }

  @Provides
  @Singleton
  public ByDateAndProductAggregationApi provideByDateAndProductAggregationApi(
      final Config c,
      final Injector i) {
    final String type = c.getString("services.aggregation.type");
    if (type.equals("real")) {
      return new ByDateAndProductAggregationService(
          i.getInstance(ByDateAndProductAggregationDAO.class));
    } else {
      final Config serviceConfig = c.getConfig("services_types." + type);
      final int daysCount = serviceConfig.getInt("days_count");
      final int storesCount = serviceConfig.getInt("stores_count");
      final int productsCount = serviceConfig.getInt("products_count");
      return new SyntheticByDateAndProductAggregationApi(daysCount, storesCount, productsCount);
    }
  }

  @Provides
  @Singleton
  public ByDateAndStoreAndProductAggregationApi provideByDateAndStoreAndProductAggregationApi(
      final Config c,
      final Injector i) {
    final String type = c.getString("services.aggregation.type");
    if (type.equals("real")) {
      return new ByDateAndStoreAndProductAggregationService(
          i.getInstance(ByDateAndStoreAndProductAggregationDAO.class));
    } else {
      final Config serviceConfig = c.getConfig("services_types." + type);
      final int daysCount = serviceConfig.getInt("days_count");
      final int storesCount = serviceConfig.getInt("stores_count");
      final int productsCount = serviceConfig.getInt("products_count");
      return new SyntheticByDateAndStoreAndProductAggregationApi(
          daysCount, storesCount, productsCount);
    }
  }

  @Provides
  @Singleton
  public ByStoreAndProductAggregationApi provideByStoreAndProductAggregationApi(
      final Config c,
      final Injector i) {
    final String type = c.getString("services.aggregation.type");
    if (type.equals("real")) {
      return new ByStoreAndProductAggregationService(
          i.getInstance(ByStoreAndProductAggregationDAO.class));
    } else {
      final Config serviceConfig = c.getConfig("services_types." + type);
      final int daysCount = serviceConfig.getInt("days_count");
      final int storesCount = serviceConfig.getInt("stores_count");
      final int productsCount = serviceConfig.getInt("products_count");
      return new SyntheticByStoreAndProductAggregationApi(daysCount, storesCount, productsCount);
    }
  }

  @Provides
  @Singleton
  public MetaRetrievingApi provideMetaRetrievingApi(final Config c, final Injector i) {
    final String type = c.getString("services.meta.type");
    if (type.equals("real")) {
      return new MetaRetrievingService(
          i.getInstance(ChecksMetaDAO.class),
          i.getInstance(StoresMetaDAO.class), productsDao);
    } else {
      final Config serviceConfig = c.getConfig("services_types." + type);
      final int storesCount = serviceConfig.getInt("stores_count");
      return new SyntheticMetaRetrievingApi(storesCount, productsCount);
    }
  }

  @Provides
  @Singleton
  EbeanServer provideEbeanServer() {
    final Stopwatch started = Stopwatch.createStarted();
    final EbeanServer server = Ebean.getServer(null);
    checkNotNull(server,
        "Ebean provider returned null. Possible no ebean.property file in resource folder");
    LOGGER.warn("Elapsed time to create ebean server {}", started);
    return server;
  }

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  Config provideApplicationConfig() {
    final String appConf = "app.conf";
    final ConfigParseOptions options = ConfigParseOptions.defaults().setAllowMissing(false);
    final ClassLoader classLoader = currentThread().getContextClassLoader();
    final Config c = parseResources(classLoader, appConf, options);
    final File folder = new File(c.origin().filename()).getParentFile();
    LOGGER.warn("Loading {} from {}", appConf, folder);
    return c.resolve();
  }
}

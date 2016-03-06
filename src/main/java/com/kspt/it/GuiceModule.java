package com.kspt.it;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kspt.it.dao.aggregation.checks.ChecksAggregationDAO;
import com.kspt.it.dao.aggregation.products.ProductsAggregationDAO;
import com.kspt.it.dao.aggregation.receipts.ReceiptsExtrapolationDAO;
import com.kspt.it.dao.meta.checks.ChecksMetaDAO;
import com.kspt.it.dao.meta.stores.StoresMetaDAO;
import com.kspt.it.services.checks.ChecksAggregationApi;
import com.kspt.it.services.checks.real.ChecksAggregationService;
import com.kspt.it.services.checks.synthetic.SyntheticChecksAggregationApi;
import com.kspt.it.services.meta.MetaRetrievingApi;
import com.kspt.it.services.meta.real.MetaRetrievingService;
import com.kspt.it.services.meta.synthetic.SyntheticMetaRetrievingApi;
import com.kspt.it.services.products.ProductsAggregationApi;
import com.kspt.it.services.products.real.ProductsAggregationService;
import com.kspt.it.services.products.synthetic.SyntheticProductsAggregationApi;
import com.kspt.it.services.receipts.ReceiptsExtrapolationApi;
import com.kspt.it.services.receipts.real.ReceiptsExtrapolationService;
import com.kspt.it.services.receipts.synthetic.SyntheticReceiptsExtrapolationApi;
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
  public ChecksAggregationApi provideChecksAggregationApi(final Config c, final Injector i) {
    final String type = c.getString("services.checks.type");
    if (type.equals("real")) {
      return new ChecksAggregationService(i.getInstance(ChecksAggregationDAO.class));
    } else {
      final Config serviceConfig = c.getConfig("services_types." + type);
      final int storesCount = serviceConfig.getInt("stores_count");
      return new SyntheticChecksAggregationApi(storesCount);
    }
  }

  @Provides
  @Singleton
  public ProductsAggregationApi provideProductsAggregationApi(final Config c, final Injector i) {
    final String type = c.getString("services.products.type");
    if (type.equals("real")) {
      return new ProductsAggregationService(i.getInstance(ProductsAggregationDAO.class));
    } else {
      final Config serviceConfig = c.getConfig("services_types." + type);
      final int daysCount = serviceConfig.getInt("days_count");
      final int storesCount = serviceConfig.getInt("stores_count");
      final int productsCount = serviceConfig.getInt("products_count");
      return new SyntheticProductsAggregationApi(daysCount, storesCount, productsCount);
    }
  }

  @Provides
  @Singleton
  public MetaRetrievingApi provideMetaRetrievingApi(final Config c, final Injector i) {
    final String type = c.getString("services.meta.type");
    if (type.equals("real")) {
      return new MetaRetrievingService(
          i.getInstance(ChecksMetaDAO.class),
          i.getInstance(StoresMetaDAO.class));
    } else {
      final Config serviceConfig = c.getConfig("services_types." + type);
      final int storesCount = serviceConfig.getInt("stores_count");
      return new SyntheticMetaRetrievingApi(storesCount);
    }
  }

  @Provides
  @Singleton
  public ReceiptsExtrapolationApi provideReceiptsExtrapolationApi(final Config c, final Injector i) {
    final String type = c.getString("services.products.type");
    if (type.equals("real")) {
      return new ReceiptsExtrapolationService(i.getInstance(ReceiptsExtrapolationDAO.class));
    } else {
      return new SyntheticReceiptsExtrapolationApi();
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

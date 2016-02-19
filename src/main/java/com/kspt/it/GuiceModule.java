package com.kspt.it;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kspt.it.services.ChecksAggregationService;
import com.kspt.it.services.ProductsAggregationService;
import com.kspt.it.services.checks.ChecksAggregationApi;
import com.kspt.it.services.products.ProductsAggregationApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiceModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(GuiceModule.class);

  @Override
  protected void configure() {
    bind(ProductsAggregationApi.class).to(ProductsAggregationService.class);
    bind(ChecksAggregationApi.class).to(ChecksAggregationService.class);
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
}

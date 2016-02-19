package com.kspt.it;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiceModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(GuiceModule.class);

  @Override
  protected void configure() {

  }

  @Provides
  @Singleton
  EbeanServer provideEbeanServer() {
    /*final Stopwatch started = Stopwatch.createStarted();
    final EbeanServer server = Ebean.getServer(null);
    checkNotNull(server,
        "Ebean provider returned null. Possible no ebean.property file in resource folder");
    LOGGER.warn("Elapsed time to create ebean server {}", started);
    return server;*/

    final String databaseName = "test";
    final ServerConfig config = new ServerConfig();
    config.setName(databaseName);
    // Define DataSource parameters
    DataSourceConfig h2DataSourceConfig = new DataSourceConfig();
    h2DataSourceConfig.setDriver("org.h2.Driver");
    h2DataSourceConfig.setUsername("sa");
    h2DataSourceConfig.setPassword("");
    h2DataSourceConfig.setUrl(String.format("jdbc:h2:mem:%s", databaseName));
    config.setDataSourceConfig(h2DataSourceConfig);
    // specify a JNDI DataSource
    // config.setDataSourceJndiName("someJndiDataSourceName");
    // set DDL options...
    config.setDdlGenerate(true);
    config.setDdlRun(false);
    config.setDefaultServer(false);
    config.setRegister(false);
    // specify jars to search for entity beans
    //config.addJar("elvis-commons-0.1-SNAPSHOT.jar");
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

package com.kspt.it;

import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import static org.eclipse.jetty.servlets.CrossOriginFilter.*;
import org.glassfish.jersey.server.validation.ValidationFeature;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import java.util.EnumSet;

public class DemoApplication extends Application<DemoApplicationConfig> {

  private static final String RESOURCES_PACKAGE = "com.kspt.it.resources";

  @Override
  public String getName() {
    return "Demo Application";
  }

  @Override
  public void initialize(final Bootstrap<DemoApplicationConfig> bootstrap) {
    GuiceBundle<DemoApplicationConfig> bundle = GuiceBundle.<DemoApplicationConfig>newBuilder()
        .addModule(new GuiceModule())
        .build(Stage.PRODUCTION);
    bootstrap.addBundle(bundle);
  }

  @Override
  public void run(final DemoApplicationConfig configuration, final Environment environment)
  throws Exception {
    setupJersey(environment);
    setupCORSFilter(environment);
    setupSwagger();
  }

  protected void setupJersey(final Environment environment) {
    environment.jersey().packages(RESOURCES_PACKAGE);
    environment.jersey().enable(ValidationFeature.class.getName());
    environment.jersey().register(ApiListingResource.class);
    environment.jersey().register(SwaggerSerializers.class);
  }

  private void setupCORSFilter(final Environment environment) {
    Dynamic filter = environment.servlets().addFilter("CORSFilter", CrossOriginFilter.class);
    final String urlPattern = environment.getApplicationContext().getContextPath() + "*";
    filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, urlPattern);
    filter.setInitParameter(ALLOWED_METHODS_PARAM, "GET,PUT,POST,OPTIONS");
    filter.setInitParameter(ALLOWED_ORIGINS_PARAM, "null,localhost");
    filter.setInitParameter(ALLOWED_HEADERS_PARAM, "Origin, Content-Type, Accept");
    filter.setInitParameter(ALLOW_CREDENTIALS_PARAM, "true");
  }

  private void setupSwagger() {
    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setVersion("1.0");
    beanConfig.setSchemes(new String[]{"http"});
    beanConfig.setHost("localhost:8080");
    beanConfig.setResourcePackage(RESOURCES_PACKAGE);
    beanConfig.setScan(true);
    Json.mapper().registerModule(new JaxbAnnotationModule());
    Yaml.mapper().registerModule(new JaxbAnnotationModule());
  }

  public static void main(final String[] args)
  throws Exception {
    new DemoApplication().run(args);
  }
}

class DemoApplicationConfig extends Configuration {
}

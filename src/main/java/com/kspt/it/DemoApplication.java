package com.kspt.it;

import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
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
  }

  protected void setupJersey(final Environment environment) {
    environment.jersey().packages(RESOURCES_PACKAGE);
    environment.jersey().enable(ValidationFeature.class.getName());
  }

  private void setupCORSFilter(final Environment environment) {
    Dynamic filter = environment.servlets().addFilter("CORSFilter", CrossOriginFilter.class);
    final String urlPattern = environment.getApplicationContext().getContextPath() + "*";
    filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, urlPattern);
    filter.setInitParameter(ALLOWED_METHODS_PARAM, "GET,PUT,POST,OPTIONS");
    filter.setInitParameter(ALLOWED_ORIGINS_PARAM, "null,localhost,http://localhost:*");
    filter.setInitParameter(ALLOWED_HEADERS_PARAM, "Origin, Content-Type, Accept");
    filter.setInitParameter(ALLOW_CREDENTIALS_PARAM, "true");
  }

  public static void main(final String[] args)
  throws Exception {
    new DemoApplication().run(args);
  }
}

class DemoApplicationConfig extends Configuration {
}

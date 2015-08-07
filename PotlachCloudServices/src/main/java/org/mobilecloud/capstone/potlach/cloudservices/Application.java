package org.mobilecloud.capstone.potlach.cloudservices;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.mobilecloud.capstone.potlach.cloudservices.auth.OAuth2SecurityConfiguration;
import org.mobilecloud.capstone.potlach.cloudservices.json.ResourcesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Mongo;

@Import(OAuth2SecurityConfiguration.class)
//Tell Spring to automatically inject any dependencies that are marked in
//our classes with @Autowired
@EnableAutoConfiguration
//Tell Spring to automatically create a MongoDB implementation of our
//VideoRepository
@EnableMongoRepositories
// Tell Spring to turn on WebMVC (e.g., it should enable the DispatcherServlet
// so that requests can be routed to our Controllers)
@EnableWebMvc
// Tell Spring that this object represents a Configuration for the
// application
@Configuration
// Tell Spring to go and scan our controller package (and all sub packages) to
// find any Controllers or other components that are part of our applciation.
// Any class in this package that is annotated with @Controller is going to be
// automatically discovered and connected to the DispatcherServlet.
@ComponentScan({"org.mobilecloud.capstone.potlach.cloudservices", "org.mobilecloud.capstone.potlach.common"})
public class Application extends RepositoryRestMvcConfiguration {

	@Autowired
	private Environment env;
	
	private static final String MAX_REQUEST_SIZE = "150MB";
	
	// The app now requires that you pass the location of the keystore and
	// the password for your private key that you would like to setup HTTPS
	// with. In Eclipse, you can set these options by going to:
	//    1. Run->Run Configurations
	//    2. Under Java Applications, select your run configuration for this app
	//    3. Open the Arguments tab
	//    4. In VM Arguments, provide the following information to use the
	//       default keystore provided with the sample code:
	//
	//       -Dkeystore.file=src/main/resources/private/keystore -Dkeystore.pass=changeit
	//
	//    5. Note, this keystore is highly insecure! If you want more securtiy, you 
	//       should obtain a real SSL certificate:
	//
	//       http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
	//
	// Tell Spring to launch our app!
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
    // This version uses the Tomcat web container and configures it to
	// support HTTPS. The code below performs the configuration of Tomcat
	// for HTTPS. Each web container has a different API for configuring
	// HTTPS. 
	//
	// The app now requires that you pass the location of the keystore and
	// the password for your private key that you would like to setup HTTPS
	// with. In Eclipse, you can set these options by going to:
	//    1. Run->Run Configurations
	//    2. Under Java Applications, select your run configuration for this app
	//    3. Open the Arguments tab
	//    4. In VM Arguments, provide the following information to use the
	//       default keystore provided with the sample code:
	//
	//       -Dkeystore.file=src/main/resources/private/keystore -Dkeystore.pass=changeit
	//
	//    5. Note, this keystore is highly insecure! If you want more securtiy, you 
	//       should obtain a real SSL certificate:
	//
	//       http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
	//
    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer(
            @Value("${keystore.file:src/main/resources/private/keystore_potlach}") String keystoreFile,
            @Value("${keystore.pass:changeit}") final String keystorePass) throws Exception {

		// If you were going to reuse this class in another
		// application, this is one of the key sections that you
		// would want to change
    	
        final String absoluteKeystoreFile = new File(keystoreFile).getAbsolutePath();

        return new EmbeddedServletContainerCustomizer () {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
		            TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
		            tomcat.addConnectorCustomizers(
		                    new TomcatConnectorCustomizer() {
								@Override
								public void customize(Connector connector) {
									connector.setPort(8443);
			                        connector.setSecure(true);
			                        connector.setScheme("https");

			                        Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
			                        proto.setSSLEnabled(true);
			                        proto.setKeystoreFile(absoluteKeystoreFile);
			                        proto.setKeystorePass(keystorePass);
			                        proto.setKeystoreType("JKS");
			                        proto.setKeyAlias("tomcat");
								}
		                    });			            
			}
        };
    }
	
    // We are overriding the bean that RepositoryRestMvcConfiguration 
 	// is using to convert our objects into JSON so that we can control
 	// the format. The Spring dependency injection will inject our instance
 	// of ObjectMapper in all of the spring data rest classes that rely
 	// on the ObjectMapper. This is an example of how Spring dependency
 	// injection allows us to easily configure dependencies in code that
 	// we don't have easy control over otherwise.
 	@Override
 	public ObjectMapper halObjectMapper(){
 		return new ResourcesMapper();
 	} 
 	

	// This configuration element adds the ability to accept multipart
	// requests to the web container.
	@Bean
    public MultipartConfigElement multipartConfigElement() {
		// Setup the application container to be accept multipart requests
		final MultiPartConfigFactory factory = new MultiPartConfigFactory();
		// Place upper bounds on the size of the requests to ensure that
		// clients don't abuse the web container by sending huge requests
		factory.setMaxFileSize(MAX_REQUEST_SIZE);
		factory.setMaxRequestSize(MAX_REQUEST_SIZE);

		// Return the configuration to setup multipart in the container
		return factory.createMultipartConfig();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@SuppressWarnings("deprecation")
	public @Bean Mongo mongo() throws Exception {
        return new Mongo(env.getProperty("spring.data.mongodb.host"));
    }

    public @Bean MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), env.getProperty("potlach.db"));
    }
}

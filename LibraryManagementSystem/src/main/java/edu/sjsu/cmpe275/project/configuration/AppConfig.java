package edu.sjsu.cmpe275.project.configuration;

import java.util.Locale;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import edu.sjsu.cmpe275.project.converter.RoleToUserProfileConverter;

/**
 * @author Onkar Ganjewar
 */

@Configuration
@EnableWebMvc
@EnableAsync
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "edu.sjsu.cmpe275.project")
@PropertySource(value = { "classpath:email.properties" })
public class AppConfig extends WebMvcConfigurerAdapter{
	static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

	@Autowired
	private Environment environment;

	@Autowired
	RoleToUserProfileConverter roleToUserProfileConverter;

	/**
	 * Configure ViewResolvers to render the views
	 */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
	}
	/* Use this part for serving .html files */
//	viewResolver.setPrefix("");
//	viewResolver.setSuffix(""); 

	/**
	 * Configure ResourceHandlers to serve static resources.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}

	/**
	 * Configure Converter to be used. In our example, we need a converter to
	 * convert string values[Roles] to UserProfiles in newUser.jsp
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(roleToUserProfileConverter);
	}

	/**
	 * Configure MessageSource to lookup any validation/error message in
	 * internationalized property files
	 */
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		// messageSource.setBasename("classpath:messages");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(0);
		return messageSource;
	}

	/**
	 * Bean for the java mail implementation
	 * @return
	 */
	@Bean
	public JavaMailSenderImpl javaMailSenderImpl() {
		final JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();

		try {
			mailSenderImpl.setHost(environment.getRequiredProperty("smtp.host"));
			mailSenderImpl.setPort(environment.getRequiredProperty("smtp.port", Integer.class));
			mailSenderImpl.setProtocol(environment.getRequiredProperty("smtp.protocol"));
			mailSenderImpl.setUsername(environment.getRequiredProperty("smtp.username"));
			mailSenderImpl.setPassword(environment.getRequiredProperty("smtp.password"));
		} catch (IllegalStateException ise) {
			LOGGER.error("Could not resolve email.properties.  See email.properties.sample");
			throw ise;
		}
		final Properties javaMailProps = new Properties();
		javaMailProps.put("mail.smtp.auth", true);
		javaMailProps.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		javaMailProps.put("mail.smtp.starttls.enable", true);
		mailSenderImpl.setJavaMailProperties(javaMailProps);
		return mailSenderImpl;
	}

	/**
	 * Bean to set the default locale for the application. 
	 * @return
	 */
	@Bean
	public LocaleResolver localeResolver() {
		final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
		return cookieLocaleResolver;
	}
}

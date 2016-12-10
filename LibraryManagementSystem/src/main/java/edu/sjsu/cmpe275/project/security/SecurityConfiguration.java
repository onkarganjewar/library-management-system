package edu.sjsu.cmpe275.project.security;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @author Onkar Ganjewar
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;

	@Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
	@Autowired
	CustomSuccessHandler customSuccessHandler;
	
	@Autowired
	PersistentTokenRepository tokenRepository;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/demo*","/resources/**","/badUser*","/registrationConfirm*", "/signup").permitAll();
//		http.authorizeRequests().antMatchers("/newBook*").access("hasRole('ADMIN')").and().csrf().and().exceptionHandling().accessDeniedPage("/Access_Denied");
//		http.authorizeRequests().antMatchers("/return*","/confirmed*","/user*").access("hasRole('USER')").and().csrf().and().exceptionHandling().accessDeniedPage("/Access_Denied");
		http.authorizeRequests().antMatchers("/login**","/static/**","/resources/**","/badUser**","/registrationConfirm**", "/signup").permitAll();
		http.authorizeRequests().antMatchers("/librarian/**").access("hasRole('ADMIN')").and().csrf().and().exceptionHandling().accessDeniedPage("/Access_Denied");

		http.authorizeRequests().antMatchers("/patron/**").access("hasRole('USER')").and().formLogin().loginPage("/login")
		.loginProcessingUrl("/login").successHandler(customSuccessHandler).failureHandler(authenticationFailureHandler)
		.usernameParameter("email").passwordParameter("password").and()
		.rememberMe().rememberMeParameter("remember-me").tokenRepository(tokenRepository)
		.tokenValiditySeconds(86400).and().csrf().and().exceptionHandling().accessDeniedPage("/Access_Denied");
		
		http.authorizeRequests().antMatchers("/**")
				.access("hasRole('USER') or hasRole('ADMIN')")
				.and().formLogin().loginPage("/login")
				.loginProcessingUrl("/login").successHandler(customSuccessHandler).failureHandler(authenticationFailureHandler)
				.usernameParameter("email").passwordParameter("password").and()
				.rememberMe().rememberMeParameter("remember-me").tokenRepository(tokenRepository)
				.tokenValiditySeconds(86400).and().csrf().and().exceptionHandling().accessDeniedPage("/Access_Denied");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
		PersistentTokenBasedRememberMeServices tokenBasedservice = new PersistentTokenBasedRememberMeServices(
				"remember-me", userDetailsService, tokenRepository);
		return tokenBasedservice;
	}

	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver() {
		return new AuthenticationTrustResolverImpl();
	}

}

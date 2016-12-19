package edu.sjsu.cmpe275.project.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.sjsu.cmpe275.project.configuration.HibernateConfigTest;
import edu.sjsu.cmpe275.project.security.CustomUserDetailsService;

/**
 * @author Onkar Ganjewar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateConfigTest.class)
@WebAppConfiguration
public class LoginControllerTest {

	static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc springMvc;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Before
	public void setup() {
		this.springMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain)
				.apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@Test
	public void LoginTest() throws Exception {
		this.springMvc
				.perform(post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("email", "onkar.ganjewar@sjsu.edu")
				.param("password", "123")
				.with(csrf())
				.accept(MediaType.ALL))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("/librarian/home*"))
				.andDo(print());
	}

	@Test
	public void GET_SignUpTest() throws Exception {
		 this.springMvc
		 .perform(get("/signup").accept(MediaType.ALL)).andDo(print())
		 .andExpect(MockMvcResultMatchers.status().isOk())
		 .andExpect(MockMvcResultMatchers.view().name("signup"))
		 .andDo(print());
	}
	
	@Test
	public void POST_SignUpTest() throws Exception {
        this.springMvc
		.perform(post("/signup")
				.param("email", "ganjewaronkar@gmail.com")
				.param("firstName", "onkar")
				.param("lastName", "ganjewar")
				.param("password", "123")
				.param("uId", "456789").with(csrf())
				.accept(MediaType.ALL))
		.andDo(print()).andExpect(status().isOk());
	}
}

package edu.sjsu.cmpe275.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
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
public class LibrarianControllerTest {

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

	/**
	 * Test for librarian home page
	 * @throws Exception
	 */
	@Test
	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
    public void renderIndexTest() throws Exception {
        this.springMvc.perform(get("/librarian/home")
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(MockMvcResultMatchers.status().isOk())
        		.andExpect(MockMvcResultMatchers.view().name("admin"))
        		.andDo(print());
	}

	/**
	 * Test to check if the new book registration page is returned or not.
	 * @throws Exception
	 */
	@Test
	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
    public void renderBookRegistrationTest() throws Exception {
		this.springMvc.perform(get("/librarian/new-book")
				.accept(MediaType.ALL))
				.andDo(print()).andExpect(status().isOk())
				.andDo(print());
    }
	
	/**
	 * Test to fetch the search results for a book based on the title
	 * @throws Exception
	 */
	@Test
	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
    public void renderSearchResultsTest() throws Exception {
        this.springMvc.perform(get("/librarian/search-book-testBook")
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(MockMvcResultMatchers.status().isOk())
        		.andExpect(MockMvcResultMatchers.view().name("searchResults"))
        		.andDo(print());
	}

	/**
	 * Test to delete the book by id 
	 * @throws Exception
	 */
	@Test
	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
    public void deleteBookTest() throws Exception {
        this.springMvc.perform(get("/librarian/delete-book-10")
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(MockMvcResultMatchers.status().isOk())
        		.andExpect(MockMvcResultMatchers.view().name("admin"))
        		.andDo(print());
	}
	
	/**
	 * Test to GET the book details by ISBN
	 * @throws Exception
	 */
	@Test
	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
    public void renderBookByISBNTest() throws Exception {
        this.springMvc.perform(get("/librarian/bookInfo-0735619670")
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(MockMvcResultMatchers.status().isOk())
        		.andExpect(MockMvcResultMatchers.view().name("newBook"))
        		.andDo(print());
	}
	
}

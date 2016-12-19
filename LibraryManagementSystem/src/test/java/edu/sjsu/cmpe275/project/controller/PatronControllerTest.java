package edu.sjsu.cmpe275.project.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
public class PatronControllerTest {

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
	 * Test to get the index page for the patron
	 * @throws Exception
	 */
	@Test
	public void renderIndexTest() throws Exception {
        this.springMvc.perform(get("/patron/home")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(status().isOk()).andDo(print());
	}


	/**
	 * Test to get the checkout confirmation page
	 * @throws Exception
	 */
	@Test
	public void renderCheckoutCompleteTest() throws Exception {
        this.springMvc.perform(get("/patron/confirmedCheckout")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("bookId", "6")
				.param("userId", "1")
        		.accept(MediaType.ALL)).andDo(print())
		        .andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("confirmedCheckoutScreen"))
				.andDo(print());
	}
	

	/**
	 * Test to get the search results page
	 * @throws Exception
	 */
	@Test
	public void renderSearchedBooksTest() throws Exception {
        this.springMvc.perform(get("/patron/search-book-9")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(status().isOk()).andDo(print());
	}

	/**
	 * Test to get the checkout verification page
	 * @throws Exception
	 */
	@Test
	public void renderCheckoutConfirmationTest() throws Exception {
        this.springMvc.perform(get("/patron/checkout-book-8")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "ganjewaronkar@gmail.com")
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("bookCheckoutWindow"))
				.andDo(print());
	}	

	/**
	 * Test to checkout the book with given id
	 * @throws Exception
	 */
	@Test
	public void checkoutBookTest() throws Exception {
        this.springMvc.perform(get("/patron/confirm-checkout-book-8")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "3")
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(status().isOk()).andDo(print());
	}
	
	/**
	 * Test to get the list of checked out books for the user
	 * @throws Exception
	 */
	@Test
	public void renderCheckedOutBooksTest() throws Exception {
        this.springMvc.perform(get("/patron/viewCheckedOutBooks")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "3")
        		.accept(MediaType.ALL)).andDo(print())
		        .andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("checkedOutBooks"))
				.andDo(print());
	}

	/**
	 * Test to return the book with given id
	 * @throws Exception
	 */
	@Test
	public void returnBookTest() throws Exception {
        this.springMvc.perform(get("/patron/return-book-6")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "ganjewaronkar@gmail.com")
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(status().isOk()).andDo(print());
	}
	
	/**
	 * Test to add the book with given id to the cart
	 * @throws Exception
	 */
	@Test
	public void addToCartTest() throws Exception {
        this.springMvc.perform(get("/patron/addToCart-10")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "7")
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(status().isOk()).andDo(print());
	}
	
	/**
	 * Test to get the cart items for given user
	 * @throws Exception
	 */
	@Test
	public void viewCartTest() throws Exception {
        this.springMvc.perform(get("/patron/viewCart")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "7")
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(status().isOk()).andDo(print());
	}
	
	/**
	 * Test to renew the book with given id
	 * @throws Exception
	 */
	@Test
	public void renewBookTest() throws Exception {
        this.springMvc.perform(get("/patron/renew-book-6")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "3")
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(status().isOk()).andDo(print());
	}
	
	/**
	 * Test to add the user to the waiting list
	 * @throws Exception
	 */
	@Test
	public void addToWaitingListTest() throws Exception {
        this.springMvc.perform(get("/patron/add-to-waiting-list-6")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "3")
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(status().isOk()).andDo(print());
	}
	
	/**
	 * Test to remove the book with given id from the cart
	 * @throws Exception
	 */
	@Test
	public void removeBookFromCartTest() throws Exception {
        this.springMvc.perform(get("/patron/remove-from-cart-8")
        		.with(user("ganjewaronkar@gmail.com").roles("USER"))
        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "3")
        		.accept(MediaType.ALL)).andDo(print())
        		.andExpect(status().isOk()).andDo(print());
	}
	
}

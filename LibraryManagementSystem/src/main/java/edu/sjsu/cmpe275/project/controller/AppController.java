package edu.sjsu.cmpe275.project.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import edu.sjsu.cmpe275.project.email.OnRegistrationCompleteEvent;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.UserProfile;
import edu.sjsu.cmpe275.project.model.VerificationToken;
import edu.sjsu.cmpe275.project.service.UserProfileService;
import edu.sjsu.cmpe275.project.service.UserService;
import edu.sjsu.cmpe275.project.validation.UserValidator;

/**
 * @author Onkar Ganjewar
 * 
 */
@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

	@Autowired
	UserService userService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	UserProfileService userProfileService;

	HttpSession session;

	@Autowired
	MessageSource messageSource;

	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;

	@Autowired
	private UserValidator customValidator;

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(ModelMap model) {
		model.addAttribute("user", getPrincipal());
		return "admin";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String usersPage(ModelMap model) {
		model.addAttribute("user", getPrincipal());
		return "users";
	}

	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("loggedinuser", getPrincipal());
		return "userslist";
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public String signUp(ModelMap model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		return "signup";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
	public String signUp_POST(@Valid User user, BindingResult result, ModelMap model,
			final HttpServletRequest request) {

		customValidator.validate(user, result);
		if (result.hasErrors()) {
			return "signup";
		}
		
		userService.saveUser(user);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), getAppUrl(request)));

		model.addAttribute("success",
				"User " + user.getFirstName() + " " + user.getLastName() + " registered successfully");
		model.addAttribute("loggedinuser", user.getFirstName());
		return "signupsuccess";
	}

	@RequestMapping(value = "/registrationConfirm.html", method = RequestMethod.GET)
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
		Locale locale = request.getLocale();

		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
			model.addAttribute("message", message);
			return "redirect:/badUser.html?lang=" + locale.getLanguage();
			// return "badToken";
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
			model.addAttribute("message", messageValue);
			return "redirect:/badUser.html?lang=" + locale.getLanguage();
		}

		user.setEnabled(true);
		userService.updateUser(user);
		return "login";
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	/**
	 * This method will provide UserProfile list to views
	 */
	@ModelAttribute("roles")
	public List<UserProfile> initializeProfiles() {
		return userProfileService.findAll();
	}

	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "accessDenied";
	}

	/**
	 * This method handles login GET requests. If users is already logged-in and
	 * tries to goto login page again, will be redirected to list page.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {

		boolean adminExists = false;
		boolean userExists = false;
		List<UserProfile> allProfiles = new ArrayList<UserProfile>();

		// Populate user profiles if not already present
		allProfiles = userProfileService.findAll();
		for (UserProfile u : allProfiles) {
			if (u.getType().equals("ADMIN")) {
				adminExists = true;
				continue;
			} else if (u.getType().equals("USER")) {
				userExists = true;
				continue;
			}
		}

		UserProfile up = new UserProfile();
		up.setType("ADMIN");
		if (!adminExists)
			userProfileService.saveProfile(up);

		UserProfile usp = new UserProfile();
		usp.setType("USER");
		if (!userExists)
			userProfileService.saveProfile(usp);

		if (isCurrentAuthenticationAnonymous()) {
			return "login";
		} else {
			return "redirect:/list";
		}
	}

	/**
	 * This method handles logout requests. Toggle the handlers if you are
	 * RememberMe functionality is useless in your app.
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			// new SecurityContextLogoutHandler().logout(request, response,
			// auth);
			persistentTokenBasedRememberMeServices.logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}

	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	/**
	 * This method returns true if users is already authenticated [logged-in],
	 * else false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authenticationTrustResolver.isAnonymous(authentication);
	}
}
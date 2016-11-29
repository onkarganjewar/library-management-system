package edu.sjsu.cmpe275.project.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.UserProfile;
import edu.sjsu.cmpe275.project.service.UserService;

/**
 * @author Onkar Ganjewar
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserService userService;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

		try {
			final User user = userService.findByEmail(email);
			if (user == null) {
				throw new UsernameNotFoundException("No user found with username: " + email);
			}

			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					user.isEnabled(), true, true, true, getGrantedAuthorities(user));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (UserProfile userProfile : user.getUserProfiles()) {
			logger.info("UserProfile : {}", userProfile);
			authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
		}
		logger.info("authorities : {}", authorities);
		return authorities;
	}
}

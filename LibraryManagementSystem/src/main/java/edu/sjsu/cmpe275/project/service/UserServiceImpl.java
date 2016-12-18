package edu.sjsu.cmpe275.project.service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.dao.UserDao;
import edu.sjsu.cmpe275.project.dao.VerificationTokenDao;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.VerificationToken;

/**
 * @author Onkar Ganjewar
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private UserDao dao;

	@Autowired
	private VerificationTokenDao tokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	@Override
	public User findById(int id) {
		return dao.findById(id);
	}

	@Override
	public User findByEmail(String email) {
		User user = dao.findByEmail(email);
		return user;
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		dao.save(user);
	}

	@Override
	public void updateUser(User user) {
		sessionFactory.getCurrentSession().update(user);
		
//		User entity = dao.findById(user.getId());
//		if (entity != null) {
//			if (!user.getPassword().equals(entity.getPassword())) {
//				entity.setPassword(passwordEncoder.encode(user.getPassword()));
//			}
//			entity.setuId(user.getuId());
//			entity.setFirstName(user.getFirstName());
//			entity.setLastName(user.getLastName());
//			entity.setEmail(user.getEmail());
//			entity.setUserProfiles(user.getUserProfiles());
//			entity.setEnabled(user.isEnabled());
//		}
	}

	@Override
	public List<User> findAllUsers() {
		return dao.findAllUsers();
	}

	@Override
	public void createVerificationTokenForUser(final User user, final String token) {
		final VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}

	@Override
	public String validateVerificationToken(String token) {
		final VerificationToken verificationToken = tokenRepository.findByToken(token);
		if (verificationToken == null) {
			return TOKEN_INVALID;
		}

		final User user = verificationToken.getUser();
		final Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			tokenRepository.delete(verificationToken);
			return TOKEN_EXPIRED;
		}

		user.setEnabled(true);
		// tokenRepository.delete(verificationToken);
		dao.save(user);
		return TOKEN_VALID;
	}

	@Override
	public User findByToken(final String verificationToken) {
		final VerificationToken token = tokenRepository.findByToken(verificationToken);
		if (token != null) {
			return token.getUser();
		}
		return null;
	}

	@Override
	public VerificationToken getVerificationToken(final String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

	@Override
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
		VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
		vToken.updateToken(UUID.randomUUID().toString());
		vToken = tokenRepository.save(vToken);
		return vToken;
	}

	@Override
	public void generateFines(int userId, double fine) {
		User tempUser = dao.findById(userId);
		double totalFine = tempUser.getFine() + fine;
		tempUser.setFine(totalFine);
		sessionFactory.getCurrentSession().update(tempUser);
	}
}

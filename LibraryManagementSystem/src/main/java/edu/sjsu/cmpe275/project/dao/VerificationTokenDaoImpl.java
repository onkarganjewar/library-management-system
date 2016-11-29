package edu.sjsu.cmpe275.project.dao;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.VerificationToken;

/**
 * @author Onkar Ganjewar
 */
@Repository("verificationTokenDao")
@Transactional
public class VerificationTokenDaoImpl extends AbstractDao<Integer, VerificationToken> implements VerificationTokenDao {
	static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Override
	public VerificationToken findByToken(String token) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("token", token));
		return (VerificationToken) crit.uniqueResult();
	}

	@Override
	public VerificationToken findByUser(User user) {
		logger.info("User : {}", user);
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("user_id", user.getId()));
		VerificationToken token = (VerificationToken) crit.uniqueResult();
		if (token != null) {
			Hibernate.initialize(token.getExpiryDate());
		}
		return token;
	}

	@Override
	public VerificationToken save(VerificationToken verificationToken) {
		// TODO Auto-generated method stub
		persist(verificationToken);
		return verificationToken;
	}

}

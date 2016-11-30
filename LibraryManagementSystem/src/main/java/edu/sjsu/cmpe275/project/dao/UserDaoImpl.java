package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.User;


/**
 * @author Onkar Ganjewar
 */
@Repository("userDao")
@Transactional
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {

	static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	@Override
	public User findById(int id) {
		User user = getByKey(id);
		if(user!=null){
			Hibernate.initialize(user.getUserProfiles());
		}
		return user;
	}
	@Override
	public User findByEmail(String email) {
		logger.info("Email : {}", email);
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("email", email));
		User user = (User)crit.uniqueResult();
		if(user!=null){
			Hibernate.initialize(user.getUserProfiles());
		}
		return user;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllUsers() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("firstName"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<User> users = (List<User>) criteria.list();
		return users;
	}

	@Override
	public void save(User user) {
		persist(user);
	}

	/**
	 * Retrieves the user by the given university Id.
	 */
	@Override
	public User findByUnivId(String uId) {

		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("uId", uId));
		User user = (User) crit.uniqueResult();
		if (user != null) {
			Hibernate.initialize(user.getUserProfiles());
			logger.info("University Id : {}", user.getuId());
		}
		return user;
	}
}

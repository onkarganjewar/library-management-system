package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.UserProfile;

/**
 * @author Onkar Ganjewar
 */
@Repository("userProfileDao")
@Transactional
public class UserProfileDaoImpl extends AbstractDao<Integer, UserProfile> implements UserProfileDao {

	public UserProfile findById(int id) {
		return getByKey(id);
	}

	public UserProfile findByType(String type) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("type", type));
		return (UserProfile) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<UserProfile> findAll() {
		Criteria crit = createEntityCriteria();
		crit.addOrder(Order.asc("type"));
		return (List<UserProfile>) crit.list();
	}

	@Override
	public void save(UserProfile userProfile) {
		persist(userProfile);
	}

}

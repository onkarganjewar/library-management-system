package edu.sjsu.cmpe275.project.dao;

import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.VerificationToken;

/**
 * @author Onkar Ganjewar
 */
public interface VerificationTokenDao {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    VerificationToken save(VerificationToken verificationToken);

    void delete(VerificationToken verificationToken);

}

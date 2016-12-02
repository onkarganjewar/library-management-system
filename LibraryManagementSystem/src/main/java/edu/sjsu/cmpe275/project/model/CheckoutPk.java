/**
 * 
 */
package edu.sjsu.cmpe275.project.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Onkar Ganjewar
 */

//@Embeddable
public class CheckoutPk implements Serializable {

	private static final long serialVersionUID = 1L;

//	@Column(name = "user_id")
	private Integer userId;
	
//	@Column(name = "book_id")
	private Integer bookId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CheckoutPk other = (CheckoutPk) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (bookId == null) {
            if (other.bookId != null)
                return false;
        } else if (!bookId.equals(other.bookId))
            return false;
        return true;
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		return result;
	}
}

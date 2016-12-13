package edu.sjsu.cmpe275.project.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * @author Onkar Ganjewar
 */
public class WaitListPk implements Serializable {

	private static final Long serialVersionUID = 1L;

	private Integer waitListId;

	private Integer userId;

	private Integer bookId;

	public Integer getWaitListId() {
		return waitListId;
	}

	public void setWaitListId(Integer waitListId) {
		this.waitListId = waitListId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBookId() {
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
		final WaitListPk other = (WaitListPk) obj;
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
		if (waitListId == null) {
			if (other.waitListId != null)
				return false;
		} else if (!waitListId.equals(other.waitListId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((waitListId == null) ? 0 : waitListId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		return result;
	}
}

/**
 * 
 */
package edu.sjsu.cmpe275.project.model;

import java.io.Serializable;

/**
 * @author Onkar Ganjewar
 * 
 */
public class BooksHoldPk implements Serializable {

	private static final Long serialVersionUID = 1L;

	private Integer holdListId;

	private Integer userId;

	private Integer bookId;
	
	private Integer bookCopyId;

	public Integer getHoldListId() {
		return holdListId;
	}

	public void setHoldListId(Integer holdListId) {
		this.holdListId = holdListId;
	}

	public Integer getBookCopyId() {
		return bookCopyId;
	}

	public void setBookCopyId(Integer bookCopyId) {
		this.bookCopyId = bookCopyId;
	}

	public Integer getWaitListId() {
		return holdListId;
	}

	public void setWaitListId(Integer waitListId) {
		this.holdListId = waitListId;
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
		final BooksHoldPk other = (BooksHoldPk) obj;
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
		if (bookCopyId == null) {
			if (other.bookCopyId != null)
				return false;
		} else if (!bookCopyId.equals(other.bookCopyId))
			return false;
		if (holdListId == null) {
			if (other.holdListId != null)
				return false;
		} else if (!holdListId.equals(other.holdListId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((holdListId == null) ? 0 : holdListId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		result = prime * result + ((bookCopyId == null) ? 0 : bookCopyId.hashCode());
		return result;
	}

}

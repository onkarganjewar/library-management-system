package edu.sjsu.cmpe275.project.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * @author Onkar Ganjewar
 */
public class BookCartPk implements Serializable {
	private static final long serialVersionUID = 1L;

//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;

	private Integer userId;
	
	private Integer bookId;

//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

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
        final BookCartPk other = (BookCartPk) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
//        if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
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
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}

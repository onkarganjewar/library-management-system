/**
 * 
 */
package edu.sjsu.cmpe275.project.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Onkar Ganjewar
 * 
 */
@Entity
@Table(name = "checkout_Books")
@IdClass(CheckoutPk.class)
public class Checkout implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column
	private Date checkoutDate;

	// @EmbeddedId
	// private CheckoutPk id;

	@ManyToOne
	@Id
	@JoinColumn(name = "book_id")
	private Book bookId;

	@ManyToOne
	@JoinColumn(name = "book_copy_id")
	private BookCopy copy;

	@ManyToOne
	@Id
	@JoinColumn(name = "user_id")
	private User userId;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Checkout other = (Checkout) obj;
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
		// if (id == null) {
		// if (other.id != null)
		// return false;
		// } else if (!id.equals(other.id))
		// return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		// result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	// public CheckoutPk getId() {
	// return id;
	// }
	//
	// public void setId(CheckoutPk id) {
	// this.id = id;
	// }

	public Book getBookId() {
		return bookId;
	}

	public void setBookId(Book bookId) {
		this.bookId = bookId;
	}

	public BookCopy getCopy() {
		return copy;
	}

	public void setCopy(BookCopy copy) {
		this.copy = copy;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}
}

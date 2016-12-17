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
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

	@Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;

    @Id
    @Column(name = "book_id", insertable = false, updatable = false)
    private Integer bookId;
    
    @Column(name = "times")
    private Integer times;

	@ManyToOne
	@MapsId("bookId")
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne
	@JoinColumn(name = "book_copy_id", unique = true)
	private BookCopy copy;


	@MapsId("userId")
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Checkout other = (Checkout) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.userId))
			return false;
		if (book == null) {
			if (other.book!= null)
				return false;
		} else if (!book.equals(other.bookId))
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
		result = prime * result + ((book == null) ? 0 : book.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		// result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book bookId) {
		this.book = bookId;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	} 
	
	public BookCopy getCopy() {
		return copy;
	}

	public void setCopy(BookCopy copy) {
		this.copy = copy;
	}

	public User getUser() {
		return user;
	}

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
	

	public void setUser(User userId) {
		this.user = userId;
	}
	
	@Override
	public String toString() {
		return "Checkout [Book Id=" + bookId + ", User Id=" + userId + ", Date= "+ checkoutDate+ "," + copy + "]";
	}
}

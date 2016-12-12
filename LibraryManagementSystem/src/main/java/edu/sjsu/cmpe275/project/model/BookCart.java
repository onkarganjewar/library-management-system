package edu.sjsu.cmpe275.project.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * @author Onkar Ganjewar
 */

@Entity
@Table(name = "book_cart")
@IdClass(BookCartPk.class)
public class BookCart implements Serializable {

	private static final long serialVersionUID = 1L;

//	@Id
//	@Column(name = "cart_id", insertable = false, updatable = false)
//	private Integer id;

	@Id
	@Column(name = "user_id", insertable = false, updatable = false)
	private Integer userId;

	@Id
	@Column(name = "book_id", insertable = false, updatable = false)
	private Integer bookId;

	@ManyToOne
	@MapsId("bookId")
	@JoinColumn(name = "book_id")
	private Book book;
	
	@MapsId("userId")
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

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

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BookCart other = (BookCart) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.userId))
			return false;
		if (book == null) {
			if (other.book != null)
				return false;
		} else if (!book.equals(other.bookId))
			return false;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((book == null) ? 0 : book.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
//		return "BookCart [Id = " + id + "Book Id=" + bookId + ", User Id=" + userId + "]";

		return "BookCart [Book Id=" + bookId + ", User Id=" + userId + "]";
	}

}

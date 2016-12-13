package edu.sjsu.cmpe275.project.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Onkar Ganjewar
 */
@Entity
@Table(name = "wait_list",
uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id", "user_id"})})
@IdClass(WaitListPk.class)
public class WaitList implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "waitList_id", insertable = false, updatable = false)
	@GenericGenerator(name="waitListIdGenerator" , strategy="increment")
	@GeneratedValue(generator="waitListIdGenerator")
	private Integer waitListId;
	
	@Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;

    @Id
    @Column(name = "book_id", insertable = false, updatable = false)
    private Integer bookId;

    @Column(name = "wait_date")
    private Date dateAdded;
    
	@Id
	@ManyToOne
	@MapsId("bookId")
	@JoinColumn(name = "book_id", referencedColumnName = "id")
	private Book book;

	@MapsId("userId")
	@ManyToOne
	@Id
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public WaitList() {
	}
	
	/**
	 * @param waitListId
	 * @param userId
	 * @param bookId
	 * @param book
	 * @param user
	 */
	public WaitList(Integer waitListId, Integer userId, Integer bookId, Book book, User user) {
		super();
		this.waitListId = waitListId;
		this.userId = userId;
		this.bookId = bookId;
		this.book = book;
		this.user = user;
	}

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


	public Date getDateAdded() {
		return dateAdded;
	}


	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
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
	public String toString() {
		return "WaitList [id=" + waitListId + ", Book Id=" + bookId + ", User Id=" + userId + ", Date added =" + dateAdded
				+ ", " + book + ", " + user + "]";
	}
}

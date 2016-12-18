package edu.sjsu.cmpe275.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Onkar Ganjewar
 */

/**
 * @author Onkar Ganjewar
 * 
 */
@Entity
@Table(name = "hold_list",
uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id", "user_id"})})
@IdClass(BooksHoldPk.class)
public class BooksHoldList {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "holdList_id", insertable = false, updatable = false)
	@GenericGenerator(name="holdListIdGenerator" , strategy="increment")
	@GeneratedValue(generator="holdListIdGenerator")
	private Integer holdListId;
	
	@Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;

    @Id
    @Column(name = "book_id", insertable = false, updatable = false)
    private Integer bookId;

    @Id
    @Column(name = "book_copy_id", insertable = false, updatable = false, unique = true)
    private Integer bookCopyId;

    @Column(name = "date_assigned", nullable = false)
    private Date dateAssigned;
   
    @Id
	@ManyToOne
	@MapsId("bookId")
	@JoinColumn(name = "book_id", referencedColumnName = "id")
	private Book book;

    @Id
    @ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

    @Id
    @OneToOne
	@MapsId("bookCopyId")
	@JoinColumn(name = "book_copy_id", referencedColumnName = "id")
	private BookCopy bookCopy;

	public BooksHoldList() {
	}

	/**
	 * @param holdListId
	 * @param userId
	 * @param bookId
	 * @param dateAssigned
	 * @param book
	 * @param user
	 */
	public BooksHoldList(Integer holdListId, Integer userId, Integer bookId, Date dateAssigned, Book book, User user) {
		super();
		this.holdListId = holdListId;
		this.userId = userId;
		this.bookId = bookId;
		this.dateAssigned = dateAssigned;
		this.book = book;
		this.user = user;
	}

	public Integer getHoldListId() {
		return holdListId;
	}

	public void setHoldListId(Integer holdListId) {
		this.holdListId = holdListId;
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

	public Integer getBookCopyId() {
		return bookCopyId;
	}

	public void setBookCopyId(Integer bookCopyId) {
		this.bookCopyId = bookCopyId;
	}

	public Date getDateAssigned() {
		return dateAssigned;
	}

	public void setDateAssigned(Date dateAssigned) {
		this.dateAssigned = dateAssigned;
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


	public BookCopy getBookCopy() {
		return bookCopy;
	}

	public void setBookCopy(BookCopy bookCopy) {
		this.bookCopy = bookCopy;
	}

	@Override
	public String toString() {
		return "BooksHoldList [holdListId=" + holdListId + ", userId=" + userId + ", bookId=" + bookId + ", bookCopyId="
				+ bookCopyId + ", dateAssigned=" + dateAssigned + ", book=" + book + ", user=" + user + ", copy=" + bookCopy
				+ "]";
	}

}

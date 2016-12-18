/**
 * 
 */
package edu.sjsu.cmpe275.project.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * @author Onkar Ganjewar
 * 
 */
@Entity
@Table(name = "BOOK")
public class Book implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String publicationYear;

	@Column
	private String libraryLocation;

	@Column
	@Cascade({ CascadeType.ALL })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "books")
	private List<BookCopy> copies;

	@Column
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	private List<Checkout> checkoutCopies;

	@Column
	@Cascade({ CascadeType.ALL })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	private List<BookCart> bookCartItems;

	@Column
	@Cascade({ CascadeType.ALL })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	private List<WaitList> waitListPersons;

	@Column
	@Cascade({ CascadeType.ALL })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	private List<BooksHoldList> holdListPersons;
	
	@Column
	private String availability;

	@Column
	private String[] keywords;

	@Column
	private String author;

	@Column
	private String title;

	@Column
	private String callNumber;

	@Column
	private String publisher;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getLibraryLocation() {
		return libraryLocation;
	}

	public void setLibraryLocation(String libraryLocation) {
		this.libraryLocation = libraryLocation;
	}

	public List<BookCopy> getCopies() {
		return copies;
	}

	public void setCopies(List<BookCopy> copies) {
		this.copies = copies;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public List<Checkout> getCheckoutCopies() {
		return checkoutCopies;
	}

	public void setCheckoutCopies(List<Checkout> checkoutCopies) {
		this.checkoutCopies = checkoutCopies;
	}

	public List<BookCart> getBookCartItems() {
		return bookCartItems;
	}

	public void setBookCartItems(List<BookCart> bookCartItems) {
		this.bookCartItems = bookCartItems;
	}

	public List<WaitList> getWaitListPersons() {
		return waitListPersons;
	}

	public void setWaitListPersons(List<WaitList> waitListPersons) {
		this.waitListPersons = waitListPersons;
	}

	public List<BooksHoldList> getHoldListPersons() {
		return holdListPersons;
	}

	public void setHoldListPersons(List<BooksHoldList> holdListPersons) {
		this.holdListPersons = holdListPersons;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Book other = (Book) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", Title=" + title + ", Publication=" + publicationYear + ", Publisher=" + publisher
				+ ", Author=" + author + "]";
	}
}

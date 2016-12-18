package edu.sjsu.cmpe275.project.model;

import java.io.Serializable; 
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import edu.sjsu.cmpe275.project.validation.UniqueEmail;
import edu.sjsu.cmpe275.project.validation.ValidUnivId;

/**
 * @author Onkar Ganjewar
 */
@Entity
@Table(name = "APP_USER")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotEmpty
	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@NotEmpty
	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;

	@NotEmpty
	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;

	@NotEmpty
	@Email
	@UniqueEmail
	@Column(name = "EMAIL", nullable = false)
	private String email;
	
	@NotEmpty
	@ValidUnivId
	@Column(name = "UNIVERSITY_ID", nullable = false)
	private String uId;
	
	@Column(name = "FINE", nullable = false)
	private double fine;

//	@NotEmpty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_USER_USER_PROFILE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "USER_PROFILE_ID") })
	private Set<UserProfile> userProfiles = new HashSet<UserProfile>();

	@Column
	@Cascade({ CascadeType.ALL })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Checkout> checkoutCopies;
	
	@Column
	@Cascade({ CascadeType.ALL })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<BookCart> bookCartItems;
	
	@Column
	@Cascade({ CascadeType.ALL })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<WaitList> waitListPersons;
	
	@Column
	@Cascade({ CascadeType.ALL })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<BooksHoldList> holdListPersons;
	
	private boolean enabled;

	public User() {
        super();
        this.enabled = false;
    }
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public double getFine() {
		return fine;
	}

	public void setFine(double fine) {
		this.fine = fine;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public Set<UserProfile> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", University Id=" + uId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", " + userProfiles + "]";
	}
}

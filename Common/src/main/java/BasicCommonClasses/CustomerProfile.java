package BasicCommonClasses;

import java.time.LocalDate;
import java.util.HashSet;

/**
 * CustomerProfile - This class represents a customer profile: user name, email, birthday etc.
 * 
 * @author Idan Atias
 */
public class CustomerProfile {
	String userName;
	String firstName;
	String lastName;
	String phoneNumber;
	String emailAddress;
	String city;
	String street;
	LocalDate birthdate;
	HashSet<Ingredient> allerganics;

	public CustomerProfile(String userName, String firstName, String lastName, String phoneNumber, String emailAddress,
			String city, String street, LocalDate birthdate, HashSet<Ingredient> allerganics) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.city = city;
		this.street = street;
		this.birthdate = birthdate;
		this.allerganics = allerganics;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	public HashSet<Ingredient> getAllerganics() {
		return allerganics;
	}
	public void setAllerganics(HashSet<Ingredient> allerganics) {
		this.allerganics = allerganics;
	}
	@Override
	public String toString() {
		return String.format(
				"CustomerProfile [userName=%s, firstName=%s, lastName=%s, phoneNumber=%s, emailAddress=%s, city=%s, street=%s, birthdate=%s, allerganics=%s]",
				userName, firstName, lastName, phoneNumber, emailAddress, city, street, birthdate, allerganics);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return result = prime * result + ((userName == null) ? 0 : userName.hashCode());
	}
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CustomerProfile other = (CustomerProfile) o;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
}

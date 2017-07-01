package BasicCommonClasses;

import java.time.LocalDate;
import java.util.HashSet;

import CommonDefs.CustomerProfileException.EmptyAllergensSet;
import CommonDefs.CustomerProfileException.InvalidParameter;

/**
 * ICustomerProfile - This interface is the contract for Customer Profile Type.
 *
 * @author Lior Ben Ami
 * @since 2017-04-09
 */
public interface ICustomerProfile {
	
	/**
	 * 
	 * @return customer usename
	 */
	String getUserName();
	
	
	/**
	 * 
	 * @param userName - update the username of the customer
	 */
	void setUserName(String userName);
	
	/**
	 * 
	 * @return - first name of the customer
	 */
	String getFirstName();
	
	/**
	 * 
	 * @param firstName - update the first name of the customer
	 */
	void setFirstName(String firstName);
	
	/**
	 * 
	 * @return- last name of the customer
	 */
	String getLastName();
	
	/**
	 * 
	 * @param lastName - update the last name of the customer
	 */
	void setLastName(String lastName);
	
	/**
	 * 
	 * @return - phone number of the customer
	 */
	String getPhoneNumber();
	
	/**
	 * 
	 * @return - update the phone number of the customer
	 */
	void setPhoneNumber(String phoneNumber);
	
	/**
	 * 
	 * @return - email of the customer
	 */
	String getEmailAddress();
	
	/**
	 * 
	 * @param emailAddress - update the email of the customer
	 */
	void setEmailAddress(String emailAddress);
	
	/**
	 * 
	 * @return the name of the city
	 */
	String getCity();
	
	/**
	 * 
	 * @param city- update the city name of the customer
	 */
	void setCity(String city);
	
	/**
	 * 
	 * @param the street of the customer
	 */
	String getStreet();
	
	/**
	 * 
	 * @param street - update the street name of the customer
	 */
	void setStreet(String street);
	
	/**
	 * 
	 * @return - the birthdate of the customer
	 */
	LocalDate getBirthdate();
	
	/**
	 * 
	 * @param birthdate - update the birthdate of the customer
	 */
	void setBirthdate(LocalDate birthdate);
	
	/**
	 * 
	 * @return - get all the allergans of the customer
	 */
	HashSet<Ingredient> getAllergens();
	
	/**
	 * 
	 * @param allergens - update the allergans of the customer
	 */
	void setAllergens(HashSet<Ingredient> allergens);
	
	/**
	 * 
	 * @param allergens - to allregans to the custoemr
	 * @throws InvalidParameter
	 */
	void addAllergens(HashSet<Ingredient> allergens) throws InvalidParameter;
	
	/**
	 * 
	 * @param allergens - the alleragns to remove
	 * @throws InvalidParameter
	 * @throws EmptyAllergensSet
	 */
	void removeAllergens(HashSet<Ingredient> allergens) throws InvalidParameter, EmptyAllergensSet;
	
	/**
	 * removes all allergans of the customer
	 */
	void clearAllAllergens();
}

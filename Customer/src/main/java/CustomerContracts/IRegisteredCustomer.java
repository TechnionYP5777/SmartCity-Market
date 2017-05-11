package CustomerContracts;

import java.time.LocalDate;
import java.util.HashSet;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Ingredient;
import CustomerContracts.ACustomerExceptions.*;

/**
 * IRegisteredCustomer - This interface is the contract for RegisteredCustomer
 * type.
 * 
 * @author idan atias
 * @author Aviad Cohen
 * @since 2017-01-04
 *
 */
public interface IRegisteredCustomer extends ICustomer {

	// #################### Getters ###########################
	/**
	 * getUserName - returns the customer's user name
	 * 
	 * @return String
	 * @throws CustomerNotConnected 
	 */
	String getUserName() throws CustomerNotConnected;

	/**
	 * getFirstName - returns the customer's first name
	 * 
	 * @return String
	 * @throws CustomerNotConnected 
	 */
	String getFirstName() throws CustomerNotConnected;

	/**
	 * getLastName - returns the customer's last name
	 * 
	 * @return String
	 * @throws CustomerNotConnected 
	 */
	String getLastName() throws CustomerNotConnected;

	/**
	 * getPhoneNumber - returns the customer's phone number
	 * 
	 * @return String
	 * @throws CustomerNotConnected 
	 */
	String getPhoneNumber() throws CustomerNotConnected;

	/**
	 * getEmailAddress - returns the customer's email
	 * 
	 * @return String
	 * @throws CustomerNotConnected 
	 */
	String getEmailAddress() throws CustomerNotConnected;

	/**
	 * getShippingAddress - returns the customer's shipping address
	 * 
	 * @return String
	 * @throws CustomerNotConnected 
	 */
	String getShippingAddress() throws CustomerNotConnected;

	/**
	 * getBirthdate - returns the customer's shipping address
	 * 
	 * @return LocalDate
	 * @throws CustomerNotConnected 
	 */
	LocalDate getBirthdate() throws CustomerNotConnected;

	/**
	 * getAllergens - returns the customer's set of allergens
	 * 
	 * @return HashSet<Ingredient>
	 * @throws CustomerNotConnected 
	 */
	HashSet<Ingredient> getAllergens() throws CustomerNotConnected;

	// #################### Setters ###########################

	/**
	 * changeFirstName - changes the customer's first name
	 * 
	 * @param firstname
	 *            - user's new first name
	 * @throws CustomerNotConnected 
	 * @throws InvalidParameter 
	 * @throws CriticalError 
	 */
	void changeFirstName(String firstname) throws CustomerNotConnected, InvalidParameter, CriticalError;

	/**
	 * changeLastName - changes the customer's last name
	 * 
	 * @param lastname
	 *            - user's new last name
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 * @throws CriticalError 
	 */
	void changeLastName(String lastname) throws CustomerNotConnected, InvalidParameter, CriticalError;

	/**
	 * changePhoneNumber - changes the customer's phone number
	 * 
	 * @param phoneNumber
	 *            - user's new phone number
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 * @throws CriticalError 
	 */
	void changePhoneNumber(String phoneNumber) throws CustomerNotConnected, InvalidParameter, CriticalError;

	/**
	 * changeEmailAddress - changes the customer's email address
	 * 
	 * @param emailAddress
	 *            - user's new email address
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 * @throws CriticalError 
	 */
	void changeEmailAddress(String emailAddress) throws CustomerNotConnected, InvalidParameter, CriticalError;

	/**
	 * changeShippingAddress - changes the customer's shipping Address
	 * 
	 * @param street
	 *            - user's new street
	 * @param city
	 * 			  - user's new city
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 * @throws CriticalError 
	 */
	void changeShippingAddress(String street, String city) throws CustomerNotConnected, InvalidParameter, CriticalError;

	/**
	 * changeBirthdate - changes the customer's birthdate
	 * 
	 * @param birthdate
	 *            - user's new birthdate
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 * @throws CriticalError 
	 */
	void changeBirthdate(LocalDate birthdate) throws CustomerNotConnected, InvalidParameter, CriticalError;

	// #########################################################

	/**
	 * clearAllergens - removes all customer's allergens
	 * @throws CustomerNotConnected 
	 */
	void clearAllergens() throws CustomerNotConnected;

	/**
	 * addAllergen - add allergen to customer's allergens set
	 * 
	 * @param allergen
	 *            - an ingredient the customer is allergic to.
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws InvalidParameter 
	 */
	void addAllergen(Ingredient allergen) throws CustomerNotConnected, CriticalError, InvalidParameter;

	/**
	 * addAllergens - add a collection of allergens to customer's allergens set
	 * 
	 * @param allergens
	 *            - a collection of ingredients the customer is allergic to.
	 * @throws CustomerNotConnected 
	 * @throws InvalidParameter 
	 */
	void addAllergens(HashSet<Ingredient> allergens) throws CustomerNotConnected, InvalidParameter;

	/**
	 * removeAllergen - remove allergen from customer's allergens set
	 * 
	 * @param allergen
	 *            - an ingredient the customer is not allergic to.
	 * @throws CustomerNotConnected 
	 * @throws CriticalError 
	 * @throws InvalidParameter 
	 */
	void removeAllergen(Ingredient allergen) throws CustomerNotConnected, CriticalError, InvalidParameter;

	/**
	 * removeAllergens - remove a collection of allergens from customer's
	 * allergens set
	 * 
	 * @param allergens
	 *            - a collection of ingredients the customer is not allergic to.
	 * @throws CustomerNotConnected 
	 * @throws InvalidParameter 
	 */
	void removeAllergens(HashSet<Ingredient> allergens) throws CustomerNotConnected, InvalidParameter;
	
	
	/**
	 * getCustomerProfile - returns the customer profile of a registered customer
	 * @throws CriticalError 
	 * 
	 */
	CustomerProfile getCustomerProfile() throws CriticalError;

	/**
	 * removeCustomer - remove the customer from system - can't be undone!
	 * 
	 * @throws CustomerNotConnected 
	 * @throws InvalidParameter 
	 * @throws AuthenticationError
	 * @throws CriticalError
	 */
	void removeCustomer() throws CustomerNotConnected, InvalidParameter, AuthenticationError, CriticalError;
	
	/**
	 * updateCustomerProfile - updates the customer profile!
	 * 
	 * @param CustomerProfile - the new CustomerProfile
	 * @throws CustomerNotConnected 
	 * @throws InvalidParameter 
	 * @throws AuthenticationError
	 * @throws CriticalError
	 */
	void updateCustomerProfile(CustomerProfile p) throws CustomerNotConnected, InvalidParameter, AuthenticationError, CriticalError;
	
}

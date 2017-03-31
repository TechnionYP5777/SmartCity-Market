package CustomerContracts;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import BasicCommonClasses.Ingredient;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;

/**
 * IRegisteredCustomer - This interface is the contract for RegisteredCustomer
 * type.
 * 
 * @author idan atias
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
	 */
	void changeFirstName(String firstname) throws CustomerNotConnected, InvalidParameter;

	/**
	 * changeLastName - changes the customer's last name
	 * 
	 * @param lastname
	 *            - user's new last name
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 */
	void changeLastName(String lastname) throws CustomerNotConnected, InvalidParameter;

	/**
	 * changePhoneNumber - changes the customer's phone number
	 * 
	 * @param phoneNumber
	 *            - user's new phone number
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 */
	void changePhoneNumber(String phoneNumber) throws CustomerNotConnected, InvalidParameter;

	/**
	 * changeEmailAddress - changes the customer's email address
	 * 
	 * @param emailAddress
	 *            - user's new email address
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 */
	void changeEmailAddress(String emailAddress) throws CustomerNotConnected, InvalidParameter;

	/**
	 * changeShippingAddress - changes the customer's shipping Address
	 * 
	 * @param street
	 *            - user's new street
	 * @param city
	 * 			  - user's new city
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 */
	void changeShippingAddress(String street, String city) throws CustomerNotConnected, InvalidParameter;

	/**
	 * changeBirthdate - changes the customer's birthdate
	 * 
	 * @param birthdate
	 *            - user's new birthdate
	 * @throws InvalidParameter 
	 * @throws CustomerNotConnected 
	 */
	void changeBirthdate(LocalDate birthdate) throws CustomerNotConnected, InvalidParameter;

	// #########################################################

	/**
	 * clearAllergens - removes all customer's allergens
	 */
	void clearAllergens();

	/**
	 * addAllergen - add allergen to customer's allergens set
	 * 
	 * @param allergen
	 *            - an ingredient the customer is allergic to.
	 */
	void addAllergens(Ingredient allergen);

	/**
	 * addAllergens - add a collection of allergens to customer's allergens set
	 * 
	 * @param allergens
	 *            - a collection of ingredients the customer is allergic to.
	 */
	void addAllergens(Collection<Ingredient> allergens);

	/**
	 * removeAllergen - remove allergen from customer's allergens set
	 * 
	 * @param allergen
	 *            - an ingredient the customer is not allergic to.
	 */
	void removeAllergen(Ingredient allergen);

	/**
	 * removeAllergens - remove a collection of allergens from customer's
	 * allergens set
	 * 
	 * @param allergens
	 *            - a collection of ingredients the customer is not allergic to.
	 */
	void removeAllergens(Collection<Ingredient> allergens);

}

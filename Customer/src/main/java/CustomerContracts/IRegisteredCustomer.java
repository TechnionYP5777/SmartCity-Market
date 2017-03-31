package CustomerContracts;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import BasicCommonClasses.Ingredient;

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
	 */
	String getUserName();

	/**
	 * getFirstName - returns the customer's first name
	 * 
	 * @return String
	 */
	String getFirstName();

	/**
	 * getLastName - returns the customer's last name
	 * 
	 * @return String
	 */
	String getLastName();

	/**
	 * getPhoneNumber - returns the customer's phone number
	 * 
	 * @return String
	 */
	String getPhoneNumber();

	/**
	 * getEmailAddress - returns the customer's email
	 * 
	 * @return String
	 */
	String getEmailAddress();

	/**
	 * getShippingAddress - returns the customer's shipping address
	 * 
	 * @return String
	 */
	String getShippingAddress();

	/**
	 * getBirthdate - returns the customer's shipping address
	 * 
	 * @return LocalDate
	 */
	LocalDate getBirthdate();

	/**
	 * getAllergens - returns the customer's set of allergens
	 * 
	 * @return HashSet<Ingredient>
	 */
	HashSet<Ingredient> getAllergens();

	// #################### Setters ###########################

	/**
	 * changeFirstName - changes the customer's first name
	 * 
	 * @param firstname
	 *            - user's new first name
	 */
	void changeFirstName(String firstname);

	/**
	 * changeLastName - changes the customer's last name
	 * 
	 * @param lastname
	 *            - user's new last name
	 */
	void changeLastName(String lastname);

	/**
	 * changePhoneNumber - changes the customer's phone number
	 * 
	 * @param phoneNumber
	 *            - user's new phone number
	 */
	void changePhoneNumber(String phoneNumber);

	/**
	 * changeEmailAddress - changes the customer's email address
	 * 
	 * @param emailAddress
	 *            - user's new email address
	 */
	void changeEmailAddress(String emailAddress);

	/**
	 * changeShippingAddress - changes the customer's shipping Address
	 * 
	 * @param shippingAddress
	 *            - user's new shipping Address
	 */
	void changeShippingAddress(String shippingAddress);

	/**
	 * changeBirthdate - changes the customer's birthdate
	 * 
	 * @param birthdate
	 *            - user's new birthdate
	 */
	void changeBirthdate(LocalDate birthdate);

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

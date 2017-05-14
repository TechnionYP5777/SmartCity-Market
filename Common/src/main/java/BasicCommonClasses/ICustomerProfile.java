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
	
	String getUserName();
	
	void setUserName(String userName);
	
	String getFirstName();
	
	void setFirstName(String firstName);
	
	String getLastName();
	
	void setLastName(String lastName);
	
	String getPhoneNumber();
	
	void setPhoneNumber(String phoneNumber);
	
	String getEmailAddress();
	
	void setEmailAddress(String emailAddress);
	
	String getCity();
	
	void setCity(String city);
	
	String getStreet();
	
	void setStreet(String street);
	
	LocalDate getBirthdate();
	
	void setBirthdate(LocalDate birthdate);
	
	HashSet<Ingredient> getAllergens();
	
	void setAllergens(HashSet<Ingredient> allergens);
	
	void addAllergens(HashSet<Ingredient> allergens) throws InvalidParameter;
	
	void removeAllergens(HashSet<Ingredient> allergens) throws InvalidParameter, EmptyAllergensSet;
	
	void clearAllAllergens();

}

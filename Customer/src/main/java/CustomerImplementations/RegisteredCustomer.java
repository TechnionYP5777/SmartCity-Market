package CustomerImplementations;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import com.google.inject.Inject;

import BasicCommonClasses.Ingredient;
import CustomerContracts.ACustomerExceptions;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.IRegisteredCustomer;
import UtilsContracts.IClientRequestHandler;

/**
 * This class represents a registered customer and holds all it's functionalitis
 * 
 * @author idan atias
 *
 */
public class RegisteredCustomer extends Customer implements IRegisteredCustomer {

	@Inject
	public RegisteredCustomer(IClientRequestHandler clientRequestHandler) {
		super(clientRequestHandler);
	}

	private void validateCustomerIsLoggedIn() throws CustomerNotConnected {
		if (customerProfile != null)
			return;
		log.error("Customer is not logged in. Cart id=(" + this.id + ")");
		throw new ACustomerExceptions.CustomerNotConnected();
	}

	private void validateString(String s) throws InvalidParameter {
		if (!s.isEmpty())
			return;
		log.error("Got empty string as parameter");
		throw new InvalidParameter();
	}

	@Override
	public String getUserName() throws CustomerNotConnected {
		validateCustomerIsLoggedIn();
		return customerProfile.getUserName();
	}

	@Override
	public String getFirstName() throws CustomerNotConnected {
		validateCustomerIsLoggedIn();
		return customerProfile.getFirstName();
	}

	@Override
	public String getLastName() throws CustomerNotConnected {
		validateCustomerIsLoggedIn();
		return customerProfile.getLastName();
	}

	@Override
	public String getPhoneNumber() throws CustomerNotConnected {
		validateCustomerIsLoggedIn();
		return customerProfile.getPhoneNumber();
	}

	@Override
	public String getEmailAddress() throws CustomerNotConnected {
		validateCustomerIsLoggedIn();
		return customerProfile.getEmailAddress();
	}

	@Override
	public String getShippingAddress() throws CustomerNotConnected {
		validateCustomerIsLoggedIn();
		return customerProfile.getStreet() + ", " + customerProfile.getCity();
	}

	@Override
	public LocalDate getBirthdate() throws CustomerNotConnected {
		validateCustomerIsLoggedIn();
		return customerProfile.getBirthdate();
	}

	@Override
	public HashSet<Ingredient> getAllergens() throws CustomerNotConnected {
		validateCustomerIsLoggedIn();
		return customerProfile.getAllergens();
	}

	@Override
	public void changeFirstName(String firstname) throws CustomerNotConnected, InvalidParameter {
		validateCustomerIsLoggedIn();
		validateString(firstname);
		customerProfile.setFirstName(firstname);
	}

	@Override
	public void changeLastName(String lastname) throws CustomerNotConnected, InvalidParameter {
		validateCustomerIsLoggedIn();
		validateString(lastname);
		customerProfile.setLastName(lastname);
	}

	@Override
	public void changePhoneNumber(String phoneNumber) throws CustomerNotConnected, InvalidParameter {
		// TODO add validation of legal phone number
		validateCustomerIsLoggedIn();
		validateString(phoneNumber);
		customerProfile.setPhoneNumber(phoneNumber);
	}

	@Override
	public void changeEmailAddress(String emailAddress) throws CustomerNotConnected, InvalidParameter {
		// TODO add validation of legal email address
		validateCustomerIsLoggedIn();
		validateString(emailAddress);
		customerProfile.setEmailAddress(emailAddress);
	}

	@Override
	public void changeShippingAddress(String street, String city) throws CustomerNotConnected, InvalidParameter {
		validateCustomerIsLoggedIn();
		validateString(street);
		validateString(city);
		customerProfile.setStreet(street);
		customerProfile.setCity(city);
	}

	@Override
	public void changeBirthdate(LocalDate birthdate) throws CustomerNotConnected, InvalidParameter {
		validateCustomerIsLoggedIn();
		customerProfile.setBirthdate(birthdate);
	}

	public void clearAllergens() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * CustomerContracts.IRegisteredCustomer#addAllergens(BasicCommonClasses.
	 * Ingredient)
	 */
	@Override
	public void addAllergens(Ingredient allergen) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * CustomerContracts.IRegisteredCustomer#addAllergens(java.util.Collection)
	 */
	@Override
	public void addAllergens(Collection<Ingredient> allergens) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * CustomerContracts.IRegisteredCustomer#removeAllergen(BasicCommonClasses.
	 * Ingredient)
	 */
	@Override
	public void removeAllergen(Ingredient allergen) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see CustomerContracts.IRegisteredCustomer#removeAllergens(java.util.
	 * Collection)
	 */
	@Override
	public void removeAllergens(Collection<Ingredient> allergens) {
		// TODO Auto-generated method stub

	}

}

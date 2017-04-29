package CustomerImplementations;

import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.HashSet;

import com.google.inject.Inject;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Ingredient;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import CustomerContracts.ACustomerExceptions;
import CustomerContracts.ACustomerExceptions.*;
import CustomerContracts.IRegisteredCustomer;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * This class represents a registered customer and holds all it's functionalities
 * 
 * @author idan atias
 * @author Aviad Cohen
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

	private void validateNotNull(Object o) throws CriticalError {
		if (o != null)
			return;
		
		log.fatal("Got NULL as parameter. Something bad happened...");
		
		throw new CriticalError();
	}

	private void validateString(String s) throws InvalidParameter, CriticalError {
		validateNotNull(s);
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
	public void changeFirstName(String firstname) throws CustomerNotConnected, InvalidParameter, CriticalError {
		validateCustomerIsLoggedIn();
		validateString(firstname);
		log.info("Customer " + customerProfile.getUserName() + " changed it's first name from "
				+ customerProfile.getFirstName() + " to " + firstname);
		customerProfile.setFirstName(firstname);
	}

	@Override
	public void changeLastName(String lastname) throws CustomerNotConnected, InvalidParameter, CriticalError {
		validateCustomerIsLoggedIn();
		validateString(lastname);
		log.info("Customer " + customerProfile.getUserName() + " changed it's last name from "
				+ customerProfile.getLastName() + " to " + lastname);
		customerProfile.setLastName(lastname);
	}

	@Override
	public void changePhoneNumber(String phoneNumber) throws CustomerNotConnected, InvalidParameter, CriticalError {
		// TODO add validation of legal phone number
		validateCustomerIsLoggedIn();
		validateString(phoneNumber);
		log.info("Customer " + customerProfile.getUserName() + " changed it's phone number from "
				+ customerProfile.getPhoneNumber() + " to " + phoneNumber);
		customerProfile.setPhoneNumber(phoneNumber);
	}

	@Override
	public void changeEmailAddress(String emailAddress) throws CustomerNotConnected, InvalidParameter, CriticalError {
		// TODO add validation of legal email address
		validateCustomerIsLoggedIn();
		validateString(emailAddress);
		log.info("Customer " + customerProfile.getUserName() + " changed it's email address from "
				+ customerProfile.getEmailAddress() + " to " + emailAddress);
		customerProfile.setEmailAddress(emailAddress);
	}

	@Override
	public void changeShippingAddress(String street, String city)
			throws CustomerNotConnected, InvalidParameter, CriticalError {
		validateCustomerIsLoggedIn();
		validateString(street);
		validateString(city);
		log.info("Customer " + customerProfile.getUserName() + " changed it's shippind address from "
				+ customerProfile.getStreet() + ", " + customerProfile.getCity() + " to " + street + ", " + city);
		customerProfile.setStreet(street);
		customerProfile.setCity(city);
	}

	@Override
	public void changeBirthdate(LocalDate birthdate) throws CustomerNotConnected, InvalidParameter, CriticalError {
		validateCustomerIsLoggedIn();
		log.info("Customer " + customerProfile.getUserName() + " changed it's birth date from "
				+ customerProfile.getBirthdate() + " to " + birthdate);
		customerProfile.setBirthdate(birthdate);
	}

	@Override
	public void clearAllergens() throws CustomerNotConnected {
		validateCustomerIsLoggedIn();
		customerProfile.clearAllAllergens();
	}

	@Override
	public void addAllergen(Ingredient allergen) throws CustomerNotConnected, CriticalError, InvalidParameter{
		validateCustomerIsLoggedIn();
		validateNotNull(allergen);
		HashSet<Ingredient> allergensSet = new HashSet<Ingredient>();
		allergensSet.add(allergen);
		addAllergens(allergensSet);
	}

	@Override
	public void addAllergens(HashSet<Ingredient> allergens) throws CustomerNotConnected, InvalidParameter {
		validateCustomerIsLoggedIn();
		try{
			customerProfile.addAllergens(allergens);
		} catch (CommonDefs.CustomerProfileException.InvalidParameter ex){
			log.error("Invalid parameter passed to customer profle allergens set");
			throw new InvalidParameter();
		}
	}

	@Override
	public void removeAllergen(Ingredient allergen) throws CustomerNotConnected, CriticalError, InvalidParameter {
		validateCustomerIsLoggedIn();
		validateNotNull(allergen);
		HashSet<Ingredient> allergensSet = new HashSet<Ingredient>();
		allergensSet.add(allergen);
		removeAllergens(allergensSet);
	}

	@Override
	public void removeAllergens(HashSet<Ingredient> allergens) throws CustomerNotConnected, InvalidParameter {
		validateCustomerIsLoggedIn();
		try{
			customerProfile.removeAllergens(allergens);
		} catch (CommonDefs.CustomerProfileException.InvalidParameter ex){
			log.error("Invalid parameter passed to customer profle allergens set");
			throw new InvalidParameter();
		} catch (CommonDefs.CustomerProfileException.EmptyAllergensSet ex){
			log.error("Tried to remove ingredients from empty allergens list");
		}
	}
	
	@Override
	public CustomerProfile getCustomerProfile() throws CriticalError{
		validateNotNull(customerProfile);
		return customerProfile;
	}

	@Override
	public void removeCustomer() throws CustomerNotConnected, InvalidParameter, AuthenticationError, CriticalError {
		String serverResponse;
		
		log.info("Creating removeCustomer command wrapper to customer with username: " + customerProfile.getUserName());

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);
		
		/* first: getting the product from the server */
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.REMOVE_CUSTOMER,
						Serialization.serialize(customerProfile.getUserName())).serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		
		terminateCommunication();
		
		CommandWrapper $ = CommandWrapper.deserialize(serverResponse);
				
		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | CriticalError |
				 AmountBiggerThanAvailable | ProductPackageDoesNotExist | 
				 GroceryListIsEmpty | ProductCatalogDoesNotExist | UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
						
			throw new CriticalError();
		}
		
		log.info("removeCustomer command succeed.");
	}
	
	@Override
	public void updateCustomerProfile(CustomerProfile p) throws CustomerNotConnected, InvalidParameter, AuthenticationError, CriticalError {
		String serverResponse;
		
		log.info("Creating updateCustomerProfile command wrapper to customer with username: " + p.getUserName());

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);
		
		/* first: getting the product from the server */
		try {
			serverResponse = sendRequestWithRespondToServer(
					(new CommandWrapper(id, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						Serialization.serialize(p)).serialize()));
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");
			
			throw new CriticalError();
		}
		
		terminateCommunication();
		
		CommandWrapper $ = CommandWrapper.deserialize(serverResponse);
				
		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | CriticalError |
				 AmountBiggerThanAvailable | ProductPackageDoesNotExist | 
				 GroceryListIsEmpty | ProductCatalogDoesNotExist | UsernameAlreadyExists ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
						
			throw new CriticalError();
		}
		
		customerProfile = p;
		
		log.info("updateCustomerProfile command succeed.");
	}
}

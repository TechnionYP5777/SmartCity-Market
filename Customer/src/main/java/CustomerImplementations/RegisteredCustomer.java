package CustomerImplementations;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import com.google.inject.Inject;

import BasicCommonClasses.Ingredient;
import CustomerContracts.ACustomerExceptions;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
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

	@Override
	public String getUserName() throws CustomerNotConnected {
		if (customerProfile == null) {
			log.error("Customer is not logged in. Cart id=(" + this.id + ")");
			throw new ACustomerExceptions.CustomerNotConnected();
		}
		return customerProfile.getUserName();
	}

	@Override
	public String getFirstName() throws CustomerNotConnected {
		if (customerProfile == null) {
			log.error("Customer is not logged in. Cart id=(" + this.id + ")");
			throw new ACustomerExceptions.CustomerNotConnected();
		}
		return customerProfile.getFirstName();
	}

	@Override
	public String getLastName() throws CustomerNotConnected {
		if (customerProfile == null) {
			log.error("Customer is not logged in. Cart id=(" + this.id + ")");
			throw new ACustomerExceptions.CustomerNotConnected();
		}
		return customerProfile.getLastName();
	}

	@Override
	public String getPhoneNumber() throws CustomerNotConnected {
		if (customerProfile == null) {
			log.error("Customer is not logged in. Cart id=(" + this.id + ")");
			throw new ACustomerExceptions.CustomerNotConnected();
		}
		return customerProfile.getPhoneNumber();
	}

	@Override
	public String getEmailAddress() throws CustomerNotConnected {
		if (customerProfile == null) {
			log.error("Customer is not logged in. Cart id=(" + this.id + ")");
			throw new ACustomerExceptions.CustomerNotConnected();
		}
		return customerProfile.getEmailAddress();
	}

	@Override
	public String getShippingAddress() throws CustomerNotConnected {
		if (customerProfile == null) {
			log.error("Customer is not logged in. Cart id=(" + this.id + ")");
			throw new ACustomerExceptions.CustomerNotConnected();
		}
		return customerProfile.getStreet() + ", " + customerProfile.getCity();
	}

	@Override
	public LocalDate getBirthdate() throws CustomerNotConnected {
		if (customerProfile == null) {
			log.error("Customer is not logged in. Cart id=(" + this.id + ")");
			throw new ACustomerExceptions.CustomerNotConnected();
		}
		return customerProfile.getBirthdate();
	}

	@Override
	public HashSet<Ingredient> getAllergens() throws CustomerNotConnected {
		if (customerProfile == null) {
			log.error("Customer is not logged in. Cart id=(" + this.id + ")");
			throw new ACustomerExceptions.CustomerNotConnected();
		}
		return customerProfile.getAllergens();
	}

	@Override
	public void changeFirstName(String firstname) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * CustomerContracts.IRegisteredCustomer#changeLastName(java.lang.String)
	 */
	@Override
	public void changeLastName(String lastname) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * CustomerContracts.IRegisteredCustomer#changePhoneNumber(java.lang.String)
	 */
	@Override
	public void changePhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see CustomerContracts.IRegisteredCustomer#changeEmailAddress(java.lang.
	 * String)
	 */
	@Override
	public void changeEmailAddress(String emailAddress) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * CustomerContracts.IRegisteredCustomer#changeShippingAddress(java.lang.
	 * String)
	 */
	@Override
	public void changeShippingAddress(String shippingAddress) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see CustomerContracts.IRegisteredCustomer#changeBirthdate(java.time.
	 * LocalDate)
	 */
	@Override
	public void changeBirthdate(LocalDate birthdate) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see CustomerContracts.IRegisteredCustomer#clearAllergens()
	 */
	@Override
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

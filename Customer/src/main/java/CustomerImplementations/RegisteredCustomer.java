package CustomerImplementations;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import com.google.inject.Inject;

import BasicCommonClasses.Ingredient;
import CustomerContracts.IRegisteredCustomer;
import UtilsContracts.IClientRequestHandler;

/**
 * @author idan atias
 *
 */
public class RegisteredCustomer extends Customer implements IRegisteredCustomer {
	
	@Inject
	public RegisteredCustomer(IClientRequestHandler clientRequestHandler) {
		super(clientRequestHandler);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#getUserName()
	 */
	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#getFirstName()
	 */
	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#getLastName()
	 */
	@Override
	public String getLastName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#getPhoneNumber()
	 */
	@Override
	public String getPhoneNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#getEmailAddress()
	 */
	@Override
	public String getEmailAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#getShippingAddress()
	 */
	@Override
	public String getShippingAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#getBirthdate()
	 */
	@Override
	public LocalDate getBirthdate() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#getAllergens()
	 */
	@Override
	public HashSet<Ingredient> getAllergens() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#changeFirstName(java.lang.String)
	 */
	@Override
	public void changeFirstName(String firstname) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#changeLastName(java.lang.String)
	 */
	@Override
	public void changeLastName(String lastname) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#changePhoneNumber(java.lang.String)
	 */
	@Override
	public void changePhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#changeEmailAddress(java.lang.String)
	 */
	@Override
	public void changeEmailAddress(String emailAddress) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#changeShippingAddress(java.lang.String)
	 */
	@Override
	public void changeShippingAddress(String shippingAddress) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#changeBirthdate(java.time.LocalDate)
	 */
	@Override
	public void changeBirthdate(LocalDate birthdate) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#clearAllergens()
	 */
	@Override
	public void clearAllergens() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#addAllergens(BasicCommonClasses.Ingredient)
	 */
	@Override
	public void addAllergens(Ingredient allergen) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#addAllergens(java.util.Collection)
	 */
	@Override
	public void addAllergens(Collection<Ingredient> allergens) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#removeAllergen(BasicCommonClasses.Ingredient)
	 */
	@Override
	public void removeAllergen(Ingredient allergen) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see CustomerContracts.IRegisteredCustomer#removeAllergens(java.util.Collection)
	 */
	@Override
	public void removeAllergens(Collection<Ingredient> allergens) {
		// TODO Auto-generated method stub

	}

}

package CustomerContracts;

import java.util.HashSet;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Sale;
import CustomerContracts.ACustomerExceptions.*;
import SMExceptions.CommonExceptions.CriticalError;

/**
 * IRegisteredCustomer - This interface is the contract for RegisteredCustomer
 * type.
 * 
 * @author idan atias
 * @author Aviad Cohen
 * @author Lior Ben Ami
 * @since 2017-01-04
 *
 */
public interface IRegisteredCustomer extends ICustomer {

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
	
	/**
	 * getCustomerAlergans - returns the customer allergens
	 * 
	 */
	HashSet<Ingredient> getCustomerAllergens();
	
	Sale getSpecailSaleForProduct(Long barcode) throws CriticalError, CustomerNotConnected, InvalidParameter, ProductCatalogDoesNotExist;
	
	Sale offerSpecailSaleForProduct(Sale s) throws CriticalError, CustomerNotConnected, InvalidParameter, ProductCatalogDoesNotExist;
}

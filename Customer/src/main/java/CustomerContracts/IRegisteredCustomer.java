package CustomerContracts;

import java.util.HashMap;
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
	 * @return HashSet<Ingredient>
	 */
	HashSet<Ingredient> getCustomerAllergens();
	
	/**
	 * getSpecailSaleForProduct - returns special sale for product (by its barcode)
	 * 
	 * @param Long barcode
	 * @return Sale
	 * @throws CriticalError
	 * @throws CustomerNotConnected
	 * @throws InvalidParameter
	 * @throws ProductCatalogDoesNotExist
	 */
	Sale getSpecailSaleForProduct(Long barcode) throws CriticalError, CustomerNotConnected, InvalidParameter, ProductCatalogDoesNotExist;
	
	/**
	 * offerSpecailSaleForProduct - customer can offer special sale for and get responsive sale 
	 * @param Sale
	 * @return Sale
	 * @throws CriticalError
	 * @throws CustomerNotConnected
	 * @throws InvalidParameter
	 * @throws ProductCatalogDoesNotExist
	 */
	Sale offerSpecailSaleForProduct(Sale s) throws CriticalError, CustomerNotConnected, InvalidParameter, ProductCatalogDoesNotExist;
	
	/** 
	 * addSpecialSale - customer can add special sale
	 * @param Sale sale
	 * @param Boolean isTaken
	 */
	void addSpecialSale(Sale sale, Boolean isTaken);

	/**
	 * getSpecialSales - returns all existing special sale in the system
	 * @return HashMap<Sale, Boolean>
	 */
	HashMap<Sale, Boolean> getSpecialSales();
}

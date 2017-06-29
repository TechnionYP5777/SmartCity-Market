package CustomerImplementations;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;

import com.google.inject.Inject;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Sale;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import CustomerContracts.ACustomerExceptions.*;
import CustomerContracts.IRegisteredCustomer;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;
import SMExceptions.CommonExceptions.CriticalError;

/**
 * This class represents a registered customer and holds all it's
 * functionalities
 * 
 * @author idan atias
 * @author Aviad Cohen
 * 
 *         since March 31, 2017
 *
 */
public class RegisteredCustomer extends Customer implements IRegisteredCustomer {

	HashMap<Sale, Boolean> specialSales = new HashMap<Sale, Boolean>();
	
	@Inject
	public RegisteredCustomer(IClientRequestHandler clientRequestHandler) {
		super(clientRequestHandler);
	}

	@Override
	public void removeCustomer() throws CustomerNotConnected, InvalidParameter, AuthenticationError, CriticalError {
		String serverResponse;

		log.info("Creating removeCustomer command wrapper to customer with username: " + customerProfile.getUserName());

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		/* first: getting the product from the server */
		try {
			serverResponse = sendRequestWithRespondToServer(new CommandWrapper(id, CommandDescriptor.REMOVE_CUSTOMER,
					Serialization.serialize(customerProfile.getUserName())).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		CommandWrapper $ = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | CriticalError | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| GroceryListIsEmpty | ProductCatalogDoesNotExist | UsernameAlreadyExists
				| ForgotPasswordWrongAnswer ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		log.info("removeCustomer command succeed.");
	}

	@Override
	public void updateCustomerProfile(CustomerProfile p)
			throws CustomerNotConnected, InvalidParameter, AuthenticationError, CriticalError {
		String serverResponse;

		log.info("Creating updateCustomerProfile command wrapper to customer with username: " + p.getUserName());

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		/* first: getting the product from the server */
		try {
			serverResponse = sendRequestWithRespondToServer(
					new CommandWrapper(id, CommandDescriptor.UPDATE_CUSTOMER_PROFILE, Serialization.serialize(p))
							.serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		CommandWrapper $ = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler($.getResultDescriptor());
		} catch (InvalidCommandDescriptor | CriticalError | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| GroceryListIsEmpty | ProductCatalogDoesNotExist | UsernameAlreadyExists
				| ForgotPasswordWrongAnswer ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		customerProfile = p;

		log.info("updateCustomerProfile command succeed.");
	}
	
	@Override
	public HashSet<Ingredient> getCustomerAllergens() {
		return customerProfile.getAllergens();
	}

	@Override
	public Sale getSpecailSaleForProduct(Long barcode)
			throws CriticalError, CustomerNotConnected, InvalidParameter, ProductCatalogDoesNotExist {
		String serverResponse;

		log.info("Creating getSpecialSalesForProduct command wrapper");

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		try {
			serverResponse = sendRequestWithRespondToServer(
					new CommandWrapper(id, CommandDescriptor.GET_SPECIAL_SALE_FOR_PRODUCT, Serialization.serialize(barcode)).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		CommandWrapper commandWrapper = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandWrapper.getResultDescriptor());
		} catch (InvalidCommandDescriptor | CriticalError | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| GroceryListIsEmpty | AuthenticationError | UsernameAlreadyExists | ForgotPasswordWrongAnswer ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		log.info("getSpecialSalesForProduct command succeed.");

		return Serialization.deserialize(commandWrapper.getData(), Sale.class);
	}
	
	@Override
	public Sale offerSpecailSaleForProduct(Sale s)
			throws CriticalError, CustomerNotConnected, InvalidParameter, ProductCatalogDoesNotExist {
		String serverResponse;

		log.info("Creating offerSpecailSaleForProduct command wrapper");

		establishCommunication(CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);

		try {
			serverResponse = sendRequestWithRespondToServer(
					new CommandWrapper(id, CommandDescriptor.OFFER_SPECIAL_SALE_FOR_PRODUCT, Serialization.serialize(s)).serialize());
		} catch (SocketTimeoutException e) {
			log.fatal("Critical bug: failed to get respond from server");

			throw new CriticalError();
		}

		terminateCommunication();

		CommandWrapper commandWrapper = getCommandWrapper(serverResponse);

		try {
			resultDescriptorHandler(commandWrapper.getResultDescriptor());
		} catch (InvalidCommandDescriptor | CriticalError | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| GroceryListIsEmpty | AuthenticationError | UsernameAlreadyExists | ForgotPasswordWrongAnswer ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		log.info("offerSpecailSaleForProduct command succeed.");

		return Serialization.deserialize(commandWrapper.getData(), Sale.class);
	}
	
	@Override
	public void addSpecialSale(Sale s, Boolean isTaken) {
		specialSales.put(s, isTaken);
	}
	
	@Override
	public HashMap<Sale, Boolean> getSpecialSales() {
		return specialSales;
	}
}

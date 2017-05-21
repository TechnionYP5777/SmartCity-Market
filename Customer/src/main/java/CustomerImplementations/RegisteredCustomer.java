package CustomerImplementations;

import java.net.SocketTimeoutException;

import com.google.inject.Inject;

import BasicCommonClasses.CustomerProfile;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import CustomerContracts.ACustomerExceptions.*;
import CustomerContracts.IRegisteredCustomer;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.ForgotPasswordHandler;
import UtilsImplementations.ForgotPasswordHandler.NoSuchUserName;
import UtilsImplementations.ForgotPasswordHandler.WrongAnswer;
import UtilsImplementations.Serialization;
import SMExceptions.CommonExceptions.CriticalError;;

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

	protected ForgotPasswordHandler fpHandler;

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
				| GroceryListIsEmpty | ProductCatalogDoesNotExist | UsernameAlreadyExists | ForgotPasswordWrongAnswer ¢) {
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
				| GroceryListIsEmpty | ProductCatalogDoesNotExist | UsernameAlreadyExists | ForgotPasswordWrongAnswer ¢) {
			log.fatal("Critical bug: this command result isn't supposed to return here");

			throw new CriticalError();
		}

		customerProfile = p;

		log.info("updateCustomerProfile command succeed.");
	}

	@Override
	public String getForgotPasswordQuestion() throws NoSuchUserName {
		fpHandler = new ForgotPasswordHandler(CustomerDefs.loginCommandSenderId,
				clientRequestHandler, CustomerDefs.port, CustomerDefs.host, CustomerDefs.timeout);
		try {
			return fpHandler.getAuthenticationQuestion(customerProfile.getUserName());
		} catch (CriticalError | WrongAnswer e) {
			log.fatal(e + "");
			log.fatal("Failed to get authentication question from server.");
			return null;
		}
	}
	
	@Override
	public boolean sendAnswerAndNewPassword(String ans, String pass) throws WrongAnswer, NoSuchUserName {
		if (fpHandler == null){
			log.error("Failed sending answer and new password. User must first get the authentication question.");
			return false;
		}
		try {
			return fpHandler.sendAnswerWithNewPassword(ans, pass);
		} catch (CriticalError e) {
			log.fatal(e + "");
			log.fatal("Failed to get authentication question from server.");
			return false;
		}
	}
}

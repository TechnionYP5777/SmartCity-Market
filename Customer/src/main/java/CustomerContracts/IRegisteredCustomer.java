package CustomerContracts;

import BasicCommonClasses.CustomerProfile;
import CustomerContracts.ACustomerExceptions.*;
import UtilsImplementations.ForgotPasswordHandler.NoSuchUserName;
import UtilsImplementations.ForgotPasswordHandler.WrongAnswer;
import SMExceptions.CommonExceptions.CriticalError;

/**
 * IRegisteredCustomer - This interface is the contract for RegisteredCustomer
 * type.
 * 
 * @author idan atias
 * @author Aviad Cohen
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

	String getForgotPasswordQuestion() throws NoSuchUserName;

	boolean sendAnswerAndNewPassword(String ans, String pass) throws WrongAnswer, NoSuchUserName;
	
}

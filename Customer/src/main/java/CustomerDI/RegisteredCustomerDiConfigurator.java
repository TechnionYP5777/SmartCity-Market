package CustomerDI;

import com.google.inject.AbstractModule;

import ClientServerCommunication.ClientRequestHandler;
import CustomerContracts.IRegisteredCustomer;
import CustomerImplementations.RegisteredCustomer;
import UtilsContracts.IClientRequestHandler;

/**
 * @author idan atias
 * Configuring DI for RegisteredCustomer for enabling Mockito testing
 *
 */
public class RegisteredCustomerDiConfigurator extends AbstractModule {
	 @Override 
	 protected void configure() {
		  this.
	    bind(IRegisteredCustomer.class).to(RegisteredCustomer.class);
		bind(IClientRequestHandler.class).to(ClientRequestHandler.class);
	  }
}
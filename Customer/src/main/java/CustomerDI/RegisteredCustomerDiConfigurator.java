package CustomerDI;

import com.google.inject.AbstractModule;

import ClientServerCommunication.ClientRequestHandler;
import CustomerContracts.IRegisteredCustomer;
import CustomerImplementations.RegisteredCustomer;
import UtilsContracts.IClientRequestHandler;

/**
 * 
 * Configuring DI for RegisteredCustomer for enabling Mockito testing
 * 
 * @author idan atias 
 * @since 2017-01-16 
 */
public class RegisteredCustomerDiConfigurator extends AbstractModule {
	 @Override 
	 protected void configure() {
		  this.
	    bind(IRegisteredCustomer.class).to(RegisteredCustomer.class);
		bind(IClientRequestHandler.class).to(ClientRequestHandler.class);
	  }
}
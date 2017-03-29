package CustomerDI;
import com.google.inject.AbstractModule;

import ClientServerCommunication.ClientRequestHandler;
import CustomerContracts.ICustomer;
import CustomerImplementations.Customer;
import UtilsContracts.IClientRequestHandler;

/**
 * CustomerDiConfigurator - This class the dependencies configurator for Customer business logic 
 * 
 * @author Lior Ben Ami
 * @since 2017-01-16 */ 

public class CustomerDiConfigurator extends AbstractModule {
	
 @Override 
 protected void configure() {
	  this.
    bind(ICustomer.class).to(Customer.class);
	bind(IClientRequestHandler.class).to(ClientRequestHandler.class);
  }
}

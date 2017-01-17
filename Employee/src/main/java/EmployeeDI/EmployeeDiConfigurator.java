package EmployeeDI;

import com.google.inject.AbstractModule;

import ClientServerCommunication.ClientRequestHandler;
import EmployeeContracts.IWorker;
import EmployeeImplementations.Worker;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.BarcodeEventHandler;

/**
 * EmployeeDiConfigurator - This class the dependencies configurator for Employee business logic 
 * 
 * @author Shimon Azulay
 * @since 2016-12-26 */ 

public class EmployeeDiConfigurator extends AbstractModule {
	
	  @Override 
	  protected void configure() {
		  this.
	    bind(IWorker.class).to(Worker.class);
		bind(IClientRequestHandler.class).to(ClientRequestHandler.class);
		bind(IBarcodeEventHandler.class).to(BarcodeEventHandler.class);
	  }
}

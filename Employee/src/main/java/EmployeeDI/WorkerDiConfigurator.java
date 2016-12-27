package EmployeeDI;

import com.google.inject.AbstractModule;

import ClientServerCommunication.ClientRequestHandler;
import EmployeeContracts.IWorker;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;

/**
 * WorkerDiConfigurator - This class the dependencies configurator for Worker business logic 
 * 
 * @author Shimon Azulay
 * @since 2016-12-26 */ 

public class WorkerDiConfigurator extends AbstractModule {
	
	  @Override 
	  protected void configure() {
		  this.
	    bind(IWorker.class).to(Worker.class);
		bind(IClientRequestHandler.class).to(ClientRequestHandler.class);
	  }
}

package EmployeeDI;

import com.google.inject.AbstractModule;

import ClientServerCommunication.ClientRequestHandler;
import EmployeeCommon.EmployeeScreensParameterService;
import EmployeeCommon.IEmployeeScreensParameterService;
import EmployeeContracts.IManager;
import EmployeeContracts.IWorker;
import EmployeeImplementations.Manager;
import EmployeeImplementations.Worker;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.BarcodeEventHandler;

/**
 * EmployeeDiConfigurator - This class the dependencies configurator for Employee
 * 
 * @author Shimon Azulay
 * @since 2016-12-26 */ 

public class EmployeeDiConfigurator extends AbstractModule {
	
	  @Override 
	  protected void configure() {
		  this.
	    bind(IWorker.class).to(Worker.class);
		bind(IManager.class).to(Manager.class);
		bind(IEmployeeScreensParameterService.class).to(EmployeeScreensParameterService.class);	
		bind(IClientRequestHandler.class).to(ClientRequestHandler.class);
		bind(IBarcodeEventHandler.class).to(BarcodeEventHandler.class);
	  }
}

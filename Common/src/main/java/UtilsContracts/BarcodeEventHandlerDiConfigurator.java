package UtilsContracts;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import UtilsImplementations.BarcodeEventHandler;

public class BarcodeEventHandlerDiConfigurator extends AbstractModule {
	
	  @Override 
	  protected void configure() {
		  this.bind(BarcodeEventHandler.class).in(Scopes.SINGLETON);
		  this.bind(IBarcodeEventHandler.class).to(BarcodeEventHandler.class); 		 
	  }

}

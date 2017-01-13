package UtilsContracts;

import com.google.inject.AbstractModule;

import UtilsImplementations.BarcodeEventHandler;

public class BarcodeEventHandlerDiConfigurator extends AbstractModule {
	
	  @Override 
	  protected void configure() {
		  this.
	    bind(IBarcodeEventHandler.class).to(BarcodeEventHandler.class);
	  }

}

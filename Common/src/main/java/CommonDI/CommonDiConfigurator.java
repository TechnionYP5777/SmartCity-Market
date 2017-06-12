package CommonDI;

import com.google.inject.AbstractModule;

import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.IEventBus;
import UtilsContracts.IFilePathLocator;
import UtilsContracts.IPersistentStore;
import UtilsContracts.IXmlSerializerProvider;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.ProjectEventBus;
import UtilsImplementations.RootFilePathLocator;
import UtilsImplementations.XmlPersistentStore;
import UtilsImplementations.XmlSerializerProvider;


/**
 * CommonDiConfigurator - This class define the dependencies configurator for Common module
 * 
 * @author Shimon Azulay
 * @since 2017-03-28 */ 

public class CommonDiConfigurator extends AbstractModule {
	
	  @Override 
	  protected void configure() {
		  this.
		bind(IXmlSerializerProvider.class).to(XmlSerializerProvider.class);
	    bind(IFilePathLocator.class).to(RootFilePathLocator.class);
	    bind(IPersistentStore.class).to(XmlPersistentStore.class);
		bind(IBarcodeEventHandler.class).to(BarcodeEventHandler.class);
		bind(IEventBus.class).to(ProjectEventBus.class);
		    
	  }
}

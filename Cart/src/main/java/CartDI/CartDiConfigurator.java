package CartDI;
import com.google.inject.AbstractModule;

import ClientServerCommunication.ClientRequestHandler;
import CartContracts.ICart;
import CartImplementations.Cart;
import UtilsContracts.IClientRequestHandler;

/**
 * CartDiConfigurator - This class the dependencies configurator for Cart business logic 
 * 
 * @author Lior Ben Ami
 * @since 2017-01-16 */ 

public class CartDiConfigurator extends AbstractModule {
	
 @Override 
 protected void configure() {
	  this.
    bind(ICart.class).to(Cart.class);
	bind(IClientRequestHandler.class).to(ClientRequestHandler.class);
  }
}

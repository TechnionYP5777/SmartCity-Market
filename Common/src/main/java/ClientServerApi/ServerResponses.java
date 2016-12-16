package ClientServerApi;

import BasicCommonClasses.CatalogProduct;

public class ServerResponses {
	/**
	 * These are the responses coming from server as expected by client. When
	 * adding a new command, add it's response (=retVal) here, as shown: (keep
	 * conventions!)
	 * 
	 * @author idan atias
	 * @author shimon azulay
	 */

	// Base class for Response - each CommandResponse inherits it. Always holds
	// a result descriptor.
	public class Response {
		public ResultDescriptor resultDescriptor;
	}

	// LoginResponse
	public class LoginResponse extends Response {
		public LoginResponse(int senderId) {
			this.senderId = senderId;
		}

		public int senderId;
	}

	// LogoutResponse
	public class LogoutResponse extends Response {
	}

	// ViewProductFromCatalogResponse
	public class ViewProductFromCatalogResponse extends Response {
		public ViewProductFromCatalogResponse(CatalogProduct catProd) {
			this.catProd = catProd;
		}

		public CatalogProduct catProd;
	}

}

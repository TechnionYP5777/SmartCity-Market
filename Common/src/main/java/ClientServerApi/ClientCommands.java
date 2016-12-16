package ClientServerApi;

public class ClientCommands {
	/**
	 * This is our API-Commands between client and server. When adding a new
	 * command, add it here as shown below: (keep conventions!)
	 * 
	 * @author idan atias
	 * @author shimon azulay
	 */

	// Base Command class: each command inherits from this base class
	public class Command {
		public CommandDescriptor commandDescriptor;
	}

	public class LoginCommand extends Command {
		public LoginCommand(String username, String password) {
			commandDescriptor = CommandDescriptor.LOGIN;
			this.username = username;
			this.password = password;
		}

		public String username;
		public String password;
	}

	public class LogoutCommand extends Command {
		public LogoutCommand(int senderId) {
			commandDescriptor = CommandDescriptor.LOGOUT;
			this.senderId = senderId;
		}

		public int senderId;
	}

	public class ViewProductFromCatalogCommand extends Command {
		public ViewProductFromCatalogCommand(int barcode) {
			commandDescriptor = CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG;
			this.barcode = barcode;
		}

		public int barcode;
	}

}

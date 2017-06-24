package ExpiredProducts;

import java.util.HashSet;

import BasicCommonClasses.ProductPackage;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeImplementations.Worker;
import SMExceptions.CommonExceptions.CriticalError;

/**
 * ExpiredProducts - This singleton class supply the service that deals with expired products in SmartMarket
 *
 * @author Lior Ben Ami
 * @since 2017-06-24
 */
public class ExpiredProducts {
	private Worker worker = null;
	private static ExpiredProducts instance = null;
	
	HashSet<ProductPackage> expiredProducts = null;
	
	private ExpiredProducts(Worker worker) {
		this.worker = worker;
		expiredProducts = new HashSet<ProductPackage>();
		GetAllExpiredProdctsFromDB();
		AlertWorker();
	}
	
	public static ExpiredProducts getInstance(Worker worker) {
		if(instance == null) {
			instance = new ExpiredProducts(worker);
		}
		return instance;
	}
	
	private void GetAllExpiredProdctsFromDB() {
		try {
			expiredProducts = worker.getAllExpiredProductPackages();
		} catch (ConnectionFailure | CriticalError | InvalidParameter | EmployeeNotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void AlertWorker() {
		if (expiredProducts.isEmpty())
			return;
		
	}
	
	
}


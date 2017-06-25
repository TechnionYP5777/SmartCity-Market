package ExpiredProducts;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.google.common.eventbus.EventBus;

import BasicCommonClasses.ProductPackage;
import EmployeeCommon.AEmployee;
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
@Singleton
public class ExpiredProducts  implements IExpiredProducts {
	public static final Integer EXPIRED_PRODUCTS_DEFAULT_PORT = 5000;
	static final Integer DURATION_TIME_MILISECS = 3600000; //1 hour
	private Worker worker = null;
	
	protected static Logger log = Logger.getLogger(AEmployee.class.getName());
	
	private Timer timer;
	
	private EventBus expiredProductsEventBus;
	
	HashSet<ProductPackage> expiredProducts = new HashSet<ProductPackage>();
	
	private ExpiredProducts(Worker worker) throws CriticalError {
		this.worker = worker;
	}
	

	private void GetAllExpiredProdctsFromDB() {
		try {
			expiredProducts = worker.getAllExpiredProductPackages();
		} catch (ConnectionFailure | CriticalError | InvalidParameter | EmployeeNotConnected e) {
			log.fatal("Critical bug: this command result isn't supposed to return here");
		}
	}
	
	class ExpiredProductsAlert extends TimerTask {

        @Override
		public void run() {
			GetAllExpiredProdctsFromDB();
			if (expiredProducts.isEmpty())
				return;
			expiredProductsEventBus.post(new ExpiredProductsEvent(expiredProducts));
        }
	}
	
	public void start()  {
		expiredProductsEventBus = new EventBus();
		timer = new Timer();
		timer.schedule(new ExpiredProductsAlert(), DURATION_TIME_MILISECS);	
	}
	
	
	
	public void register(Object listener) {
		expiredProductsEventBus.register(listener);
	}
	
	public void unregister(Object listener) {
		expiredProductsEventBus.unregister(listener);
	}
	
}


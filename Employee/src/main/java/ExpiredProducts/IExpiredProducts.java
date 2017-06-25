package ExpiredProducts;

/**
 * IExpiredProducts - interface presents ExpiredPeoducts event
 * 
 * @author Lior Ben Ami 
 * @since  2017-06-25
 *
 */
public interface IExpiredProducts {
	public void start();
	
	public void register(Object listener);
	
	public void unregister(Object listener);
	
}

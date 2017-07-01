package UtilsContracts;

/**
 * this interface helps the caller of confirmation dialog to get indication about click Abort button
 * 
 * @author Shimon Azulay
 * @since 26.6.17
 *
 */

public interface IEventBus {
	
	/**
	 * 
	 * @param obj - the object to register to the event bus
	 */
	void register(Object obj);
	
	/**
	 * 
	 * @param obj - the object to unregister from the event bus
	 */
	void unRegister(Object obj);
	
	/**
	 * 
	 * @param obj - the object to post to
	 */
	void post(Object obj);

}

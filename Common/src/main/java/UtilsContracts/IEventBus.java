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
	 * @param o - the object to register to the event bus
	 */
	void register(Object o);
	
	/**
	 * 
	 * @param o - the object to unregister from the event bus
	 */
	void unRegister(Object o);
	
	/**
	 * 
	 * @param o - the object to post to
	 */
	void post(Object o);

}

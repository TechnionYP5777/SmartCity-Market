package UtilsContracts;


/** IPersistentStore - An interface that represents persistence.
 * 
 * @author Shimon Azulay
 * @since 2016-03-28 */

public interface IPersistentStore {

	/**
	 * store object
	 * 
	 * @param requester
	 * @param objectToStore
	 * @throws Exception
	 */
	void storeObject(Object requester, Object objectToStore) throws Exception;
	
	/**
	 * restore object
	 * 
	 * @param requester
	 * @param restoredType
	 * @return
	 * @throws Exception
	 */
	<TObject> TObject restoreObject(Object requester, Class<TObject> restoredType) throws Exception;
}

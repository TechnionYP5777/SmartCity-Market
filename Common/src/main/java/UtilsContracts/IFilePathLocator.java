package UtilsContracts;

import java.io.IOException;



/** IFilePathLocator - An interface that represents utilities in the application directories.
 * @author Shimon Azulay
 * @since 2016-03-29 */

public interface IFilePathLocator {
	
	/**
	 * @param contextClass
	 * @param relativeFilePath
	 * 
	 * @return absolute path to the file
	 * 
	 * @throws IOException
	 */
	String getFilePath(Class<?> contextClass, String relativeFilePath);
	
	/**
	 * @param pluginId
	 * @param relativeFilePath fileName from root directory 
	 * @return
	 */
	public String getFilePath(String pluginId, String relativeFilePath);
	
	public boolean isExists(Class<?> contextClass, String relativeFilePath);
	public boolean isExists(String pluginId, String relativeFilePath);
}

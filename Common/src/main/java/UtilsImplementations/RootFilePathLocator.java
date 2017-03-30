package UtilsImplementations;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import UtilsContracts.IFilePathLocator;

/** RootFilePathLocator - An impl that represents utilities in the application directories.
 * @author Shimon Azulay
 * @since 2016-03-29 */

public class RootFilePathLocator implements IFilePathLocator {

	@Override
	public String getFilePath(Class<?> bundleClass, String relativePath) {
		Path currentRelativePath = Paths.get("");
		String absolutePath = currentRelativePath.toAbsolutePath().toString();
		File file = new File(absolutePath, relativePath);
		return file.getPath();
	}

	@Override
	public String getFilePath(String pluginId, String relativeFilePath) {
		return getFilePath(RootFilePathLocator.class, relativeFilePath);
	}

	@Override
	public boolean isExists(Class<?> contextClass, String relativeFilePath) {
		return new File(getFilePath(contextClass, relativeFilePath)).exists();
	}

	@Override
	public boolean isExists(String pluginId, String relativeFilePath) {
		return new File(getFilePath(pluginId, relativeFilePath)).exists();
	}

	

}

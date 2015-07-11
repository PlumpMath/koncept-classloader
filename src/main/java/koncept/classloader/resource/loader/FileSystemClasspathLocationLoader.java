package koncept.classloader.resource.loader;

import java.io.File;

import koncept.classloader.resource.ClasspathLocation;
import koncept.classloader.resource.ClasspathLocationLoader;
import koncept.classloader.resource.loader.location.DirectoryLocation;
import koncept.classloader.resource.loader.location.JarLocation;

public class FileSystemClasspathLocationLoader implements ClasspathLocationLoader {

	@Override
	public ClasspathLocation attemptGet(ClasspathLocation parent, String location) {
		File file = new File(location);
		if (file.exists() && file.isDirectory()) {
			DirectoryLocation directoryLocation =  new DirectoryLocation(parent, file);
			if (directoryLocation.isValid()) return directoryLocation;
		}
		
		if (file.exists() && file.isFile() && file.getName().toLowerCase().endsWith(".jar"))
			return new JarLocation(parent, new FileResource(parent, file));
		
		return null;
	}

	
}

package koncept.classloader.resource.loader.location;

import java.io.File;

import koncept.classloader.resource.ClasspathLocation;
import koncept.classloader.resource.ClasspathResource;
import koncept.classloader.resource.loader.FileResource;

public class DirectoryLocation implements ClasspathLocation {

	private final ClasspathLocation parent;
	private File file;
	
	public DirectoryLocation (ClasspathLocation parent, File file) {
		this.parent = parent;
		this.file = file;
	}
	
	public DirectoryLocation (File file) {
		this.parent = null;
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}
	
	public boolean isValid() {
		return file.exists() && file.isDirectory();
	}
	
	@Override
	public ClasspathLocation getParent() {
		return parent;
	}
	
	@Override
	public ClasspathResource getClasspathResource(String resource) {
		if (file.isDirectory()) {
			File resourceFile = new File(file, resource);
			FileResource fileResource = new FileResource(this, resourceFile);
			if (fileResource.isValid()) return fileResource;
		}
		return null;
	}
	
	public String toString() {
		return file.toURI().toString();
	}
	
	@Override
	public String asURLString() {
		return file.toURI().toString();
	}

}
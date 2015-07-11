package koncept.classloader.resource.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import koncept.classloader.resource.ClasspathLocation;
import koncept.classloader.resource.ClasspathResource;

public class FileResource implements ClasspathResource {

	private ClasspathLocation parent;
	private File file;
	
	public FileResource(ClasspathLocation parent, File file) {
		this.parent = parent;
		this.file = file;
	}
	
	@Override
	public ClasspathLocation getClasspathLocation() {
		return parent;
	}

	@Override
	public String asURLString() {
		return file.toURI().toString();
	}

	@Override
	public InputStream getStream() throws IOException {
		return new FileInputStream(getFile());
	}
	
	private File getFile() {
		return file;
	}
	
	public boolean isValid() {
		return file.exists() && file.isFile();
	}

}

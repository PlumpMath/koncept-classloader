package koncept.classloader.resource.loader.location;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import koncept.classloader.resource.ClasspathLocation;
import koncept.classloader.resource.ClasspathResource;
import koncept.classloader.resource.loader.JarResource;

public class JarLocation implements ClasspathLocation {

	private final ClasspathLocation parent;
	private ClasspathResource jar;
	
	public JarLocation (ClasspathLocation parent, ClasspathResource jar) {
		this.parent = parent;
		this.jar = jar;
	}
	
	@Override
	public ClasspathLocation getParent() {
		return parent;
	}
	
	@Override
	public ClasspathResource getClasspathResource(String resource) {
		try (InputStream in = jar.getStream()) {
			ZipInputStream zIn = new ZipInputStream(in);
			ZipEntry ze = zIn.getNextEntry();
			while (ze != null) {
				if (ze.getName().equals(resource)) 
					return new JarResource(this, resource);
				ze = zIn.getNextEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString() {
		return jar.asURLString();
	}
	
	public ClasspathResource getJar() {
		return jar;
	}
	
	@Override
	public String asURLString() {
		return jar.asURLString();
	}

}
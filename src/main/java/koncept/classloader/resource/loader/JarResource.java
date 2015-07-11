package koncept.classloader.resource.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import koncept.classloader.resource.ClasspathLocation;
import koncept.classloader.resource.ClasspathResource;
import koncept.classloader.resource.loader.location.JarLocation;

public class JarResource implements ClasspathResource {

	private final JarLocation parent;
	private final String entry;
	
	public JarResource(JarLocation parent, String entry) {
		this.parent = parent;
		this.entry = entry;
	}

	@Override
	public String asURLString() {
		return parent.asURLString() + "!" + entry;
	}
	
	@Override
	public ClasspathLocation getClasspathLocation() {
		return parent;
	}
	
	@Override
	public InputStream getStream() throws IOException {
		InputStream in = null;
		try {
			in = parent.getJar().getStream();
			ZipInputStream zIn = new ZipInputStream(in);
			ZipEntry ze = zIn.getNextEntry();
			while (ze != null) {
				if (ze.getName().equals(entry)) 
					return zIn;
				ze = zIn.getNextEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
			in.close();
		}
		return null;
	}
	
}

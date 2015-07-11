package koncept.classloader.resource;

import java.io.IOException;
import java.io.InputStream;

public interface ClasspathResource {

	ClasspathLocation getClasspathLocation(); //loading classpath location
	String asURLString();
	InputStream getStream() throws IOException;
	
}

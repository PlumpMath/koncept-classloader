package koncept.classloader.resource;

public interface ClasspathLocation {

	ClasspathLocation getParent(); //null for top level
	ClasspathResource getClasspathResource(String resource);
	public String asURLString();
}

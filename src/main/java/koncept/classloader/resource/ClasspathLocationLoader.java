package koncept.classloader.resource;

public interface ClasspathLocationLoader {

	public ClasspathLocation attemptGet(ClasspathLocation parent, String location);
	
}

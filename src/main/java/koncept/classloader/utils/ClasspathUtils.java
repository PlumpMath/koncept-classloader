package koncept.classloader.utils;

import java.util.ArrayList;
import java.util.List;

import koncept.classloader.resource.ClasspathLocation;
import koncept.classloader.resource.ResourceFactory;

public class ClasspathUtils {
	
	ResourceFactory resourceFactory;
	
	public ClasspathUtils() {
		resourceFactory = new ResourceFactory();
	}
	
	public ClasspathUtils(ResourceFactory resourceFactory) {
		this.resourceFactory = resourceFactory;
	}

	
	
	public List<ClasspathLocation> getSystemClasspath() {
		return parse(System.getProperty("java.class.path"));
	}
	
	public List<ClasspathLocation> parse(String classpath) {
		String pathSeperator = System.getProperty("path.separator");
		// do we need to literalise the regex?
		return parse(classpath.split(pathSeperator));
	}
	
	public List<ClasspathLocation> parse(String[] classpath) {
		List<ClasspathLocation> resources = new ArrayList<ClasspathLocation>();
		for (String s: classpath) {
			ClasspathLocation location = resourceFactory.parse(null, s);
			if (location != null) {
				resources.add(location);
			}
		}
		return resources;
	}
}

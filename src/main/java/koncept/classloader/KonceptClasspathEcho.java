package koncept.classloader;

import java.util.List;

import koncept.classloader.resource.ClasspathLocation;

public class KonceptClasspathEcho {

	public static void main(String[] args) throws Exception {
		System.out.println("System classpath: " + System.getProperty("java.class.path"));
		
		ClassLoader loader = KonceptClasspathEcho.class.getClassLoader();
		System.out.println("Entry point classloader was: " + loader.getClass().getName());
		
		if (loader.getClass().getName().equals(KonceptClassLoader.class.getName())) {
			if (loader instanceof KonceptClassLoader) {
				System.out.println("KonceptClassLoader from same classloader as KonceptClasspathEcho");
				List<ClasspathLocation> classpathLocations = ((KonceptClassLoader)loader).classpathLocations();
				for(ClasspathLocation classpathLocation: classpathLocations)
					System.out.println("Classpath location: " + classpathLocation);
			} else {
				System.out.println("KonceptClassLoader from different classloader as KonceptClasspathEcho");
				List<Object> classpathLocations = (List)loader.getClass().getMethod("classpathLocations").invoke(loader);
				for(Object classpathLocation: classpathLocations)
					System.out.println("Classpath location: " + classpathLocation);
			}
		}
		if (loader instanceof KonceptClassLoader) {
			System.out.println("Entry point classloader is a koncept classloader");
			List<ClasspathLocation> classpathLocations = ((KonceptClassLoader)loader).classpathLocations();
			for(ClasspathLocation classpathLocation: classpathLocations)
				System.out.println("Classpath location: " + classpathLocation);	
		}
	}
	
}

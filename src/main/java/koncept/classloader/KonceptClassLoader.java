package koncept.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import koncept.classloader.filter.LoadingFilter;
import koncept.classloader.filter.SystemLoadingFilter;
import koncept.classloader.resource.ClasspathLocation;
import koncept.classloader.resource.ClasspathResource;
import koncept.classloader.utils.ClasspathUtils;

public class KonceptClassLoader extends ClassLoader {

	private final List<ClasspathLocation> classpathLocations;
	private final List<KonceptClassLoader> childLoaders = new ArrayList<>();	
	
	private Map<String, Class<?>> loadedClasses = new HashMap<String, Class<?>>();
	
	private LoadingFilter loadingFilter = new SystemLoadingFilter();
	
	public KonceptClassLoader() {
		this(new ClasspathUtils().getSystemClasspath());
	}
	public KonceptClassLoader(String classpath) {
		this(new ClasspathUtils().parse(classpath));
	}
	public KonceptClassLoader(List<ClasspathLocation> classpathLocations) {
		this(null, classpathLocations);
	}
	public KonceptClassLoader(KonceptClassLoader parent, ClasspathLocation classpathLocations) {
		this(parent, Arrays.asList(classpathLocations));
	}
	public KonceptClassLoader(KonceptClassLoader parent, List<ClasspathLocation> classpathLocations) {
		super(parent);
		this.classpathLocations = classpathLocations;
	}
	
	public static KonceptClassLoader forLocations(List<ClasspathLocation> classpathLocations) {
		KonceptClassLoader parent = new KonceptClassLoader(Collections.emptyList());
		for(ClasspathLocation classpathLocation: classpathLocations)
			parent.childLoader(classpathLocation);
		return parent;
	}
	
	public KonceptClassLoader childLoader(ClasspathLocation location) {
		KonceptClassLoader child = new KonceptClassLoader(this, location);
		return childLoader(child);
	}
	
	public KonceptClassLoader childLoader(KonceptClassLoader child) {
		childLoaders.add(child);
		return child;
	}
	
	
	
	public List<ClasspathLocation> classpathLocations() {
		return new ArrayList<>(classpathLocations);
	}
	
	public List<KonceptClassLoader> children() {
		return new ArrayList<KonceptClassLoader>(childLoaders);
	}
	

	@Override
	/**
	 * This is the main entry point to a classloader, via JVM callback
	 * 
	 * @param name
	 * @param resolve
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if (loadingFilter.loadInParent(name)){
			ClassLoader parent = getParent();
			if (parent == null)
				parent = getSystemClassLoader();
			return parent.loadClass(name);
		}
		
		Class<?> c = loadClassDirectly(name, resolve);
		
		if (c == null) {
			for(KonceptClassLoader child: childLoaders) {
				c = child.loadClassFromParent(name, resolve);
				if (c != null) return c;
			}
		}
		
		
		
		if (c == null && parentIsKonceptClassLoader()) {
			KonceptClassLoader parent = getParentKonceptClassLoader();
			for(KonceptClassLoader child: parent.childLoaders)
				if (child != this) {
					c = child.loadClassFromSibling(name, resolve);
					if (c != null) return c;
				}
		}
		
		if (c == null) throw new ClassNotFoundException(name);
		return c;
		
	}
	
	protected Class<?> getLoadedClass(String name) {
		return loadedClasses.get(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param resolve
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> loadClassDirectly(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> c = getLoadedClass(name);
		if (c != null) return c;
		
		String classLocation = name.replace('.', '/') + ".class";
		
		for (ClasspathLocation location: classpathLocations) {
			ClasspathResource loadedResource = location.getClasspathResource(classLocation);
			if (loadedResource != null) {
				
				try {
					InputStream is = loadedResource.getStream();
					c = defineClass(name, is);
					if (resolve) { //run here for efficiency...
					    resolveClass(c);
					}
					loadedClasses.put(name, c);
					return c;
				} catch (IOException e) {
					throw new ClassNotFoundException(name, e);
				}
				
			}
		}
		return null;
	}
	
	/**
	 * This is used for loading SIBLING classes.
	 * @param name
	 * @param resolve
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> loadClassFromSibling(String name, boolean resolve) throws ClassNotFoundException {
		/*
		 * It turns out that the logic for this is the same as loading a class directly
		 */
		return loadClassDirectly(name, resolve);
	}
	
	/**
	 * This is used for loading CHILD classes.
	 * @param name
	 * @param resolve
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> loadClassFromParent(String name, boolean resolve) throws ClassNotFoundException {
		/*
		 * It turns out that the logic for this is the same as loading a class directly
		 */
		return loadClassDirectly(name, resolve);
	}
	
	
	
	

	
	private boolean parentIsKonceptClassLoader() {
		return getParent() != null && getParent() instanceof KonceptClassLoader;
	}
	private KonceptClassLoader getParentKonceptClassLoader() {
		if (parentIsKonceptClassLoader()) return (KonceptClassLoader)getParent();
		return null;
	}
	
	
	protected Class<?> defineClass(String name, InputStream is) throws ClassNotFoundException {
		try {
			int size = 512;
			int done = 0;
			byte[] b = new  byte[size];
			int read = is.read(b, done, size);
			
			while (read != -1) {
				if (read == 0) continue;
				done += read;
				byte[] copy = new byte[done + size];
				System.arraycopy(b, 0, copy, 0, done);
				b = copy;
				read = is.read(b, done, size);
			}
			
			return defineClass(name, b, 0, done);
		} catch (IOException e) {
			throw new ClassNotFoundException(name, e);
		}
	}
}

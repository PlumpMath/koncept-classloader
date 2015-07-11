package koncept.classloader.resource;

import java.util.ArrayList;
import java.util.Collection;

import koncept.classloader.exception.KonceptClassException;
import koncept.classloader.resource.loader.FileSystemClasspathLocationLoader;

public class ResourceFactory {
	
	Collection<ClasspathLocationLoader> loaders = new ArrayList<ClasspathLocationLoader>();
	
	
	public ResourceFactory() {
		loadDefaults();
	}
	
	public ResourceFactory(Collection<ClasspathLocationLoader> loaders) {
		if (loaders != null) {
			this.loaders = loaders;
		}
		else {
			loadDefaults();
		}
	}
	
	
	private void loadDefaults() {
		addLoader(FileSystemClasspathLocationLoader.class);
	}
	
	public void addLoader(Class<? extends ClasspathLocationLoader> loaderClass) {
		if (ClasspathLocationLoader.class.isAssignableFrom(loaderClass)) {
			try {
				ClasspathLocationLoader loader = loaderClass.newInstance();
				addLoader(loader);
			} catch (InstantiationException e) {
				throw new KonceptClassException(e);
			} catch (IllegalAccessException e) {
				throw new KonceptClassException(e);
			} 
		} else {
			throw new KonceptClassException(new ClassCastException());
		}
	}
	
	public void addLoader(ClasspathLocationLoader loader) {
		loaders.add(loader);
	}
	
	public ClasspathLocation parse(ClasspathLocation parentLocation, String resource) {
		for(ClasspathLocationLoader loader: loaders) {
			ClasspathLocation location = loader.attemptGet(parentLocation, resource);
			if (location != null) return location;
		}
		return null;
	}

}

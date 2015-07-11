package koncept.classloader;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.jar.Manifest;

public class KonceptJarRunner {

	private static String MANIFEST_LOCATION = "META-INF/MANIFEST.MF";
	
	private static String ATTR_MAIN_CLASS = "Main-Class";
	private static String ATTR_CLASS_PATH = "Class-Path";
	
	public static void main(String[] args) throws Exception {
		KonceptClassLoader loader = new KonceptClassLoader();

		InputStream is = KonceptJarRunner.class.getResourceAsStream(MANIFEST_LOCATION);
		Manifest mf = is != null ? new Manifest(is) : null;
		
		if (mf != null) {
			String classPath = mf.getMainAttributes().getValue(ATTR_CLASS_PATH);
			if (classPath != null) {
				loader = new KonceptClassLoader(classPath);
			}
		}
		
		String mainClassName = System.getProperty(ATTR_MAIN_CLASS);
		if (mainClassName == null && mf != null) {
			//try and load from manifest
			mainClassName = mf.getMainAttributes().getValue(ATTR_MAIN_CLASS);
		}
		if (mainClassName == null) throw new RuntimeException("No main class to load");
		
		
		
		Class<?> mainClass = loader.loadClass(mainClassName);
		Method method = mainClass.getMethod("main", args.getClass()); //throws NoSuchMethodException
		if (!Modifier.isStatic(method.getModifiers())) throw new RuntimeException("No main method to execute");
		method.invoke(null, new Object[] {args});
	}
	
}

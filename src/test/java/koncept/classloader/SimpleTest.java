package koncept.classloader;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTest {

	
	@Test
	public void checkCanLoadClass() throws Exception {
		KonceptClassLoader loader = new KonceptClassLoader();
		Class<?> c = loader.loadClass(getClass().getName());
		assertNotNull(c);
	}
	
	@Test
	public void checkLoadedClassIdentity() throws Exception {
		KonceptClassLoader loader = new KonceptClassLoader();
		Class<?> c = loader.loadClass(getClass().getName());
		assertNotNull(c);
		assertEquals(c, loader.loadClass(SimpleTest.class.getName()));
		assertNotSame(c, getClass());
	}
	
}

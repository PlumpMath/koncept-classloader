package koncept.classloader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

public class NestedClassTest {

	
	@Test
	public void checkNestedClassLoading() throws Exception {
		KonceptClassLoader loader = new KonceptClassLoader();
		
		assertNotNull(loader.loadClass(NestedClassTest.FirstLevelNestedClass.class.getName()));
		assertNotNull(loader.loadClass(NestedClassTest.FirstLevelNestedClass.SecondLevelNestedClass.class.getName()));
		assertNotNull(loader.loadClass(NestedClassTest.NestedInterface.class.getName()));
		
		assertNotNull(loader.loadClass(NestedClassTest.NestingWrapper.DoubleNested.class.getName()));
		Class c = loader.loadClass(NestedClassTest.NestingWrapper.DoubleNested.class.getName());
		Object impl = c.newInstance();
		Method method = c.getMethod("execute");
		Object returnValue = method.invoke(impl);
		assertNotNull(returnValue);
		assertTrue((Boolean)returnValue);
		
	}
	
	
	
	static class FirstLevelNestedClass {
		
		static class SecondLevelNestedClass {
			
		}
	}
	
	static interface NestedInterface {
		public boolean execute();
	}
	
	static class Nested implements NestedInterface {
		public boolean execute() {return true;}
	}
	
	public static class NestingWrapper {
		public static class DoubleNested implements NestedInterface {
			public boolean execute() {return true;}
		}
	}
}

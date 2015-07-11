package koncept.classloader.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import koncept.classloader.resource.ClasspathLocation;

import org.junit.Test;

public class ClasspathUtilsTest {

	@Test
	public void canLoadSomeOfSystemClasspath() {
		List<ClasspathLocation> locations = new ClasspathUtils().getSystemClasspath();
		assertNotNull(locations); //should always be true
		assertFalse(locations.isEmpty()); //should be able to understand the system classpath
		
		int expectedSize = System.getProperty("java.class.path").split(System.getProperty("path.separator")).length;
		assertThat(locations.size(), is(expectedSize));
	}
	
	@Test
	public void checkCurrentDirectory() {
		List<ClasspathLocation> locations = new ClasspathUtils().parse(".");
		assertNotNull(locations);
		assertEquals(1, locations.size()); //classpath is current directory
	}
	
}

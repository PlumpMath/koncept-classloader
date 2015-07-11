package koncept.classloader.filter;

public class SystemLoadingFilter implements LoadingFilter {

	@Override
	public boolean loadInParent(String name) {
		return name.startsWith("java.")|| name.startsWith("javax.");
	}

}

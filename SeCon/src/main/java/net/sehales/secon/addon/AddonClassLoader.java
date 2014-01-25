package net.sehales.secon.addon;

import java.net.URL;
import java.net.URLClassLoader;

public class AddonClassLoader extends URLClassLoader {

	public AddonClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}

}

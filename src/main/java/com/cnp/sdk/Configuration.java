package com.cnp.sdk;

import java.io.File;

public class Configuration {
	
	private static final String CNP_SDK_CONFIG = ".cnp_SDK_config.properties";

	public File location() {
		File file = new File(System.getProperty("user.home") + File.separator + CNP_SDK_CONFIG);
		if(System.getProperty("java.specification.version").equals("1.4")) {
			if(System.getProperty("CNP_CONFIG_DIR") != null) {
				file = new File(System.getProperty("CNP_CONFIG_DIR") + File.separator + CNP_SDK_CONFIG);
			}
		} else {
			if(System.getenv("CNP_CONFIG_DIR") != null) {
				if(System.getenv("CNP_CONFIG_DIR").equals("classpath:" + CNP_SDK_CONFIG)) {
					if (getClass().getClassLoader().getResource(CNP_SDK_CONFIG) != null) {
						String filePath = getClass().getClassLoader().getResource(CNP_SDK_CONFIG).getPath();
						file = new File(filePath);
					}
				} else {
					file = new File(System.getenv("CNP_CONFIG_DIR") + File.separator + CNP_SDK_CONFIG);
				}
			}
		}

		return file;
	}
}

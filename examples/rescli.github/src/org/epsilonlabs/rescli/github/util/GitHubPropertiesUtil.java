package org.epsilonlabs.rescli.github.util;

import java.util.Properties;

import org.epsilonlabs.rescli.core.util.PropertiesUtil;

public class GitHubPropertiesUtil {

	private static final String PROPERTIES_FILE = "github.properties";

	public static String get(String property){
		Properties properties = PropertiesUtil.load(GitHubPropertiesUtil.class, PROPERTIES_FILE);
		return properties.getProperty(property);
	}

}
package org.scrutmydocs.webapp.util;

import com.google.common.io.Files;
import org.scrutmydocs.webapp.configuration.ScrutMyDocsProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyScanner {

    public static ScrutMyDocsProperties scanPropertyFile() throws IOException {
		ScrutMyDocsProperties smdProps = new ScrutMyDocsProperties();
		
		String userHome = System.getProperty("user.home");
		FileSystemResource resource = new FileSystemResource(userHome + "/.scrutmydocs/config/scrutmydocs.properties");
		Properties props = null;
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);		
		} catch (FileNotFoundException e) {
			// File does not exists ? Who cares ?
		}

		if (props == null) {
			// Build the file from the Classpath
			FileSystemResource configDir = new FileSystemResource(userHome + "/.scrutmydocs/config/");
			ClassPathResource classPathResource = new ClassPathResource("/scrutmydocs/config/scrutmydocs.properties");
			
			File from = classPathResource.getFile();
			File dest = new File(configDir.getFile(),from.getName());
			Files.createParentDirs(dest);
			Files.copy(from, dest);

			props = PropertiesLoaderUtils.loadProperties(resource);
		}
		
		if (props == null) {
			throw new RuntimeException("Can not build ~/.scrutmydocs/config/scrutmydocs.properties file. Check that current user have a write access");
		}
		else {
			smdProps.setNodeEmbedded(getProperty(props, "node.embedded", true));
			smdProps.setClusterName(getProperty(props, "cluster.name", "scrutmydocs"));
			FileSystemResource configDir = new FileSystemResource(userHome + "/.scrutmydocs/config/esdata/");
			smdProps.setPathData(getProperty(props, "path.data", configDir.getPath()));
			smdProps.setNodeAdresses(getPropertyAsArray(props, "node.addresses", "localhost:9300,localhost:9301"));
			smdProps.setDropboxKey(getProperty(props, "dropbox.app.key", null));
            smdProps.setDropboxSecret(getProperty(props, "dropbox.app.secret", null));

			// We check some rules here :
			// if node.embedded = false, we must have an array of node.adresses
			if (!smdProps.isNodeEmbedded() && (smdProps.getNodeAdresses() == null || smdProps.getNodeAdresses().length < 1)) {
				throw new RuntimeException("If you don't want embedded node, you MUST set node.addresses property.");
			}
		}
		
		return smdProps;
	}

    private static boolean getProperty(Properties props, String path,
	    boolean defaultValue) {
	String sValue = getProperty(props, path, Boolean.toString(defaultValue));
	if (sValue == null)
	    return false;
	return Boolean.parseBoolean(sValue);
    }

    private static String getProperty(Properties props, String path,
	    String defaultValue) {
	return props.getProperty(path, defaultValue);
    }

    private static String[] getPropertyAsArray(Properties props, String path,
	    String defaultValue) {
	String sValue = getProperty(props, path, defaultValue);
	if (sValue == null)
	    return null;

	// Remove spaces and split results with comma
	String[] arrStr = StringUtils.commaDelimitedListToStringArray(sValue);

	return arrStr;
    }
}

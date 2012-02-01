package fr.issamax.dao.elastic.factory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A {@link FactoryBean} implementation used to create a {@link Client} element
 * from a {@link Node}.
 * <p>
 * The lifecycle of the underlying {@link Client} instance is tied to the
 * lifecycle of the bean via the {@link #destroy()} method which calls
 * {@link Client#close()}
 * 
 * @author David Pilato
 */
public class ElasticsearchClientFactoryBean extends ElasticsearchAbstractClientFactoryBean {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	Node node;

	@Override
	protected Client buildClient() throws Exception {
		if (node == null)
			throw new Exception(
					"You must define an ElasticSearch Node as a Spring Bean.");
		return node.client();
	}
	
	public static String sign(String toSign) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(toSign.getBytes());

		String key = "";
		byte b[] = md.digest();
		for (int i = 0; i < b.length; i++) {
			long t = b[i] < 0 ? 256 + b[i] : b[i];
			key += Long.toHexString(t);
		}

		return key;
	}
}

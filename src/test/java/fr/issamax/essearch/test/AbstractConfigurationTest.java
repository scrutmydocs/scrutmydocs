package fr.issamax.essearch.test;


import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Just launch the Spring factory
 * @author PILATO
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:annotation-context.xml"
		})
public class AbstractConfigurationTest {
	
	@Autowired Node node;

	@Autowired Client client;
}

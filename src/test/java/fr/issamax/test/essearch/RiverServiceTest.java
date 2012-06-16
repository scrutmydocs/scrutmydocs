package fr.issamax.test.essearch;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.service.RiverService;
import fr.issamax.essearch.constant.ESSearchProperties;

public class RiverServiceTest extends AbstractConfigurationTest {

	@Autowired RiverService riverService;
	
	@Test public void test_add_river() throws InterruptedException {
		Assert.assertNotNull(riverService);

		FSRiver fsriver = new FSRiver("mytestriver", ESSearchProperties.INDEX_NAME, 
						ESSearchProperties.INDEX_TYPE_DOC, "fs", "tmp", "/tmp_es", 30L, "standard", false);		

		riverService.add(fsriver);
	}
	
	@Test public void test_remove_river() throws InterruptedException {
		Assert.assertNotNull(riverService);

		FSRiver fsriver = new FSRiver("mytestriver", ESSearchProperties.INDEX_NAME, 
						ESSearchProperties.INDEX_TYPE_DOC, "fs", "tmp", "/tmp_es", 30L, "standard", false);		

		riverService.add(fsriver);
		
		// We have to wait for 1s
		Thread.sleep(1000);
		
		riverService.delete(fsriver);
	}

}

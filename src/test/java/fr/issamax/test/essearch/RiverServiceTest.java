package fr.issamax.test.essearch;

import java.util.Collection;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.data.FSRiverHelper;
import fr.issamax.essearch.admin.river.service.RiverService;
import fr.issamax.essearch.constant.ESSearchProperties;

public class RiverServiceTest extends AbstractConfigurationTest {

	@Autowired RiverService riverService;
	
	@Test public void test_add_river() throws InterruptedException {
		Assert.assertNotNull(riverService);

		XContentBuilder xb = FSRiverHelper.toXContent(
				ESSearchProperties.INDEX_NAME, 
				ESSearchProperties.INDEX_TYPE_DOC, 
				new FSRiver("fs", "tmp", "/tmp_es", 30L));		
		
		client.prepareIndex("_river", "mytestriver", "_meta").setSource(xb)
				.execute().actionGet();
		
		// We have to wait for 1s
		Thread.sleep(1000);
		
		Collection<FSRiver> rivers = riverService.get();
		Assert.assertEquals("Rivers should not be empty", 1, rivers.size());
	}
	
	@Test public void test_get_one_river() throws InterruptedException {
		Assert.assertNotNull(riverService);

		XContentBuilder xb = FSRiverHelper.toXContent(
				ESSearchProperties.INDEX_NAME, 
				ESSearchProperties.INDEX_TYPE_DOC, 
				new FSRiver("fs", "tmp", "/tmp_es", 30L));		
		
		client.prepareIndex("_river", "mytestriver", "_meta").setSource(xb)
				.execute().actionGet();
		
		// We have to wait for 1s
		Thread.sleep(1000);
		
		FSRiver fsriver = riverService.get("tmp");
		Assert.assertNotNull("Rivers should exist", fsriver);
	}

	@Test public void test_get_nonexisting_river() throws InterruptedException {
		Assert.assertNotNull(riverService);

		FSRiver fsriver = riverService.get("iamariverthatdoesntexist");
		Assert.assertNull("Rivers should not exist", fsriver);
	}

}

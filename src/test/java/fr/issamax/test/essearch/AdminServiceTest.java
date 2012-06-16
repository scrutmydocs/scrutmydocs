package fr.issamax.test.essearch;

import java.util.Collection;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.data.FSRiverHelper;
import fr.issamax.essearch.admin.river.service.AdminService;
import fr.issamax.essearch.constant.ESSearchProperties;

public class AdminServiceTest extends AbstractConfigurationTest {

	@Autowired AdminService adminService;
	
	@Test public void test_add_river() throws InterruptedException {
		Assert.assertNotNull(adminService);

		XContentBuilder xb = FSRiverHelper.toXContent(
				new FSRiver("mytestriver", ESSearchProperties.INDEX_NAME, 
						ESSearchProperties.INDEX_TYPE_DOC, "fs", "tmp", "/tmp_es", 30L, "standard", false));		
		
		client.prepareIndex(ESSearchProperties.ES_META_INDEX, ESSearchProperties.ES_META_RIVERS, "mytestriver").setSource(xb)
				.execute().actionGet();
		
		// We have to wait for 1s
		Thread.sleep(1000);
		
		Collection<FSRiver> rivers = adminService.get();
		Assert.assertEquals("Rivers should not be empty", 1, rivers.size());
	}
	
	@Test public void test_get_one_river() throws InterruptedException {
		Assert.assertNotNull(adminService);

		XContentBuilder xb = FSRiverHelper.toXContent(
				new FSRiver("mytestriver", ESSearchProperties.INDEX_NAME, 
						ESSearchProperties.INDEX_TYPE_DOC, "fs", "tmp", "/tmp_es", 30L, "standard", false));		
		
		client.prepareIndex(ESSearchProperties.ES_META_INDEX, ESSearchProperties.ES_META_RIVERS, "mytestriver").setSource(xb)
				.execute().actionGet();
		
		// We have to wait for 1s
		Thread.sleep(1000);
		
		FSRiver fsriver = adminService.get("mytestriver");
		Assert.assertNotNull("Rivers should exist", fsriver);
	}

	@Test public void test_get_nonexisting_river() throws InterruptedException {
		Assert.assertNotNull(adminService);

		FSRiver fsriver = adminService.get("iamariverthatdoesntexist");
		Assert.assertNull("Rivers should not exist", fsriver);
	}

}

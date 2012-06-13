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
	
	@Test public void test_add_river() {
		Assert.assertNotNull(riverService);

		XContentBuilder xb = FSRiverHelper.toXContent(new FSRiver("fs", "tmp", "/tmp_es", 30L));		
		
		client.prepareIndex("_river", ESSearchProperties.INDEX_NAME, "_meta").setSource(xb)
				.execute().actionGet();
		
		// When starting this test, we should not have any river
		Collection<FSRiver> rivers = riverService.get();
		Assert.assertEquals("Rivers should be empty when starting tests", 0, rivers.size());
	}
}

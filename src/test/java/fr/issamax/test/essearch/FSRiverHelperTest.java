package fr.issamax.test.essearch;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.junit.Assert;
import org.junit.Test;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.data.FSRiverHelper;
import fr.issamax.essearch.constant.ESSearchProperties;

public class FSRiverHelperTest {

	/**
	 * We want to check the parsing of <pre>
{
  "index" : {
	  "index" : "docs",
	  "type" : "doc"
  },
  "fs" : {
      "update_rate" : 30000,
      "name" : "tmp",
      "url" : "/tmp_es"
  },
  "type" : "fs"
}
</pre>
	 * @throws IOException 
	 */
	@Test public void test_tofsriver() throws IOException {
		FSRiver model = new FSRiver(ESSearchProperties.INDEX_NAME, 
				ESSearchProperties.INDEX_TYPE_DOC, "fs", "tmp", "/tmp_es", 30L);
		
		XContentBuilder xb = FSRiverHelper.toXContent(model);		
		String jsonContent = xb.string();

		Map<String, Object> map = XContentHelper.convertToMap(jsonContent.getBytes(), 0, jsonContent.length(), false).v2();
		
		FSRiver fsriver = FSRiverHelper.toFSRiver(map);
		
		Assert.assertEquals(model.getName(), fsriver.getName());
		Assert.assertEquals(model.getType(), fsriver.getType());
		Assert.assertEquals(model.getUrl(), fsriver.getUrl());
		Assert.assertEquals(model.getUpdateRate(), fsriver.getUpdateRate());

		Assert.assertEquals(model.getIndexname(), fsriver.getIndexname());
		Assert.assertEquals(model.getTypename(), fsriver.getTypename());

	}
}

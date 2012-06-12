package fr.issamax.essearch.admin.river.data;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;

public class FSRiverHelper {
	public static XContentBuilder toXContent(FSRiver fsriver) {
		XContentBuilder xb = null;
		try {
			xb = jsonBuilder()
					.startObject()
						.field("type", "fs")
						.startObject("fs")
							.startArray(fsriver.getType())
								.startObject()
									.field("name", fsriver.getName())
									.field("url", fsriver.getUrl())
									.field("update_rate", fsriver.getUpdateRate() * 1000)
								.endObject()
							.endArray()
						.endObject()
					.endObject();
		} catch (IOException e) {
			// TODO Log when error
		}		
		return xb;
	}
}

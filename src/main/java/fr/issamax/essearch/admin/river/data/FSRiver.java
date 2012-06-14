package fr.issamax.essearch.admin.river.data;


/**
 * Manage Filesystem Rivers metadata
 * @author PILATO
 *
 */
public class FSRiver extends AbstractRiver {
	private static final long serialVersionUID = 1L;
	
	private String url;
	private Long updateRate;
	
	public FSRiver() {
		this("/tmp", 60L);
	}
	
	/**
	 * @param url
	 * @param updateRate
	 */
	public FSRiver(String url, Long updateRate) {
		super();
		this.url = url;
		this.updateRate = updateRate;
	}


	/**
	 * @param indexname The ES index where we store our docs
	 * @param typename The ES type we use to store docs
	 * @param type The river type
	 * @param name The human readable name for this river
	 * @param url URL where to fetch content
	 * @param updateRate Update Rate (in seconds)
	 */
	public FSRiver(String indexname, String typename, String type, String name,
			String url, Long updateRate) {
		super(indexname, typename, type, name,true);
		this.url = url;
		this.updateRate = updateRate;
	}

	/**
	 * @return URL where to fetch content
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url URL where to fetch content
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return Update Rate (in seconds)
	 */
	public Long getUpdateRate() {
		return updateRate;
	}
	
	/**
	 * @param updateRate Update Rate (in seconds)
	 */
	public void setUpdateRate(Long updateRate) {
		this.updateRate = updateRate;
	}
}

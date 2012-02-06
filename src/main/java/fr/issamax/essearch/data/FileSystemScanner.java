package fr.issamax.essearch.data;

public class FileSystemScanner {
	private String url;
	
	/**
	 * For future use
	 */
	private String include;

	/**
	 * For future use
	 */
	private String exclude;

	public FileSystemScanner() {
	}
	
	public FileSystemScanner(String url) {
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the include
	 */
	public String getInclude() {
		return include;
	}

	/**
	 * @param include the include to set
	 */
	public void setInclude(String include) {
		this.include = include;
	}

	/**
	 * @return the exclude
	 */
	public String getExclude() {
		return exclude;
	}

	/**
	 * @param exclude the exclude to set
	 */
	public void setExclude(String exclude) {
		this.exclude = exclude;
	}


}

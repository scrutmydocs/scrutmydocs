package fr.issamax.essearch.admin.river.data;

import java.io.Serializable;

import fr.issamax.essearch.constant.ESSearchProperties;

/**
 * Manage Abstract Rivers metadata
 * @author PILATO
 *
 */
public abstract class AbstractRiver implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String indexname;
	private String typename;
	private String type;
	private boolean start;
	
	/**
	 * Default constructor using a dummy name and defaults to
	 * index/type : docs/doc
	 */
	public AbstractRiver() {
		this("dummy");
	}
	
	/**
	 * Default constructor using a dummy name and defaults to
	 * index/type : docs/doc
	 */
	public AbstractRiver(String id) {
		this(id, ESSearchProperties.INDEX_NAME, ESSearchProperties.INDEX_TYPE_DOC, "dummy", "My Dummy River", false);
	}
	
	/**
	 * @param id The unique id of this river
	 * @param indexname The ES index where we store our docs
	 * @param typename The ES type we use to store docs
	 * @param type The technical type of the river
	 * @param name The human readable name for this river
	 * @param start Started ?
	 */
	public AbstractRiver(String id, String indexname, String typename, String type, String name, boolean start) {
		this.id = id;
		this.indexname = indexname;
		this.typename = typename;
		this.type = type;
		this.name = name;
		this.start = start;
	}

	/**
	 * @return The human readable name for this river
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The human readable name for this river
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The ES index where we store our docs
	 */
	public String getIndexname() {
		return indexname;
	}

	/**
	 * @param indexname The ES index where we store our docs
	 */
	public void setIndexname(String indexname) {
		this.indexname = indexname;
	}

	/**
	 * @return The ES type we use to store docs
	 */
	public String getTypename() {
		return typename;
	}

	/**
	 * @param typename The ES type we use to store docs
	 */
	public void setTypename(String typename) {
		this.typename = typename;
	}

	/**
	 * @return The river type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The river type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @param start The river state
	 */
	public void setStart(boolean start) {
		this.start = start;
	}
	
	/**
	 * @param start The river state
	 */
	public boolean isStart() {
		return start;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	 
}

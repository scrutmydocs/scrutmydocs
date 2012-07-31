package org.scrutmydocs.webapp.configuration;

import org.scrutmydocs.webapp.util.StringTools;

import java.io.Serializable;

public class ScrutMyDocsProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean nodeEmbedded = true; 
	private String[] nodeAdresses = {"localhost:9300", "localhost:9301"}; 
	private String clusterName = "scrutmydocs"; 
	private String pathData = "~/.scrutmydocs/esdata";
	private String dropboxKey = null;
    private String dropboxSecret = null;

	/**
	 * @return the nodeEmbedded
	 */
	public boolean isNodeEmbedded() {
		return nodeEmbedded;
	}
	/**
	 * @param nodeEmbedded the nodeEmbedded to set
	 */
	public void setNodeEmbedded(boolean nodeEmbedded) {
		this.nodeEmbedded = nodeEmbedded;
	}
	/**
	 * @return the nodeAdresses
	 */
	public String[] getNodeAdresses() {
		return nodeAdresses;
	}
	/**
	 * @param nodeAdresses the nodeAdresses to set
	 */
	public void setNodeAdresses(String[] nodeAdresses) {
		this.nodeAdresses = nodeAdresses;
	}
	/**
	 * @return the clusterName
	 */
	public String getClusterName() {
		return clusterName;
	}
	/**
	 * @param clusterName the clusterName to set
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	/**
	 * @return the pathData
	 */
	public String getPathData() {
		return pathData;
	}
	/**
	 * @param pathData the pathData to set
	 */
	public void setPathData(String pathData) {
		this.pathData = pathData;
	}

    /**
     * @return Dropbox Application Key
     */
    public String getDropboxKey() {
        return dropboxKey;
    }

    /**
     * @param dropboxKey Dropbox Application Key
     */
    public void setDropboxKey(String dropboxKey) {
        this.dropboxKey = dropboxKey;
    }

    /**
     *
     * @return Dropbox Application Secret
     */
    public String getDropboxSecret() {
        return dropboxSecret;
    }

    /**
     *
     * @param dropboxSecret Dropbox Application Secret
     */
    public void setDropboxSecret(String dropboxSecret) {
        this.dropboxSecret = dropboxSecret;
    }

    @Override
	public String toString() {
		return StringTools.toString(this);
	}
	
}

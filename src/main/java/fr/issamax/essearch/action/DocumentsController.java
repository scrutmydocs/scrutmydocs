package fr.issamax.essearch.action;

import static fr.issamax.essearch.constant.ESSearchProperties.DIR_FIELD_NAME;
import static fr.issamax.essearch.constant.ESSearchProperties.DIR_FIELD_VIRTUAL_PATH;
import static fr.issamax.essearch.constant.ESSearchProperties.DOC_FIELD_NAME;
import static fr.issamax.essearch.constant.ESSearchProperties.DOC_FIELD_VIRTUAL_PATH;
import static fr.issamax.essearch.constant.ESSearchProperties.INDEX_NAME;
import static fr.issamax.essearch.constant.ESSearchProperties.INDEX_TYPE_FOLDER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.data.Result;

@Component("documentsController")
@Scope("session")
public class DocumentsController implements Serializable {

	private static final long serialVersionUID = 8765323975883945040L;

	@Autowired
	protected Client esClient;

	private TreeNode root;

	public DocumentsController() throws Exception {

		root = new DefaultTreeNode("root", null);

		TreeNode node1 = new DefaultTreeNode(new Result("/","/"), root);
		new DefaultTreeNode(new Result("dummy node","dummy path"), node1);

	}

	public void onNodeExpand(NodeExpandEvent event) throws Exception {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Expanded", event.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);

		addChilds(event.getTreeNode());
	}

	public void onNodeCollapse(NodeCollapseEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Collapsed", event.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onNodeSelect(NodeSelectEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Selected", event.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onNodeUnselect(NodeUnselectEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Unselected", event.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addChilds(TreeNode treeNode) throws Exception {
		
		treeNode.getChildren().clear();
		
		
		SearchResponse response = esClient
				.prepareSearch(INDEX_NAME)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(
						QueryBuilders.termQuery(DOC_FIELD_VIRTUAL_PATH,
								((Result) treeNode.getData()).getVirtualPath()))
				.setFrom(0).setSize(50000).execute().actionGet();

		if (response.getHits() != null && response.getHits().getHits() != null) {
			for (SearchHit hit : response.getHits().getHits()) {
				buildTreeNode(treeNode, hit);
			}
		}

	}

	/**
	 * Build a new TreeNode based on SearchHit
	 * @param treenode The treenode where we want to add new nodes
	 * @param hit The search hit
	 */
	private void buildTreeNode(TreeNode treenode, SearchHit hit) {
		String name = hit.getSource().get(DOC_FIELD_NAME).toString();
		String virtualPath = hit.getSource().get(DOC_FIELD_VIRTUAL_PATH).toString();
		TreeNode node1 = new DefaultTreeNode(new Result(name,virtualPath+name), treenode);
		
		// If we have a folder, we must add a dummy folder to simulate it
		if (INDEX_TYPE_FOLDER.equals(hit.getType())){
			new DefaultTreeNode(new Result("dummy node","dummy path"), node1);
		}
	}
	
	public Collection<String> getFolderDirectory(String path) throws Exception {
		Collection<String> files = new ArrayList<String>();

		SearchResponse response = esClient
				.prepareSearch(INDEX_NAME)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setTypes(INDEX_TYPE_FOLDER)
				.setQuery(QueryBuilders.termQuery(DIR_FIELD_VIRTUAL_PATH, path))
				.setFrom(0).setSize(50000).execute().actionGet();

		if (response.getHits() != null && response.getHits().getHits() != null) {
			for (SearchHit hit : response.getHits().getHits()) {
				String name = hit.getSource().get(DIR_FIELD_NAME).toString();
				files.add(name);
			}
		}

		return files;

	}

	public TreeNode getRoot() {
		return root;
	}
}

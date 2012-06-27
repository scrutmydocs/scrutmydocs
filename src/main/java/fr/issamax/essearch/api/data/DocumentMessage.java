package fr.issamax.essearch.api.data;

import java.io.Serializable;

public class DocumentMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private boolean ok;
	private String erreurs[];

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String[] getErreurs() {
		return erreurs;
	}

	public void setErreurs(String[] erreurs) {
		this.erreurs = erreurs;
	}

}

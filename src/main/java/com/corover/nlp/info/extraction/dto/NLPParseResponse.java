package com.corover.nlp.info.extraction.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NLPParseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3487256564829702076L;

	public NLPParseResponse() {
		compounds = new ArrayList<>();
	}

	private String intent;
	private String entity;

	private String intentParent;
	private String entityChild;

	private List<String> compounds;

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getEntityChild() {
		return entityChild;
	}

	public void setEntityChild(String entityChild) {
		this.entityChild = entityChild;
	}

	public String getIntentParent() {
		return intentParent;
	}

	public void setIntentParent(String intentParent) {
		this.intentParent = intentParent;
	}

	public List<String> getCompounds() {
		return compounds;
	}

	public void addCompounds(String source, String target) {
		this.compounds.add(source + ":" + target);
	}
}

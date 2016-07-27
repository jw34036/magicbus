package org.light32.projects.magicbus.model;

import java.io.Serializable;

/**
 * 
 *
 * @author jwhitt
 */
public class MBMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final long timestamp;
	private String requestMethod;
	private String id;
	private Object body;
	
	public MBMessage() { 
		this.timestamp = System.currentTimeMillis();
	}
	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	/**
	 * @return the requestMethod
	 */
	public String getRequestMethod() {
		return this.requestMethod;
	}
	/**
	 * @param requestMethod the requestMethod to set
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the body
	 */
	public Object getBody() {
		return this.body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(Object body) {
		this.body = body;
	}
}

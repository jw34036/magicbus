package org.light32.projects.magicbus.model;

import java.io.Serializable;

/**
 * status output message for the bus
 *
 * @author jwhitt
 */
public class MBStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String statusCode;
	private final String statusMessage;
	
	public MBStatus(String string) {
		this.statusCode = string;
		this.statusMessage = null;
	}
	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return this.statusCode;
	}
	
	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return this.statusMessage;
	}
	
}

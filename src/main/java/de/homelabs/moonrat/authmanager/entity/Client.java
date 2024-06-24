package de.homelabs.moonrat.authmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "authentication", name = "clients")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String clientId;
	private byte[] clientKey = new byte[32];
	private String clientDescription;
	private int clientType;
	//private int[] scope;
	private boolean clientAllowed = false;
	
	
	public Client() {}
	
	public Client(String clientId, byte[] clientKey, String clientDescription, int clientType, boolean clientAllowed) {
		super();
		this.clientId = clientId;
		this.clientKey = clientKey;
		this.clientDescription = clientDescription;
		this.clientType = clientType;
		this.clientAllowed = clientAllowed;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}
	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	/**
	 * @return the clientKey
	 */
	public byte[] getClientKey() {
		return clientKey;
	}
	/**
	 * @param clientKey the clientKey to set
	 */
	public void setClientKey(byte[] clientKey) {
		this.clientKey = clientKey;
	}
	/**
	 * @return the clientDescription
	 */
	public String getClientDescription() {
		return clientDescription;
	}
	/**
	 * @param clientDescription the clientDescription to set
	 */
	public void setClientDescription(String clientDescription) {
		this.clientDescription = clientDescription;
	}
	/**
	 * @return the clientType
	 */
	public int getClientType() {
		return clientType;
	}
	/**
	 * @param clientType the clientType to set
	 */
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}
	/**
	 * @return the clientAllowed
	 */
	public boolean isClientAllowed() {
		return clientAllowed;
	}
	/**
	 * @param clientAllowed the clientAllowed to set
	 */
	public void setClientAllowed(boolean clientAllowed) {
		this.clientAllowed = clientAllowed;
	}	
}

package model;

/**
 * @author ypandya
 *
 */
public class Administrator {
	
	/**
	 * This is admin model class to store the admin data
	 */
	public String userName;
	public String password;
	public String ipAddress;
	
	/**
	 * Administrator constructor to set admin data 
	 * @param username
	 * @param password
	 * @param ipAddress
	 */
	public Administrator(String username, String password, String ipAddress){
		this.userName = username;
		this.password = password;
		this.ipAddress = ipAddress;
	}

	/**
	 * This method is used to get the username
	 * @return String username of the admin
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * This method is used to set username
	 * @param userName username of the admin
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * This method is used to get the password
	 * @return String password of the admin
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method is used to set password
	 * @param password password of the admin
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This method is used to get the ip
	 * @return String ip of the admin
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * This method is used to set ipAddress
	 * @param ipAddress ip of the admin
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
}
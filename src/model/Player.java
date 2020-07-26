package model;

/**
 * @author ypandya
 *
 */
public class Player {
	
	/**
	 * This is player model class to store the player data
	 */
	public String firstName;
	public String lastName;
	public String age;
	public String userName;
	public String password;
	public String ipAddress;
	public boolean status;
	
	/**
	 * Player constructor to set admin data 
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param userName
	 * @param password
	 * @param ipAddress
	 */
	public Player(String firstName, String lastName, String age, String userName, String password, String ipAddress){
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.userName = userName;
		this.password = password;
		this.ipAddress = ipAddress;
		this.status = false;	
	}
	
	/**
	 * This method is used to get the firstName
	 * @return String firstName of the player
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * This method is used to set firstName
	 * @param firstName firstName of the player
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * This method is used to get the lastName
	 * @return String lastName of the player
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * This method is used to set lastName
	 * @param lastName lastName of the player
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * This method is used to get the userName
	 * @return String userName of the player
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * This method is used to set userName
	 * @param userName userName of the player
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * This method is used to get the password
	 * @return String password of the player
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method is used to set password
	 * @param password password of the player
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This method is used to get the ipAddress
	 * @return String ipAddress of the player
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * This method is used to set ipAddress
	 * @param ipAddress ipAddress of the player
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * This method is used to get the status
	 * @return boolean status of the player
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * This method is used to set status
	 * @param status status of the player
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
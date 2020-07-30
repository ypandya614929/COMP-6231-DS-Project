package RM1Server;

//References:
/**
 *
 * @author ypandya
 */
public class RM1GameServer {
	
	/**
	 * This is the RM1GameServer class
	 */
	
	public RM1GameServer() {
		
		try {
			
			new RM1EUServer();
			new RM1ASServer();
			new RM1NAServer();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
}
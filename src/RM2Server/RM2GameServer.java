package RM2Server;

//References:
/**
 *
 * @author ypandya
 */
public class RM2GameServer {
	
	/**
	 * This is the RM1GameServer class
	 */
	
	public RM2GameServer() {
		
		try {
            
            new RM2EUServer();
			new RM2ASServer();
			new RM2NAServer();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
}
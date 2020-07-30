package Runserver;
import RM1Server.RM1GameServer;
import RM2Server.RM2GameServer;
import RM3Server.RM3GameServer;

/**
 * 
 */

/**
 * @author ypandya
 *
 */
public class GameServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
            
            new RM1GameServer();
			new RM2GameServer();
			new RM3GameServer();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}

}

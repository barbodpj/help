package controller.exception.gameException;

//TODO mvc
public class GameException extends Exception{
	protected String message;

	public void printAnnouncement() {
		System.out.println(message);
	}

}
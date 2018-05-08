package controller.exception.gameException;

public class GameException extends Exception{

	protected String message;

	public String printAnnouncement() {
		return message;
	}

}
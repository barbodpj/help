package controller.exception.gameException;

public class UnavailableTroopException extends GameException {

	public UnavailableTroopException() {
		super.message = "You can’t build this soldier.";
	}

}
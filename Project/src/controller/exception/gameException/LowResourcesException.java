package controller.exception.gameException;

public class LowResourcesException extends GameException {

	public LowResourcesException() {
		super.message = "You don’t have enough resources.";
	}

}
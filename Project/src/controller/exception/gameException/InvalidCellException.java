package controller.exception.gameException;

public class InvalidCellException extends GameException {

	public InvalidCellException() {
		super.message = "You can’t build this building here.Please choose another cell.";
	}

}
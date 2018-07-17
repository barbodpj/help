package controller.exception.gameException;

public class InvalidAttackCellException extends GameException {
    public InvalidAttackCellException() {
        super.message = "You can’t put this soldier here.";
    }
}

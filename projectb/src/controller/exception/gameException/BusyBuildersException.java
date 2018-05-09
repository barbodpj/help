package controller.exception.gameException;

public class BusyBuildersException extends GameException {
    public BusyBuildersException() {
        super.message = "You don’t have any worker to build this building.";
    }
}

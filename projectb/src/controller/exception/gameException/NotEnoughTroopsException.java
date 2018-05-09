package controller.exception.gameException;

import constants.enums.TroopType;

public class NotEnoughTroopsException extends GameException {
    private TroopType type;
    public NotEnoughTroopsException(TroopType type) {
        super.message = "Not enough " + type.getValue() + " in camps";
    }
}

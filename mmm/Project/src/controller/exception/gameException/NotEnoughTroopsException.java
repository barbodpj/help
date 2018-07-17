package controller.exception.gameException;

import constants.enums.TroopType;

public class NotEnoughTroopsException extends GameException {
    private TroopType type;
    private int amount;
    public NotEnoughTroopsException(TroopType type, int amount) {
        super.message = "Not enough " + type.getValue() + " in camps";
        this.amount = amount;
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public TroopType getType() {
        return type;
    }
}

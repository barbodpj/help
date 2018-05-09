package comparator;

import constants.enums.TroopType;

import java.util.Comparator;

public class TroopTypeComparator implements Comparator <TroopType>{


    @Override
    public int compare(TroopType o1, TroopType o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}

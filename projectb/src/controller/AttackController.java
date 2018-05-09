package controller;

import constants.enums.TroopType;
import model.Cost;
import model.Village;
import model.map.Map;
import model.troop.Troop;

import java.util.ArrayList;
import java.util.HashMap;

public class AttackController { //TODO atrin zenaro bega

    private Cost moneyGained ;

    private HashMap<TroopType , ArrayList<Troop> > selectedTroops = new HashMap <TroopType , ArrayList<Troop>>();

    private HashMap<TroopType , ArrayList<Troop> > putTroops = new HashMap <TroopType , ArrayList<Troop>>();

    private Village attackerVillage;

    private Village defenderVillage;

    public AttackController( Village attackerVillage , Village defenderVillage )
    {
        this.attackerVillage = attackerVillage;
        this.defenderVillage = defenderVillage;
    }

    public boolean isAttackFinished()
    {
        if ( attackerVillage.getGoldStorageCapacity() - attackerVillage.getAvailableGold() == 0 &&
                attackerVillage.getElixirStorageCapacity() - attackerVillage.getAvailableGold() == 0 || this.putTroops.size() == 0
                || this.defenderVillage.getAvailableGold() == moneyGained.getGold() &&
                this.defenderVillage.getAvailableElixir() == moneyGained.getElixir())
        {
            return true;
        }else
        {
            return false;
        }


    }

}
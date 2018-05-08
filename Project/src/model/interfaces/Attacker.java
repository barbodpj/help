package model.interfaces;

import model.Location;

public interface Attacker {

	int getAttackRange();

	int getDamage();

	void startAttack(Location startLocation);



}
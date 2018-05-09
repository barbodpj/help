package controller;

import constants.enums.*;

public class MenuController {

	private Section currentSection = Section.Village;
	private Integer buildingNumber = null;
	private BuildingType buildingType = null;

	/**
	 *
	 * @param section
	 */
	public String changeSection(Section section) {
		currentSection = section;
		return section.getMenu();
	}

	public String back() {
		Section parentSection = currentSection.getParent();

		if(parentSection == Section.Village && currentSection != Section.Attack) {
			buildingNumber = null;
			buildingType = null;
		}

		currentSection = parentSection;

		if(currentSection == Section.Village) {
			return "You have entered village";
		}
		else {
			return currentSection.getMenu();
		}
	}

	public String showMenu() {
		if(currentSection == Section.Village) {
			return "You are in village";
		}
		else {
			return currentSection.getMenu();
		}
	}

	public BuildingType getBuildingType() {
		return buildingType;
	}

	public Section getCurrentSection() {
		return this.currentSection;
	}

	public Integer getBuildingNumber() {
		return this.buildingNumber;
	}

	public void setBuildingNumber(Integer buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
	}

}
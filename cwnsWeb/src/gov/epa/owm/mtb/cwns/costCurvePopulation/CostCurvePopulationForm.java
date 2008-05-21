package gov.epa.owm.mtb.cwns.costCurvePopulation;

import org.apache.struts.action.ActionForm;

public class CostCurvePopulationForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	public static final long INITIAL_LONG_VALUE = -999;
	public static final int INITIAL_INT_VALUE = 0;
	
	private String costCurvePopulationAct = "";

	private String isUpdatable = "Y";
	
	private long costCurvePopulationFacilityId = INITIAL_LONG_VALUE;

	private int newSeparateSewerSystemPopulation = INITIAL_INT_VALUE;
	private int rehabReplaceSeparateSewerSystemPopulation  = INITIAL_INT_VALUE;
			
	private int newOWTSAllResidentHouses  = INITIAL_INT_VALUE;
	private int newOWTSAllNonResidentHouses  = INITIAL_INT_VALUE;
	private int newOWTSInnovResidentHouses  = INITIAL_INT_VALUE;
	private int newOWTSInnovNonResidentHouses  = INITIAL_INT_VALUE;
	private int newOWTSConvenResidentHouses  = INITIAL_INT_VALUE;
	private int newOWTSConvenNonResidentHouses  = INITIAL_INT_VALUE;
	private int newClusteredSystemResidentHouses  = INITIAL_INT_VALUE;
	private int newClusteredSystemNonResidentHouses  = INITIAL_INT_VALUE;
	private int rehabOWTSAllResidentHouses  = INITIAL_INT_VALUE;
	private int rehabOWTSAllNonResidentHouses  = INITIAL_INT_VALUE;
	private int rehabOWTSInnovResidentHouses  = INITIAL_INT_VALUE;
	private int rehabOWTSInnovNonResidentHouses  = INITIAL_INT_VALUE;
	private int rehabOWTSConvenResidentHouses  = INITIAL_INT_VALUE;
	private int rehabOWTSConvenNonResidentHouses  = INITIAL_INT_VALUE;
	private int rehabClusteredSystemResidentHouses  = INITIAL_INT_VALUE;
	private int rehabClusteredSystemNonResidentHouses  = INITIAL_INT_VALUE;
	
    private float resClusteredPopulationPerHouse;
	private float nonResClusteredPopulationPerHouse;
	private float resOWTSPopulationPerHouse;
	private float nonResOWTSPopulationPerHouse;
	
	private double populationPerHouseResidential = 2.8;
	private double populationPerHouseNonResidential = 2.8;
	private String populationPerHouseDisplayOnly = "N";
	
	private String showMessageZone = "N";
	
	public String getShowMessageZone() {
		return showMessageZone;
	}
	public void setShowMessageZone(String showMessageZone) {
		this.showMessageZone = showMessageZone;
	}
	public String getCostCurvePopulationAct() {
		return costCurvePopulationAct;
	}
	public void setCostCurvePopulationAct(String costCurvePopulationAct) {
		this.costCurvePopulationAct = costCurvePopulationAct;
	}
	public long getCostCurvePopulationFacilityId() {
		return costCurvePopulationFacilityId;
	}
	public void setCostCurvePopulationFacilityId(long costCurvePopulationFacilityId) {
		this.costCurvePopulationFacilityId = costCurvePopulationFacilityId;
	}
	public String getIsUpdatable() {
		return isUpdatable;
	}
	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}
	public int getNewClusteredSystemNonResidentHouses() {
		return newClusteredSystemNonResidentHouses;
	}
	public void setNewClusteredSystemNonResidentHouses(
			int newClusteredSystemNonResidentHouses) {
		this.newClusteredSystemNonResidentHouses = newClusteredSystemNonResidentHouses;
	}
	public int getNewClusteredSystemResidentHouses() {
		return newClusteredSystemResidentHouses;
	}
	public void setNewClusteredSystemResidentHouses(
			int newClusteredSystemResidentHouses) {
		this.newClusteredSystemResidentHouses = newClusteredSystemResidentHouses;
	}
	public int getNewOWTSAllNonResidentHouses() {
		return newOWTSAllNonResidentHouses;
	}
	public void setNewOWTSAllNonResidentHouses(int newOWTSAllNonResidentHouses) {
		this.newOWTSAllNonResidentHouses = newOWTSAllNonResidentHouses;
	}
	public int getNewOWTSAllResidentHouses() {
		return newOWTSAllResidentHouses;
	}
	public void setNewOWTSAllResidentHouses(int newOWTSAllResidentHouses) {
		this.newOWTSAllResidentHouses = newOWTSAllResidentHouses;
	}
	public int getNewOWTSConvenNonResidentHouses() {
		return newOWTSConvenNonResidentHouses;
	}
	public void setNewOWTSConvenNonResidentHouses(int newOWTSConvenNonResidentHouses) {
		this.newOWTSConvenNonResidentHouses = newOWTSConvenNonResidentHouses;
	}
	public int getNewOWTSConvenResidentHouses() {
		return newOWTSConvenResidentHouses;
	}
	public void setNewOWTSConvenResidentHouses(int newOWTSConvenResidentHouses) {
		this.newOWTSConvenResidentHouses = newOWTSConvenResidentHouses;
	}
	public int getNewOWTSInnovNonResidentHouses() {
		return newOWTSInnovNonResidentHouses;
	}
	public void setNewOWTSInnovNonResidentHouses(int newOWTSInnovNonResidentHouses) {
		this.newOWTSInnovNonResidentHouses = newOWTSInnovNonResidentHouses;
	}
	public int getNewOWTSInnovResidentHouses() {
		return newOWTSInnovResidentHouses;
	}
	public void setNewOWTSInnovResidentHouses(int newOWTSInnovResidentHouses) {
		this.newOWTSInnovResidentHouses = newOWTSInnovResidentHouses;
	}
	public int getNewSeparateSewerSystemPopulation() {
		return newSeparateSewerSystemPopulation;
	}
	public void setNewSeparateSewerSystemPopulation(
			int newSeparateSewerSystemPopulation) {
		this.newSeparateSewerSystemPopulation = newSeparateSewerSystemPopulation;
	}
	public String getPopulationPerHouseDisplayOnly() {
		return populationPerHouseDisplayOnly;
	}
	public void setPopulationPerHouseDisplayOnly(
			String populationPerHouseDisplayOnly) {
		this.populationPerHouseDisplayOnly = populationPerHouseDisplayOnly;
	}
	public int getRehabClusteredSystemNonResidentHouses() {
		return rehabClusteredSystemNonResidentHouses;
	}
	public double getPopulationPerHouseNonResidential() {
		return populationPerHouseNonResidential;
	}
	public void setPopulationPerHouseNonResidential(
			double populationPerHouseNonResidential) {
		this.populationPerHouseNonResidential = populationPerHouseNonResidential;
	}
	public double getPopulationPerHouseResidential() {
		return populationPerHouseResidential;
	}
	public void setPopulationPerHouseResidential(
			double populationPerHouseResidential) {
		this.populationPerHouseResidential = populationPerHouseResidential;
	}
	public void setRehabClusteredSystemNonResidentHouses(
			int rehabClusteredSystemNonResidentHouses) {
		this.rehabClusteredSystemNonResidentHouses = rehabClusteredSystemNonResidentHouses;
	}
	public int getRehabClusteredSystemResidentHouses() {
		return rehabClusteredSystemResidentHouses;
	}
	public void setRehabClusteredSystemResidentHouses(
			int rehabClusteredSystemResidentHouses) {
		this.rehabClusteredSystemResidentHouses = rehabClusteredSystemResidentHouses;
	}
	public int getRehabOWTSAllNonResidentHouses() {
		return rehabOWTSAllNonResidentHouses;
	}
	public void setRehabOWTSAllNonResidentHouses(int rehabOWTSAllNonResidentHouses) {
		this.rehabOWTSAllNonResidentHouses = rehabOWTSAllNonResidentHouses;
	}
	public int getRehabOWTSAllResidentHouses() {
		return rehabOWTSAllResidentHouses;
	}
	public void setRehabOWTSAllResidentHouses(int rehabOWTSAllResidentHouses) {
		this.rehabOWTSAllResidentHouses = rehabOWTSAllResidentHouses;
	}
	public int getRehabOWTSConvenNonResidentHouses() {
		return rehabOWTSConvenNonResidentHouses;
	}
	public void setRehabOWTSConvenNonResidentHouses(
			int rehabOWTSConvenNonResidentHouses) {
		this.rehabOWTSConvenNonResidentHouses = rehabOWTSConvenNonResidentHouses;
	}
	public int getRehabOWTSConvenResidentHouses() {
		return rehabOWTSConvenResidentHouses;
	}
	public void setRehabOWTSConvenResidentHouses(int rehabOWTSConvenResidentHouses) {
		this.rehabOWTSConvenResidentHouses = rehabOWTSConvenResidentHouses;
	}
	public int getRehabOWTSInnovNonResidentHouses() {
		return rehabOWTSInnovNonResidentHouses;
	}
	public void setRehabOWTSInnovNonResidentHouses(
			int rehabOWTSInnovNonResidentHouses) {
		this.rehabOWTSInnovNonResidentHouses = rehabOWTSInnovNonResidentHouses;
	}
	public int getRehabOWTSInnovResidentHouses() {
		return rehabOWTSInnovResidentHouses;
	}
	public void setRehabOWTSInnovResidentHouses(int rehabOWTSInnovResidentHouses) {
		this.rehabOWTSInnovResidentHouses = rehabOWTSInnovResidentHouses;
	}
	public int getRehabReplaceSeparateSewerSystemPopulation() {
		return rehabReplaceSeparateSewerSystemPopulation;
	}
	public void setRehabReplaceSeparateSewerSystemPopulation(
			int rehabReplaceSeparateSewerSystemPopulation) {
		this.rehabReplaceSeparateSewerSystemPopulation = rehabReplaceSeparateSewerSystemPopulation;
	}
	public float getNonResClusteredPopulationPerHouse() {
		return nonResClusteredPopulationPerHouse;
	}
	public void setNonResClusteredPopulationPerHouse(
			float nonResClusteredPopulationPerHouse) {
		this.nonResClusteredPopulationPerHouse = nonResClusteredPopulationPerHouse;
	}
	public float getNonResOWTSPopulationPerHouse() {
		return nonResOWTSPopulationPerHouse;
	}
	public void setNonResOWTSPopulationPerHouse(float nonResOWTSPopulationPerHouse) {
		this.nonResOWTSPopulationPerHouse = nonResOWTSPopulationPerHouse;
	}
	public float getResClusteredPopulationPerHouse() {
		return resClusteredPopulationPerHouse;
	}
	public void setResClusteredPopulationPerHouse(
			float resClusteredPopulationPerHouse) {
		this.resClusteredPopulationPerHouse = resClusteredPopulationPerHouse;
	}
	public float getResOWTSPopulationPerHouse() {
		return resOWTSPopulationPerHouse;
	}
	public void setResOWTSPopulationPerHouse(float resOWTSPopulationPerHouse) {
		this.resOWTSPopulationPerHouse = resOWTSPopulationPerHouse;
	}
	
	
}

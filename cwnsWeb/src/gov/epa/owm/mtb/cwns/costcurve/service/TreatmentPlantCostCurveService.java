package gov.epa.owm.mtb.cwns.costcurve.service;

/**
 * Algorythm ONLY Input Data 
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CountyRef;
import gov.epa.owm.mtb.cwns.model.TreatmentPlantCcFormulaRef;

public class TreatmentPlantCostCurveService {

	public static final char PLANT_TYPE_MECHANICAL = 'M';

	public static final char PLANT_TYPE_LOGOON = 'L';

	public static final String CURVE_TYPE_CD_ONE = "1";

	public static final String CURVE_TYPE_CD_ONE_A = "1A";

	public static final String CURVE_TYPE_CD_TWO = "2";

	public static final String CURVE_TYPE_CD_TWO_A = "2A";

	public static final String CURVE_TYPE_CD_THREE = "3";

	public static final String CURVE_TYPE_CD_THREE_A = "3A";

	public static final String CURVE_TYPE_CD_FOUR = "4";

	public static final String CURVE_TYPE_CD_FIVE = "5";

	public static final String CURVE_TYPE_CD_SIX = "6";

	public static final String CURVE_TYPE_CD_S_A = "SA";

	public static final String CURVE_TYPE_CD_S_B = "SB";

	public static final int EFFLUENT_TREATMENT_LEVEL_PRIMARY = 20;

	public static final int EFFLUENT_TREATMENT_LEVEL_ADVANCED_PRIMARY = 30;

	public static final int EFFLUENT_TREATMENT_LEVEL_SECONDARY = 40;

	public static final int EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT = 50;

	// OTHER_ADVANCED_TREATMENT Number will exclude Advanced Treatment of
	// ‘Nutrient Removal’ (id = 10)
	public static final int EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT = 501;

	public static final int EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT = 502;

	public static final int EFFLUENT_ADVANCED_NO_BOD_AND_NO_OTHER_ADVANCED_TREATMENT = 503;

	public static final int EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT = 504;

	public static final int EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT = 505;

	public static final int EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT = 506;

	/**
	 * If user indicates a mechanical plant, get coefficients as per below and
	 * future design flow facility projected effluent | advanced treatment |
	 * treatment_currve_type | cat_I_split_pct | cat_II_split_pct
	 * 
	 * If user indicates a lagoon plant, get coefficients as per below and
	 * future design flow facility projected effluent | nutrient removal |
	 * treatment_curve_type | cat_I_split_pct | cat_II_split_pct SECONDARY n/a 4
	 * 100 0 ADVANCED TREATMENT n/a 4 86 14
	 * 
	 * @param projectedPlantype
	 * @param projectedFacilityEffluenttype
	 * @param isNutrientRemoval
	 * @return
	 */
	public String getTreatmentCurveType(char projectedPlantype,
			int projectedFacilityEffluenttype,
			int projectedFacilityEffluenTypeAdvancedTreatmentSubtype) {
		if (projectedPlantype == PLANT_TYPE_MECHANICAL) {
			if (projectedFacilityEffluenttype == EFFLUENT_TREATMENT_LEVEL_SECONDARY) {
				return CURVE_TYPE_CD_ONE;
			}
			if (projectedFacilityEffluenttype == EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT) {

				if (projectedFacilityEffluenTypeAdvancedTreatmentSubtype == EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT
						|| projectedFacilityEffluenTypeAdvancedTreatmentSubtype == EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT) {
					return CURVE_TYPE_CD_ONE_A;
				} else if (projectedFacilityEffluenTypeAdvancedTreatmentSubtype == EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT) {
					return CURVE_TYPE_CD_TWO;
				} else if (projectedFacilityEffluenTypeAdvancedTreatmentSubtype == EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT
						|| projectedFacilityEffluenTypeAdvancedTreatmentSubtype == EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT) {
					return CURVE_TYPE_CD_TWO_A;
				} else {
					return CURVE_TYPE_CD_ONE;
				}
			} else {
				return CURVE_TYPE_CD_ONE;
			}
		} else if (projectedPlantype == PLANT_TYPE_LOGOON) {
			return CURVE_TYPE_CD_FOUR;
		} else
			return CURVE_TYPE_CD_ONE;
	}

	public int getCatISplitPercentage(char projectedPlantype,
			int projectedFacilityEffluenttype,
			int projectedFacilityEffluenTypeAdvancedTreatmentSubtype) {
		if (projectedPlantype == PLANT_TYPE_MECHANICAL) {
			if (projectedFacilityEffluenttype == EFFLUENT_TREATMENT_LEVEL_SECONDARY) {
				return 100;
			}
			if (projectedFacilityEffluenttype == EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT) {
				if (projectedFacilityEffluenTypeAdvancedTreatmentSubtype == EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT) {
					return 83;
				} else if (projectedFacilityEffluenTypeAdvancedTreatmentSubtype == EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT) {
					return 86;
				} else if (projectedFacilityEffluenTypeAdvancedTreatmentSubtype == EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT) {
					return 72;
				} else {
					return 100;
				}
			} else {
				return 100;
			}
		} else if (projectedPlantype == PLANT_TYPE_LOGOON) {
			if (projectedFacilityEffluenttype == EFFLUENT_TREATMENT_LEVEL_SECONDARY)
				return 100;
			else if (projectedFacilityEffluenttype == EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT) {
				return 86;
			} else
				return 100;
		} else
			return 100;
	}

	public String getFutureTreatmentCurveType(int presentEffluentType,
			int presentEffluentTypeAdvancedTreatmentSubtype,
			int projectedEffluentType,
			int projectedEffluentTypeTypeAdvancedTreatmentSubtype)
			throws CostCurveException {
		String cureveType = "";
		switch (presentEffluentType) {
		case EFFLUENT_TREATMENT_LEVEL_PRIMARY:
		case EFFLUENT_TREATMENT_LEVEL_ADVANCED_PRIMARY:
			switch (projectedEffluentType) {
			case EFFLUENT_TREATMENT_LEVEL_SECONDARY:
				cureveType = CURVE_TYPE_CD_ONE;
				break;
			case EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE_A;
					break;
				case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO_A;
					break;
				}
			default:
				throw new CostCurveException(
						"Cannot use cost curves to upgrade this treatment plant");
			}
			break;
		case EFFLUENT_TREATMENT_LEVEL_SECONDARY:
			switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
			case EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_ONE_A;
				break;
			case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_ONE_A;
				break;
			case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_TWO;
				break;
			case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_TWO_A;
				break;
			case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_TWO_A;
				break;
			default:
				throw new CostCurveException(
						"Cannot use cost curves to upgrade this treatment plant");
			}
			break;
		case EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT:
			switch (presentEffluentTypeAdvancedTreatmentSubtype) {
			case EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO_A;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;

			case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO_A;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;

			case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO_A;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;

			case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO_A;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;
			}
		}
		return cureveType;
	}

	public String getSalvageTreatmentCurveType(int presentEffluentType,
			int presentEffluentTypeAdvancedTreatmentSubtype,
			int projectedEffluentType,
			int projectedEffluentTypeTypeAdvancedTreatmentSubtype)
			throws CostCurveException {
		String cureveType = "";
		switch (presentEffluentType) {
		case EFFLUENT_TREATMENT_LEVEL_PRIMARY:
		case EFFLUENT_TREATMENT_LEVEL_ADVANCED_PRIMARY:
			switch (projectedEffluentType) {
			case EFFLUENT_TREATMENT_LEVEL_SECONDARY:
				cureveType = CURVE_TYPE_CD_S_B;
				break;
			case EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_S_B;
					break;
				case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_S_B;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_S_B;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_S_B;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_S_B;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
			}
			break;
		case EFFLUENT_TREATMENT_LEVEL_SECONDARY:
			switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
			case EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_ONE;
				break;
			case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_ONE;
				break;
			case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_ONE;
				break;
			case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_ONE;
				break;
			case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				cureveType = CURVE_TYPE_CD_ONE;
				break;
			default:
				throw new CostCurveException(
						"Cannot use cost curves to upgrade this treatment plant");
			}
			break;
		case EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT:
			switch (presentEffluentTypeAdvancedTreatmentSubtype) {
			case EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE_A;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;

			case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE_A;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_ONE_A;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;

			case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;

			case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					cureveType = CURVE_TYPE_CD_TWO;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;
			}
		}
		return cureveType;
	}

	public int getUpgradeCatISplitPercentage(int presentEffluentType,
			int presentEffluentTypeAdvancedTreatmentSubtype,
			int projectedEffluentType,
			int projectedEffluentTypeTypeAdvancedTreatmentSubtype)
			throws CostCurveException {
		int catIPercentage = 100;
		switch (presentEffluentType) {
		case EFFLUENT_TREATMENT_LEVEL_PRIMARY:
		case EFFLUENT_TREATMENT_LEVEL_ADVANCED_PRIMARY:
			switch (projectedEffluentType) {
			case EFFLUENT_TREATMENT_LEVEL_SECONDARY:
				catIPercentage = 100;
				break;
			case EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					catIPercentage = 83;
					break;
				case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					catIPercentage = 83;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
					catIPercentage = 86;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
					catIPercentage = 72;
					break;
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					catIPercentage = 72;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
			}
			break;

		case EFFLUENT_TREATMENT_LEVEL_SECONDARY:
			switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
			case EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
			case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
			case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
			case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
			case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				catIPercentage = 0;
				break;
			default:
				throw new CostCurveException(
						"Cannot use cost curves to upgrade this treatment plant");
			}
			break;
		case EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT:
			switch (presentEffluentTypeAdvancedTreatmentSubtype) {
			case EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					catIPercentage = 0;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;

			case EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					catIPercentage = 0;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;
			case EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT:
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					catIPercentage = 0;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;
			case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
				switch (projectedEffluentTypeTypeAdvancedTreatmentSubtype) {
				case EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT:
					catIPercentage = 0;
					break;
				default:
					throw new CostCurveException(
							"Cannot use cost curves to upgrade this treatment plant");
				}
				break;
			}
		}
		return catIPercentage;
	}

	public float getCoefficientAValue(String treatmentCurveType, float flowRate) {
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.curveTypeCd", SearchCondition.OPERATOR_EQ,
				treatmentCurveType));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition(
				"id.minArgumentValue", SearchCondition.OPERATOR_LT,
				new BigDecimal(flowRate)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition(
				"id.maxArgumentValue", SearchCondition.OPERATOR_GT,
				new BigDecimal(flowRate)));
		TreatmentPlantCcFormulaRef t = (TreatmentPlantCcFormulaRef) searchDAO
				.getSearchObject(TreatmentPlantCcFormulaRef.class, scs);
		return t != null ? t.getCoeffAValue().floatValue() : (float) 0.0;
	}

	public float getCoefficientBValue(String treatmentCurveType, float flowRate) {

		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.curveTypeCd", SearchCondition.OPERATOR_EQ,
				treatmentCurveType));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition(
				"id.minArgumentValue", SearchCondition.OPERATOR_LT,
				new BigDecimal(flowRate)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition(
				"id.maxArgumentValue", SearchCondition.OPERATOR_GT,
				new BigDecimal(flowRate)));
		TreatmentPlantCcFormulaRef t = (TreatmentPlantCcFormulaRef) searchDAO
				.getSearchObject(TreatmentPlantCcFormulaRef.class, scs);
		return t != null ? t.getCoeffBValue().floatValue() : (float) 0.0;
	}

	public float getMuliplier(long countyId) {

		SearchConditions scs = new SearchConditions(new SearchCondition(
				"countyId", SearchCondition.OPERATOR_EQ, new Long(countyId)));
		CountyRef c = (CountyRef) searchDAO.getSearchObject(CountyRef.class,
				scs);
		if (c != null) {
			return c.getTreatmentMultiplierRatio().floatValue();
		}
		return 0;
	}

	public double computeBasicCost(float flowRate, float coefficientAValue,
			float coefficientBValue) {
		double cost = coefficientAValue * 1000
				* Math.pow(flowRate, coefficientBValue);
		return cost;
	}

	public double computeCostByPlantType(char plantType, float flowRate,
			float coefficientAValue, float coefficientBValue,
			int projectedFacilityEffluentType) {
		double cost = computeBasicCost(flowRate, coefficientAValue,
				coefficientBValue);
		if (PLANT_TYPE_MECHANICAL == plantType) {
			cost = cost + computeDisinfectionCosts(flowRate);
		} else if (PLANT_TYPE_LOGOON == plantType
				&& EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT == projectedFacilityEffluentType) {
			cost = cost + computeFiltrationCosts(flowRate);
		}

		return cost;
	}

	public double computeDisinfectionCosts(float flowRate) {
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.curveTypeCd", SearchCondition.OPERATOR_EQ,
				CURVE_TYPE_CD_SIX));
		TreatmentPlantCcFormulaRef t = (TreatmentPlantCcFormulaRef) searchDAO
				.getSearchObject(TreatmentPlantCcFormulaRef.class, scs);

		float coefficientAValue = t == null ? (float) 0.0 : t.getCoeffAValue()
				.floatValue();
		float coefficientBValue = t == null ? (float) 0.0 : t.getCoeffBValue()
				.floatValue();

		return computeBasicCost(flowRate, coefficientAValue, coefficientBValue);
	}

	public double computeFiltrationCosts(float flowRate) {
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.curveTypeCd", SearchCondition.OPERATOR_EQ,
				CURVE_TYPE_CD_FIVE));
		TreatmentPlantCcFormulaRef t = (TreatmentPlantCcFormulaRef) searchDAO
				.getSearchObject(TreatmentPlantCcFormulaRef.class, scs);

		float coefficientAValue = t == null ? (float) 0.0 : t.getCoeffAValue()
				.floatValue();
		float coefficientBValue = t == null ? (float) 0.0 : t.getCoeffBValue()
				.floatValue();

		return computeBasicCost(flowRate, coefficientAValue, coefficientBValue);

	}

	/**
	 * IF pop IS LESS THAN 5000 fgpcd = 95 ELSE IF pop IS GREATER OR EQUAL TO
	 * 5000 AND pop IS LESS THAN 10000 fgpcd = 105 ELSE IF pop IS GREATER OR
	 * EQUAL TO 10000 fgpcd = 115
	 * 
	 * @param futurePopulation
	 * @return
	 */
	public int computeProjectedGPCD(int futurePopulation) {
		int fgpcd = 0;
		if (futurePopulation < 5000) {
			fgpcd = 95;
		} else if (futurePopulation < 10000) {
			fgpcd = 105;
		} else {
			fgpcd = 115;
		}

		return fgpcd;
	}

	public float computeFutureMunicipalDesignFlow(
			int projectedNonResidentialReceivingPopulation,
			int projectedUpstreamNonResidentialReceivingPopulation,
			int projectedResidentialReceivingPopulation,
			int projectedUpstreamResidentialReceivingPopulation, int gpcd) {
		float fut_mun_flow_rate = (float) ((projectedNonResidentialReceivingPopulation + projectedUpstreamNonResidentialReceivingPopulation)
				* 0.6 + projectedResidentialReceivingPopulation + projectedUpstreamResidentialReceivingPopulation)
				* gpcd / 1000000;
		// round to 1/1000
		int int_fut_mun_flow_rate = Math.round(fut_mun_flow_rate * 1000);

		return (float) (int_fut_mun_flow_rate / 1000.0);

	}

	// Apply Treatment Multiplier and Split Cost
	public List applyMultiplierSplitCost(double cost_base_amount,
			double multiplier, int catISplitPercentage) {
		List costOutputs = new ArrayList();

		cost_base_amount = cost_base_amount * multiplier * 1000;

		CostOutput costOutput1 = new CostOutput();
		long cost1 = Math.round(cost_base_amount * catISplitPercentage / 100.0);
		costOutput1.setCost(cost1);
		costOutput1.setCategoryID("I");
		costOutputs.add(costOutput1);

		CostOutput costOutput2 = new CostOutput();
		long cost2 = Math.round(cost_base_amount * (100 - catISplitPercentage)
				/ 100.0);
		costOutput2.setCost(cost2);
		costOutput2.setCategoryID("II");
		costOutputs.add(costOutput2);
		return costOutputs;
	}

	private SearchDAO searchDAO;

	public void setSearchDAO(SearchDAO dao) {
		searchDAO = dao;
	}

}

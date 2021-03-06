package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * FacilityTypeCategoryRef generated by hbm2java
 */
public class FacilityTypeCategoryRef implements java.io.Serializable {

	// Fields    

	private FacilityTypeCategoryRefId id;

	private CategoryRef categoryRef;

	private FacilityTypeRef facilityTypeRef;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public FacilityTypeCategoryRef() {
	}

	/** full constructor */
	public FacilityTypeCategoryRef(FacilityTypeCategoryRefId id,
			CategoryRef categoryRef, FacilityTypeRef facilityTypeRef,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.id = id;
		this.categoryRef = categoryRef;
		this.facilityTypeRef = facilityTypeRef;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public FacilityTypeCategoryRefId getId() {
		return this.id;
	}

	public void setId(FacilityTypeCategoryRefId id) {
		this.id = id;
	}

	public CategoryRef getCategoryRef() {
		return this.categoryRef;
	}

	public void setCategoryRef(CategoryRef categoryRef) {
		this.categoryRef = categoryRef;
	}

	public FacilityTypeRef getFacilityTypeRef() {
		return this.facilityTypeRef;
	}

	public void setFacilityTypeRef(FacilityTypeRef facilityTypeRef) {
		this.facilityTypeRef = facilityTypeRef;
	}

	public String getLastUpdateUserid() {
		return this.lastUpdateUserid;
	}

	public void setLastUpdateUserid(String lastUpdateUserid) {
		this.lastUpdateUserid = lastUpdateUserid;
	}

	public Date getLastUpdateTs() {
		return this.lastUpdateTs;
	}

	public void setLastUpdateTs(Date lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}

}

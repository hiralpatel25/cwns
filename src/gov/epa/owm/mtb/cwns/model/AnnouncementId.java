package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * AnnouncementId generated by hbm2java
 */
public class AnnouncementId implements java.io.Serializable {

	// Fields

	private long administrativeMessageId;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public AnnouncementId() {
	}

	/** full constructor */
	public AnnouncementId(long administrativeMessageId, Date lastUpdateTs) {
		this.administrativeMessageId = administrativeMessageId;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public long getAdministrativeMessageId() {
		return this.administrativeMessageId;
	}

	public void setAdministrativeMessageId(long administrativeMessageId) {
		this.administrativeMessageId = administrativeMessageId;
	}

	public Date getLastUpdateTs() {
		return this.lastUpdateTs;
	}

	public void setLastUpdateTs(Date lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AnnouncementId))
			return false;
		AnnouncementId castOther = (AnnouncementId) other;

		return (this.getAdministrativeMessageId() == castOther
				.getAdministrativeMessageId())
				&& ((this.getLastUpdateTs() == castOther.getLastUpdateTs()) || (this
						.getLastUpdateTs() != null
						&& castOther.getLastUpdateTs() != null && this
						.getLastUpdateTs().equals(castOther.getLastUpdateTs())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getAdministrativeMessageId();
		result = 37
				* result
				+ (getLastUpdateTs() == null ? 0 : this.getLastUpdateTs()
						.hashCode());
		return result;
	}

}

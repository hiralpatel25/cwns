package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * DocumentTypeGroupRefId generated by hbm2java
 */
public class DocumentTypeGroupRefId implements java.io.Serializable {

	// Fields

	private long documentGroupId;

	private String documentTypeId;

	// Constructors

	/** default constructor */
	public DocumentTypeGroupRefId() {
	}

	/** full constructor */
	public DocumentTypeGroupRefId(long documentGroupId,
			String documentTypeId) {
		this.documentGroupId = documentGroupId;
		this.documentTypeId = documentTypeId;
	}

	// Property accessors
	public long getDocumentGroupId() {
		return this.documentGroupId;
	}

	public void setDocumentGroupId(long documentGroupId) {
		this.documentGroupId = documentGroupId;
	}

	public String getDocumentTypeId() {
		return this.documentTypeId;
	}

	public void setDocumentTypeId(String documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DocumentTypeGroupRefId))
			return false;
		DocumentTypeGroupRefId castOther = (DocumentTypeGroupRefId) other;

		return (this.getDocumentGroupId() == castOther.getDocumentGroupId())
				&& ((this.getDocumentTypeId() == castOther
						.getDocumentTypeId()) || (this.getDocumentTypeId() != null
						&& castOther.getDocumentTypeId() != null && this
						.getDocumentTypeId().equals(
								castOther.getDocumentTypeId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getDocumentGroupId();
		result = 37
				* result
				+ (getDocumentTypeId() == null ? 0 : this
						.getDocumentTypeId().hashCode());
		return result;
	}

}

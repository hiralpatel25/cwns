package gov.epa.owm.mtb.cwns.pointofcontact;

public class PointOfContactHelper {

	private long pointOfContactId;

	private String name;

	private String authorityName;

	private String roleTitle;

	private String phone;

	private String email;

	private char sourcedFromNpdesFlag;

	private String feedbackDeleteFlag = "";

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFeedbackDeleteFlag() {
		return feedbackDeleteFlag;
	}

	public void setFeedbackDeleteFlag(String feedbackDeleteFlag) {
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getPointOfContactId() {
		return pointOfContactId;
	}

	public void setPointOfContactId(long pointOfContactId) {
		this.pointOfContactId = pointOfContactId;
	}

	public String getRoleTitle() {
		return roleTitle;
	}

	public void setRoleTitle(String roleTitle) {
		this.roleTitle = roleTitle;
	}

	public char getSourcedFromNpdesFlag() {
		return sourcedFromNpdesFlag;
	}

	public void setSourcedFromNpdesFlag(char sourcedFromNpdesFlag) {
		this.sourcedFromNpdesFlag = sourcedFromNpdesFlag;
	}
}

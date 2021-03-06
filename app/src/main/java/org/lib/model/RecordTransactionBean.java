package org.lib.model;

public class RecordTransactionBean {
	// <transaction from="localuser1"
	// to="sriram" sessid="a5d60417cab787018dd9e6b5a7d01edd"
	// type="101" stime="2015-04-17 18:43:49" etime="2015-04-17 18:43:54"
	// userid="localuser1" network="2" deviceos="IOS" ac="0" />
	private String parentID;
	private String fromName;
	private String toName;
	private String sessionid;
	private String type;
	private String startTime;
	private String endTime;
	private String userId;
	private String network;
	private String deviceOS;
	private String calltype;
	private String ac;
	private String callDuration = null;
	private String createdDate;
	private String bs_calltype = null;
	private String bsStatus;
	private int status=0;
   private String host;
    private String participants;
    private String disableVideo;
	private String callstatus;
	private String chatid;

	public String getRecordedfile() {
		return recordedfile;
	}

	public void setRecordedfile(String recordedfile) {
		this.recordedfile = recordedfile;
	}

	private String recordedfile;


	private String host_emailid;

	private String tot_participant;

	private String call_state;

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getBs_calltype() {
		return bs_calltype;
	}

	public void setBs_calltype(String bs_calltype) {
		this.bs_calltype = bs_calltype;
	}

	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getBsStatus() {
		return bsStatus;
	}

	public void setBsStatus(String bsStatus) {
		this.bsStatus = bsStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
   public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getDisableVideo() {
        return disableVideo;
    }

    public void setDisableVideo(String disableVideo) {
        this.disableVideo = disableVideo;
    }

	public String getChatid() {
		return chatid;
	}

	public void setChatid(String chatid) {
		this.chatid = chatid;
	}

	public String getCallstatus() {
		return callstatus;
	}

	public void setCallstatus(String callstatus) {
		this.callstatus = callstatus;
	}

	public String getTot_participant() {
		return tot_participant;
	}

	public void setTot_participant(String tot_participant) {
		this.tot_participant = tot_participant;
	}

	public String getCall_state() {
		return call_state;
	}

	public void setCall_state(String call_state) {
		this.call_state = call_state;
	}

	public String getHost_emailid() {
		return host_emailid;
	}

	public void setHost_emailid(String host_emailid) {
		this.host_emailid = host_emailid;
	}

}

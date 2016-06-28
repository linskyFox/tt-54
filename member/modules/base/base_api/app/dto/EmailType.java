package dto;

import java.io.Serializable;
import java.util.Date;

public class EmailType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cname;

	private String ccreateuser;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public String getCname() {
		return cname;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}

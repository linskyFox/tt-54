package entity.loyalty;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class Theme {

	private Integer iid;

	@NotNull
	private String curl;

	private String cbannerurl;

	@NotNull
	private Integer icssid;

	private String ccreateuser;

	private Date dcreatedate;

	private String cupdateuser;

	private Date dupdatedate;

	@NotNull
	private Integer ienable;

	@NotNull
	private Date denablestartdate;

	@NotNull
	private Date denableenddate;

	@NotNull
	private Integer iwebsiteid;

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl == null ? null : curl.trim();
	}

	public String getCbannerurl() {
		return cbannerurl;
	}

	public void setCbannerurl(String cbannerurl) {
		this.cbannerurl = cbannerurl == null ? null : cbannerurl.trim();
	}

	public Integer getIcssid() {
		return icssid;
	}

	public void setIcssid(Integer icssid) {
		this.icssid = icssid;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCupdateuser() {
		return cupdateuser;
	}

	public void setCupdateuser(String cupdateuser) {
		this.cupdateuser = cupdateuser == null ? null : cupdateuser.trim();
	}

	public Date getDupdatedate() {
		return dupdatedate;
	}

	public void setDupdatedate(Date dupdatedate) {
		this.dupdatedate = dupdatedate;
	}

	public Integer getIenable() {
		return ienable;
	}

	public void setIenable(Integer ienable) {
		this.ienable = ienable;
	}

	public Date getDenablestartdate() {
		return denablestartdate;
	}

	public void setDenablestartdate(Date denablestartdate) {
		this.denablestartdate = denablestartdate;
	}

	public Date getDenableenddate() {
		return denableenddate;
	}

	public void setDenableenddate(Date denableenddate) {
		this.denableenddate = denableenddate;
	}
}
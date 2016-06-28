package valueobjects.product;

import java.io.Serializable;

public class AdItem implements Serializable{
	private static final long serialVersionUID = 1L;
	String title;
	String imgUrl;
	String url;
	String defaultShow;

    private String cbgimageurl;
    private String cbgcolor;
    private boolean bbgimgtile; //鑳屾櫙鍥炬槸鍚﹀钩閾�
    private boolean bhasbgimage;


	public AdItem(String title, String imgUrl, String url, String defaultShow) {
		super();
		this.title = title;
		this.imgUrl = imgUrl;
		this.url = url;
		this.defaultShow = defaultShow;
	}
	
	public AdItem(String title, String imgUrl, String url, String defaultShow,
			String cbgimageurl, String cbgcolor, boolean bbgimgtile) {
		super();
		this.title = title;
		this.imgUrl = imgUrl;
		this.url = url;
		this.defaultShow = defaultShow;
		this.cbgimageurl = cbgimageurl;
		this.cbgcolor = cbgcolor;
		this.bbgimgtile = bbgimgtile;
	}

	public String getTitle() {
		return title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * 榛樿鏄剧ず鏍煎紡  a,img
	 * @return
	 */
	public String getDefaultShow() {
		return this.defaultShow;
	}
	
	public String getCbgimageurl() {
		return cbgimageurl;
	}


	public String getCbgcolor() {
		return cbgcolor;
	}


	public boolean isBbgimgtile() {
		return bbgimgtile;
	}

	public boolean isBhasbgimage() {
		return bhasbgimage;
	}

}

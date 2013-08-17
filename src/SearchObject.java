public class SearchObject {
	private String name;
	private int price;
	private String imgurl;
	private String type;
	private String Link;
	private String source;
	
	void setName(String Name) {
		this.name = Name;
	}
	void setPrice(int Price) {
		this.price = Price;
	}
	void setImgurl(String Imgurl) {
		this.imgurl = Imgurl;
	}
	void setType(String Type) {
		this.type = Type;
	}
	String getName() {
		return this.name;
	}
	int getPrice() {
		return this.price;
	}
	String getImgurl() {
		return this.imgurl;
	}
	String getType() {
		return this.type;
	}
	public String getLink() {
		return Link;
	}
	public void setLink(String link) {
		Link = link;
	}
	public SearchObject(String Name, int Price, String Imgurl, String Type, String LINK) {
		this.name = Name;
		this.price = Price;
		this.imgurl = Imgurl;
		this.type = Type;
		this.Link = LINK;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
}
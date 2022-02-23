package spider.page.constant;

public enum PageUrlTypeEnum {
	Blogs(1,"微博"),
	Comments(2, "评论"),
	Downable(99, "下载物");

	private final int code;
	private final String text;

	PageUrlTypeEnum(int code, String text) {
		this.code=code;
		this.text=text;
	}

	public int getCode() {
		return code;
	}

	public String getText() {
		return text;
	}
}

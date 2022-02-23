package spider.page.constant;

public enum StepEnum {
	Auth(0,"登录"),
	User_Tab(1, "获取用户TabID"),
	Blog_List(2, "获取用户微博列表"),
	Comment_List(3, "获取单条微博的评论列表"),
	Download(99, "下载模式");

	private final int code;
	private final String text;

	StepEnum(int code, String text) {
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

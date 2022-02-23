package spider.page.constant;

/**
 * @ClassName Switches
 * @Description 放置各种开关参数
 * @date 2022/2/9 10:46
 * @Author eee27
 */
public class Switches {
	/**
	 * 是否抓取微博的图片
	 */
	private static Boolean captureBlogImg =true;

	/**
	 * 是否抓取评论的图片
	 */
	private static Boolean captureCommentImg=false;

	/**
	 * 对于每个用户,最多爬多少页微博
	 */
	public static Integer maxBlogPagesForEveryOne =2;

	/**
	 * 对于每条微博,最多爬多少页评论
	 */
	public static Integer maxCommentsForEveryBlog=1;




	public static Boolean getCaptureBlogImg() {
		return captureBlogImg;
	}

	public static void setCaptureBlogImg(Boolean captureBlogImg) {
		Switches.captureBlogImg = captureBlogImg;
	}

	public static Boolean getCaptureCommentImg() {
		return captureCommentImg;
	}

	public static void setCaptureCommentImg(Boolean captureCommentImg) {
		Switches.captureCommentImg = captureCommentImg;
	}
}

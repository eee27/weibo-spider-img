package spider.page.constant;

/**
 * @ClassName statics
 * @Description 常量
 * @date 2022/2/9 10:11
 * @Author eee27
 */
public class Statics {
	public static final String Mobile_Api_Prefix = "https://m.weibo.cn/";

	public static String Local_Disk="D";
	public static String Local_Root_Path=Local_Disk+":\\spider\\weibo\\";
	public static String Local_User_List_File=Local_Root_Path+"user.txt";
	public static String Local_Downable_Save_Path =Local_Root_Path+"downable\\";


	public static final String Mobile_Login_Path="https://passport.weibo.cn/sso/login";

	//根据用户Id查询基本信息
	@Deprecated
	public static final String Index_Info_Path = "api/container/getIndex?uid={0}&type=uid&containerid=100505{0}";

	//根据tab的id查询微博列表
	public static final String Cards_Info_Path = "api/container/getIndex?uid={0}&type=uid&containerid=107603{0}{1}";

	//根据获得的单条微博的id获取该条微博的详细信息
	//private static final String WEIBO_ITEM_INFO_PATH = "show?id={0}";

	//根据单条微博的id获取评论信息,若有分页,则{1}为当前分页的最大值id,需要在下次传进去
	public static final String Comment_List_Path = "comments/hotflow?id={0}&mid={0}{1}&max_id_type=0";

	//另一个根据微博id获取评论信息的接口,还没代码里试过有什么区别
	public static final String Comment_API_Path = "api/comments/show?id={0}&spider.page={1}";

	public static String Single_Test_Id=null;

	public static final String Login_Name = "111111111";
	public static final String Password = "11111111";

	public static final Integer Packet_Success_Code = 1;
	public static final Integer Packet_Fail_Login_Code=-100;

	public static final String Blog_Tab_Type = "weibo";

	/**
	 * 所有微博用户首页,containerId只需要在用户Id前面加这个,很难变动
	 */
	public static final String MainPage_Weibo_Container_Id="100505";

	/**
	 * 所有用户的微博Tab页的containerId只需要在用户Id前面加这个,很难变动
	 */
	public static final String Tab_Weibo_Container_Id="107603";



	public static final String Weibo_Time_Format="EEE MMM d HH:mm:ss Z yyyy";
	public static final String Common_Time_Format="yyyy-MM-dd hh:mm:ss";






}

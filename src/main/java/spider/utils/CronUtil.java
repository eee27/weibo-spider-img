package spider.utils;

/**
 * @ClassName CronUtil
 * @Description 定时表达式
 * @date 2022/2/10 9:11
 * @Author eee27
 */
public class CronUtil {
	/*
    Cron1: Seconds Minutes Hours DayofMonth(L W) Month DayofWeek(L #) Year
    Cron2: Seconds Minutes Hours DayofMonth Month DayofWeek

    week start from sunday

    ,   5,20    Trigger on 5 and 20
    -   5-20    Trigger on 5 to 20
    *           Trigger Each Unit
    /   5/20    Trigger on 5,25,45...

    L   final
    W   workday
    #   4#2     Trigger on second week's third day each month

    */

	/**
	 * 每1分钟
	 */
	public static final String Every_1_Min="0 */1 * * * ?";

	/**
	 * 每2分钟
	 */
	public static final String Every_2_Min="0 */2 * * * ?";

	/**
	 * 每5分钟
	 */
	public static final String Every_5_Min="0 */5 * * * ?";

	/**
	 * 工作时间(早9晚5)每半个点
	 */
	public static final String Work_Day_Every_30_min="0 0/30 9-17 * * ?";

	/**
	 * 每天凌晨2点
	 */
	public static final String Every_Day_2AM="0 0 2* * ?";

	/**
	 * 每天14点内的每分钟
	 */
	public static final String Every_1_Min_In_H14="0 * 14 * * ?";

	/**
	 * 周一到周五的10:15
	 */
	public static final String Work_Day_1015="0 15 10 ? * MON-FRI";


}

package spider.executor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import spider.define.FixedHttpClientDownloader;
import spider.page.constant.Statics;
import spider.page.constant.Switches;
import spider.page.processor.WbMobileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.utils.HttpConstant;
import spider.utils.CronUtil;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static spider.page.constant.Statics.Cards_Info_Path;
import static spider.page.constant.Statics.Local_Root_Path;
import static spider.page.constant.Statics.Mobile_Api_Prefix;

/**
 * @ClassName WbMyImgExec
 * @Description 微博爬虫执行器,初始化爬虫,并定期调取执行
 * @date 2022/2/7 14:54
 * @Author eee27
 */
@Component
@EnableScheduling
public class WbSpiderExecutor {
	Logger logger= LoggerFactory.getLogger("Spider");

	/**
	 * 用来保存图片Url->对应作者id+名字的映射,在解析时插入map,下载时据此放到对应文件夹
	 */
	private static HashMap<String,String> downloadablePathMap =new HashMap<>();

	/**
	 * 放置待爬取的用户列表
	 */
	private static List<String> targetIds=new ArrayList<>();

	private static HashMap<String,Integer> targetBlogCounts=new HashMap<>();

	/**
	 * 记录每次启动后爬取的评论链接,避免重复访问
	 */
	private static final AtomicReference<HashSet<String>> visitedCommentUrls = new AtomicReference<>(new HashSet<>());

	/**
	 * 记录每次启动后下载的文件地址,避免重复下载
	 */
	private static final AtomicReference<HashSet<String>> downloadedUrls = new AtomicReference<>(new HashSet<>());


	private static HashMap<String,Integer> maxBlogsMap=new HashMap<>();
	private static HashMap<String,Integer> maxCommentsMap=new HashMap<>();

	private Spider spider;

	public WbSpiderExecutor(){
		FileUtil.mkdir(Statics.Local_Downable_Save_Path);
		FileUtil.touch(Statics.Local_User_List_File);
	}


	@Scheduled(cron = CronUtil.Every_5_Min)
	public void exec(){
		logger.info("爬虫开始执行");
		readUsersList();

		spider=initAndAuth();
		spider.run();
	}

	/**
	 * 每次定时任务开启时加载固定位置的抓取用户列表
	 * 也就是说可以随时改之后保存等下一次跑批生效
	 * @return
	 */
	private void readUsersList(){
		if(StringUtils.isNotBlank(Statics.Single_Test_Id)){
			targetIds.add(Statics.Single_Test_Id);
			return;
		}

		List<String> dirtyUserIdsData=FileUtil.readLines(Statics.Local_User_List_File, StandardCharsets.UTF_8);
		for (String dirtyUserId : dirtyUserIdsData) {
			if(NumberUtil.isNumber(dirtyUserId)){
				//ReUtil.isMatch("(\\d+)", dirtyUserId);
				targetIds.add(dirtyUserId);
			}
		}

		if(targetIds.size()<=0){
			targetIds.add("1111681197");
		}
		logger.info("目标用户数:"+targetIds.size());
	}

	/**
	 * 初始化爬虫并进行登录获取cookie,初始化只应一次,后续要验证cookies并调用登录
	 * @return
	 */
	private Spider initAndAuth(){
		FixedHttpClientDownloader downloader=new FixedHttpClientDownloader();
		WbMobileProcessor processor=new WbMobileProcessor();

		Spider spider=Spider.create(processor)
				.setDownloader(downloader)
				.thread(5)
				.addPipeline(new ConsolePipeline())
				.addPipeline(new FilePipeline(Local_Root_Path));

		addEntryUrlsToSpider(spider);
		return spider;
	}

	private void auth() {
		Request request=new Request(Statics.Mobile_Login_Path);
		request.setMethod(HttpConstant.Method.POST);
		request.addHeader("Content-Type", ContentType.FORM_URLENCODED.getValue());
		request.addHeader(HttpConstant.Header.USER_AGENT, "Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36 Edg/98.0.4758.80");

		HashMap<String,String> paramsMap=new HashMap<>();
		paramsMap.put("username", "15000000000");
		paramsMap.put("password", "L1111111111");
		paramsMap.put("savestate", "1");
		paramsMap.put("pagerefer", "https://m.weibo.cn/");
		paramsMap.put("entry", "mweibo");

		String params= HttpUtil.toParams(paramsMap);
		System.out.println("PARAM:"+params);
		HttpRequestBody body=new HttpRequestBody();
		body.setBody(params.getBytes(StandardCharsets.UTF_8));
		request.setRequestBody(body);
		spider.addRequest(request);
	}

	/**
	 * 每次爬取前将根url加入爬虫
	 * @param spider
	 */
	private void addEntryUrlsToSpider(Spider spider) {
		for (String userId : targetIds) {
			//无翻页,所以sinceId不要
			spider.addUrl(Mobile_Api_Prefix+MessageFormat.format(Cards_Info_Path,userId,""));
			WbSpiderExecutor.addVisitBlogsCount(userId);
		}
	}


	public static void putItemToDownablePathMap(String key, String value){
		downloadablePathMap.put(key,value);
	}

	public static String getPathFromPicMap(String key){
		return downloadablePathMap.get(key);
	}

	public static boolean existInPathMap(String key){
		return downloadablePathMap.containsKey(key);
	}

	public static void putItemToDownloadedUrls(String url){
		downloadedUrls.get().add(url);
	}

	public static boolean existInDownloadedUrls(String url){
		return downloadedUrls.get().contains(url);
	}

	public static boolean reachMaxBlogsMap(String key) {
		if(Switches.maxBlogPagesForEveryOne==0||!maxBlogsMap.containsKey(key)){return false;}
		return maxBlogsMap.get(key) >= Switches.maxBlogPagesForEveryOne;
	}

	public static void addVisitBlogsCount(String key) {
		if(!maxBlogsMap.containsKey(key)){
			maxBlogsMap.put(key, 1);
		}else{
			maxBlogsMap.replace(key, maxBlogsMap.get(key)+1);
		}
	}

	public static boolean reachMaxCommentsMap(String key) {
		if(Switches.maxCommentsForEveryBlog==0||!maxCommentsMap.containsKey(key)){return false;}
		return maxCommentsMap.get(key) >= Switches.maxCommentsForEveryBlog;
	}

	public static void addVisitCommentsCount(String key) {
		if(!maxCommentsMap.containsKey(key)){
			maxCommentsMap.put(key, 1);
		}else{
			maxCommentsMap.replace(key, maxCommentsMap.get(key)+1);
		}
	}

	public static void setTargetBlogCounts(String userId,Integer count){
		targetBlogCounts.put(userId, count);
	}

	public static Integer getTargetBlogCounts(String userId){
		return targetBlogCounts.get(userId);
	}
}

package spider.page.processor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import spider.executor.WbSpiderExecutor;
import spider.model.Card;
import spider.model.CardListInfo;
import spider.model.CardsPage;
import spider.model.Comment;
import spider.model.CommentPage;
import spider.model.HomePage;
import spider.model.MBlog;
import spider.model.PicInfo;
import spider.model.Tab;
import spider.page.constant.PageUrlTypeEnum;
import spider.page.constant.Statics;
import spider.page.constant.StepEnum;
import spider.page.constant.Switches;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WbProcessor
 * @Description 手机端接口抓取
 * @date 2022/2/7 14:15
 * @Author eee27
 */
public class WbMobileProcessor implements PageProcessor {
	Logger logger = LoggerFactory.getLogger("Spider");

	public WbMobileProcessor(){}

	Site site = Site.me()
			.setDomain(".m.weibo.cn")
			.setRetryTimes(3)
			.setSleepTime(100)
			.setCharset(StandardCharsets.UTF_8.name())
			.setUserAgent("Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36 Edg/98.0.4758.80")
			;

	private static final ObjectMapper objectMapper=new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


	@Override
	public void process(Page page) {
		logger.info("***-----------------开始单次请求-------spider.page.getRequest().getUrl()-----------***");
		Integer requestType=requestTypeCheck(page);

		if(requestType.equals(StepEnum.Auth.getCode())){
			requestAuth(page);
		}else if(requestType.equals(StepEnum.User_Tab.getCode())){
			requestUserHomePage(page);
		}else if(requestType.equals(StepEnum.Blog_List.getCode())){
			requestWeiboListByTabContainerId(page);
		}else if(requestType.equals(StepEnum.Comment_List.getCode())){
			requestCommentsListByBlogId(page);
		}else if(requestType.equals(StepEnum.Download.getCode())){
			requestDownload(page);
		}

		logger.info("***-----------------结束单次请求------------------***");
	}


	//region 请求分类处理相关方法
	/**
	 * 根据链接区分不同处理方式
	 * @param page
	 * @return
	 */
	private Integer requestTypeCheck(Page page){
		if(page.getRequest().getUrl().contains("sso/login")){
			return StepEnum.Auth.getCode();
		}

		if(page.getRequest().getUrl().contains("container/getIndex")){
			if(page.getRequest().getUrl().contains("containerid=100505")){
				//从userId解析weibo页面的containerId,目前因为获取到了107603的固定头所以用不上了
				return StepEnum.User_Tab.getCode();
			}else if(page.getRequest().getUrl().contains("containerid=107603")){
				return StepEnum.Blog_List.getCode();
			}
		}else if(page.getRequest().getUrl().contains("hotflow")){
			return StepEnum.Comment_List.getCode();
		}
		return StepEnum.Download.getCode();
	}

	/**
	 * 登录
	 * @param page
	 */
	@Deprecated
	private void requestAuth(Page page){
		logger.info("login");
		Map<String,String> cookies=page.getRequest().getCookies();
		for (Map.Entry<String, String> entry : cookies.entrySet()) {
			logger.info("COOKIE");
			logger.info(entry.getKey());
			logger.info(entry.getValue());
		}
	}

	/**
	 * 根据用户ID请求首页的微博tabId
	 * @param page
	 * @return
	 */
	@Deprecated
	private void requestUserHomePage(Page page) {
		String tabContainerId = null;

		JSONObject jsonObject = getRequestJsonObject(page);
		if(!jsonObject.containsKey("ok")){logger.error("请求内无状态码"); return;}
		Integer resultCount = jsonObject.get("ok", Integer.class);
		if (!resultCount.equals(Statics.Packet_Success_Code)) {logger.error("请求内部状态码报错"+resultCount);return;}
		//解析报文
		try {
			HomePage homePage = objectMapper.readValue(jsonObject.getJSONObject("data").toString(), HomePage.class);

			logger.info("用户ID:" + homePage.getUserInfo().getId());
			logger.info("用户名:" + homePage.getUserInfo().getName() + " | " + homePage.getUserInfo().getScreenName());
			logger.info("微博数:" + homePage.getUserInfo().getBlogCount());

			List<Tab> tabs = homePage.getTabsData().getTabs();
			for (Tab tab : tabs) {
				logger.debug("用户主页分页:" + tab.getId() + " | " + tab.getContainerId() + " | " + tab.getTitle());
				if (tab.getTabKey().equals(Statics.Blog_Tab_Type)) {
					//分页为weibo页,取其id
					tabContainerId = tab.getContainerId();

					String targetTabContainerUrl= Statics.Mobile_Api_Prefix+MessageFormat
							.format(Statics.Index_Info_Path
							,homePage.getUserInfo().getId()
							,tabContainerId);
					logger.debug("添加用户微博Tab页面:"+targetTabContainerUrl);
					page.addTargetRequest(targetTabContainerUrl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据微博页的tabId,请求用户的最新微博列表
	 * @param page
	 */
	private void requestWeiboListByTabContainerId(Page page){
		JSONObject jsonObject = getRequestJsonObject(page);
		if(!jsonObject.containsKey("ok")){logger.error("请求内无状态码"); return;}
		Integer resultCount = jsonObject.get("ok", Integer.class);
		if (!resultCount.equals(Statics.Packet_Success_Code)) {logger.error("请求内部状态码报错"+resultCount);return;}

		//解析报文
		try {
			CardsPage cardsPage = objectMapper.readValue(jsonObject.getJSONObject("data").toString(), CardsPage.class);

			//基本信息
			CardListInfo cardListInfo=cardsPage.getCardListInfo();
			logger.info("TabContainerId:"+cardListInfo.getContainedId());
			logger.info("总计微博条数:"+cardListInfo.getTotal());

			//在不抓取评论的情况下,若微博条数比上次没有变化,则结束
			//若少了,则覆盖上次结果,结束
			/*if(!Switches.getCaptureCommentImg()) {
				String userId = cardListInfo.getContainedId().replace(Statics.Tab_Weibo_Container_Id, "");
				if (WbSpiderExecutor.getTargetBlogCounts(userId) != null) {
					if (WbSpiderExecutor.getTargetBlogCounts(userId) >= cardListInfo.getTotal()) {
						//上次比这次多,或者一样,不抓
						logger.info("微博数未增加,跳过后续抓取:" + cardListInfo.getTotal() + ":" + WbSpiderExecutor.getTargetBlogCounts(userId));

						logger.info(WbSpiderExecutor.getTargetBlogCounts(userId).toString());
						WbSpiderExecutor.setTargetBlogCounts(userId, cardListInfo.getTotal());
						spider.page.setSkip(true);
						return;
					}
				}
				WbSpiderExecutor.setTargetBlogCounts(userId, cardListInfo.getTotal());
			}*/


			//处理翻页
			if(cardListInfo.getSinceId()!=null&&cardListInfo.getSinceId().length()>1){
				logger.info("微博存在翻页");
				Map<String,String> pagedParams= HttpUtil.decodeParamMap(page.getRequest().getUrl(), Charset.forName(page.getCharset()));
				if(pagedParams.containsKey("uid")){
					String uid=pagedParams.get("uid");
					String nextPageCommentUrl=Statics.Mobile_Api_Prefix+
							MessageFormat.format(
									Statics.Cards_Info_Path
									,uid
									,"&since_id="+cardListInfo.getSinceId()
							);
					logger.info("入队微博翻页地址:"+nextPageCommentUrl);
					addUrlToSpider(nextPageCommentUrl, PageUrlTypeEnum.Blogs,uid,page);
				}
			}

			//详细列表信息
			List<Card> cards=cardsPage.getCards();
			//为列表里的用户创建对应的图片保存文件夹
			String userId=null;
			String screenName=null;
			if(cards!=null&&cards.size()>0){
				userId=cards.get(0).getmBlog().getUser().getId();
				screenName=cards.get(0).getmBlog().getUser().getScreenName();
				mkdirForUser(userId,screenName);
			}
			//解析每条微博信息
			for (Card card : cards) {
				logger.info("CardId:"+card.getItemId());
				logger.info("是否最新一条:"+card.getLastWeibo());

				MBlog mBlog=card.getmBlog();
				logger.info("内容:"+mBlog.getText());
				logger.info("发送时间:"+ DateUtil.format(mBlog.getCreatedAt(), Statics.Common_Time_Format));
				logger.info("微博bid:"+mBlog.getBid());
				logger.info("id:"+mBlog.getId());

				if(Switches.getCaptureBlogImg()) {
					//抓取正文图片
					if (mBlog.getPicNum()==null || mBlog.getPicNum()<=0) {
						logger.info("无图");
					} else {
						for (PicInfo pic : mBlog.getPics()) {
							String picUrl=picPathReplace(pic.getUrl());
							logger.info("入队待下载微博图片地址:" + picUrl);
							addUrlToSpider(picUrl,PageUrlTypeEnum.Downable,null,page);
							WbSpiderExecutor.putItemToDownablePathMap(picUrl, joinUserBlogDownablePath(userId,screenName));
						}
					}
					//抓取正文视频
					if(mBlog.getVideoInfo()==null){
					}else{
						logger.info("有视频");
						logger.info("标题:"+mBlog.getVideoInfo().getVideoTitle());
						String videoUrl=null;
						if(mBlog.getVideoInfo().getMediaInfo().getStreamUrlHd()!=null){
							videoUrl=mBlog.getVideoInfo().getMediaInfo().getStreamUrlHd();
						}else{
							videoUrl=mBlog.getVideoInfo().getMediaInfo().getStreamUrl();
						}
						addUrlToSpider(videoUrl, PageUrlTypeEnum.Downable,null,page);
						WbSpiderExecutor.putItemToDownablePathMap(videoUrl, joinUserBlogDownablePath(userId,screenName));
					}
				}
				if (Switches.getCaptureCommentImg()) {
					//添加评论链接到待抓取
					String firstPageCommentsUrl=Statics.Mobile_Api_Prefix+
							MessageFormat
									.format(Statics.Comment_List_Path
											, mBlog.getId()
									,"");
					logger.info("入队首页评论地址:"+firstPageCommentsUrl);
					addUrlToSpider(firstPageCommentsUrl, PageUrlTypeEnum.Comments,mBlog.getId(),page);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据微博列表内包的id获取评论列表
	 * @param page
	 */
	private void requestCommentsListByBlogId(Page page){
		JSONObject jsonObject = getRequestJsonObject(page);
		if(!jsonObject.containsKey("ok")){logger.error("请求内无状态码"); return;}
		Integer resultCount = jsonObject.get("ok", Integer.class);
		if (!resultCount.equals(Statics.Packet_Success_Code)) {logger.error("请求内部状态码报错"+resultCount);return;}

		//解析报文
		try {
			CommentPage commentPage = objectMapper.readValue(jsonObject.getJSONObject("data").toString(), CommentPage.class);

			logger.info("共"+commentPage.getTotalNumber()+"条评论");

			//处理翻页
			if(commentPage.getMaxId()!=null&&commentPage.getMaxId().length()>1){
				logger.info("评论存在翻页");
				Map<String,String> pagedParams= HttpUtil.decodeParamMap(page.getRequest().getUrl(), Charset.forName(page.getCharset()));
				if(pagedParams.containsKey("id")){
					String blogId=pagedParams.get("id");
					String nextPageCommentUrl=Statics.Mobile_Api_Prefix+
							MessageFormat.format(
									Statics.Comment_List_Path
									,blogId
									,"&max_id="+commentPage.getMaxId()
							);
					logger.info("入队评论翻页地址:"+nextPageCommentUrl);
					addUrlToSpider(nextPageCommentUrl, PageUrlTypeEnum.Comments,blogId,page);
				}
			}

			//评论信息和图片获取
			for (Comment comment : commentPage.getCommentDatas()) {
				logger.info("评论人:"+comment.getUser());
				logger.info("楼层:"+comment.getFloorNumber());
				logger.info("评论内容:"+comment.getText());
				logger.info("id:"+comment.getId());
				logger.info("bid:"+comment.getBid());

				if(comment.getPic()!=null){
					//有图
					PicInfo pic=comment.getPic();
					logger.info("评论图片id:"+pic.getPid());

					String downableUrl=picPathReplace(pic.getUrl());
					logger.info("入队待下载微博图片地址"+downableUrl);
					addUrlToSpider(downableUrl, PageUrlTypeEnum.Downable,null,page);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对于图片或其他未匹配到的路径内容,默认下载到本地
	 * @param page
	 */
	private void requestDownload(Page page) {
		if(WbSpiderExecutor.existInDownloadedUrls(page.getRequest().getUrl())){
			logger.info("existed:"+page.getRequest().getUrl());
			return;
		}

		if(WbSpiderExecutor.existInPathMap(page.getRequest().getUrl())){
			HttpUtil.downloadFile(page.getRequest().getUrl(), FileUtil.file(Statics.Local_Downable_Save_Path
					+ WbSpiderExecutor.getPathFromPicMap(page.getRequest().getUrl())));
		}else {
			HttpUtil.downloadFile(page.getRequest().getUrl(), FileUtil.file(Statics.Local_Downable_Save_Path));
		}
		WbSpiderExecutor.putItemToDownloadedUrls(page.getRequest().getUrl());
	}
	//endregion

	//region 各种附加的工具方法
	/**
	 * 根据page请求统一做异常处理,最后返回jsonObject到实际调用方法
	 * @param page
	 * @return
	 */
	private JSONObject getRequestJsonObject(Page page) {
		String result = page.getRawText();
		logger.info("请求结果:" + page.getStatusCode());

		if (page.getStatusCode() >= HttpStatus.HTTP_BAD_REQUEST) {
			logger.error("请求返回码异常:" + page.getStatusCode());
			return null;
		}

		if (!JSONUtil.isJson(result)) {
			logger.error("结果非JSON格式");
			logger.error(result);
			return null;
		}

		return JSONUtil.parseObj(result);
	}

	/**
	 * 将默认获取到的小图替换为大图地址,后面最好直接实体类解析大图对象去获取更稳
	 * @param orj360Url
	 * @return
	 */
	private String picPathReplace(String orj360Url){
		return orj360Url.replace("/orj360/", "/large/");
	}

	public void mkdirForUser(String userId,String screenName){
		try {
			Files.createDirectories(Paths.get(Statics.Local_Downable_Save_Path + joinUserBlogDownablePath(userId, screenName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String joinUserBlogDownablePath(String userId, String screenName){
		return userId+"_"+screenName+"\\";
	}

	/**
	 * 添加链接到爬虫,包装一下原始方法,以供集中判断去重
	 * @param url
	 * @param urlType
	 * @return
	 */
	private boolean addUrlToSpider(String url, PageUrlTypeEnum urlType,String key,Page page){
		if(urlType.equals(PageUrlTypeEnum.Blogs)){
			if(WbSpiderExecutor.reachMaxBlogsMap(key)){
				logger.info("reach max blog count");
				return false;
			}else{
				page.addTargetRequest(url);
				WbSpiderExecutor.addVisitBlogsCount(key);
			}
		}else if(urlType.equals(PageUrlTypeEnum.Comments)){
			if(WbSpiderExecutor.reachMaxCommentsMap(key)){
				logger.info("reach max comment count");
				return false;
			}else{
				page.addTargetRequest(url);
				WbSpiderExecutor.addVisitCommentsCount(key);
			}
		}else{
			if(WbSpiderExecutor.existInDownloadedUrls(url)){
				logger.info("url existed not add:"+url);
				return false;
			}
			page.addTargetRequest(url);
		}
		return true;
	}

	@Override
	public Site getSite() {
		return site;
	}

	//endregion

}

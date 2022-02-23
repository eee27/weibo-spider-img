package spider.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static spider.page.constant.Statics.Weibo_Time_Format;

/**
 * @ClassName MBlog
 * @Description card内单条微博信息
 * @date 2022/2/8 10:19
 * @Author eee27
 */
public class MBlog implements Serializable {
	@JsonProperty(value = "bid")
	private String bid;
	@JsonProperty(value = "comments_count")
	private Integer commentsCount;
	@JsonProperty(value = "created_at")
	@JsonFormat(pattern = Weibo_Time_Format, locale = "en", timezone = "GMT+8")
	private Date createdAt;
	@JsonProperty(value = "id")
	private String id;
	@JsonProperty(value = "picStatus")
	@Deprecated
	private String picStatus;//1
	@JsonProperty(value = "pic_num")
	private Integer picNum;
	@JsonProperty(value = "text")
	private String text;
	@JsonProperty(value = "pic_ids")
	private List<String> picIds;
	@JsonProperty(value = "pics")
	private List<PicInfo> pics;
	@JsonProperty(value = "user")
	private User user;
	@JsonProperty(value = "page_info")
	private VideoInfo videoInfo;


	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public Integer getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Integer commentsCount) {
		this.commentsCount = commentsCount;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getPicIds() {
		return picIds;
	}

	public void setPicIds(List<String> picIds) {
		this.picIds = picIds;
	}

	public List<PicInfo> getPics() {
		return pics;
	}

	public void setPics(List<PicInfo> pics) {
		this.pics = pics;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getPicNum() {
		return picNum;
	}

	public void setPicNum(Integer picNum) {
		this.picNum = picNum;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public VideoInfo getVideoInfo() {
		return videoInfo;
	}

	public void setVideoInfo(VideoInfo videoInfo) {
		this.videoInfo = videoInfo;
	}
}

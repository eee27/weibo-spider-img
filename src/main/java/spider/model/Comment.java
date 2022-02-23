package spider.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

import static spider.page.constant.Statics.Weibo_Time_Format;

/**
 * @ClassName CommentData
 * @Description 评论详细数据,挂载Comment的大List对象内
 * @date 2022/2/7 16:44
 * @Author eee27
 */
public class Comment implements Serializable {
	@JsonProperty(value = "id")
	private String id;
	@JsonProperty(value = "bid")
	private String bid;
	@JsonProperty(value = "like_count")
	private Integer likeCount;
	@JsonProperty(value = "floor_number")
	private Integer floorNumber;
	@JsonProperty(value="created_at")
	@JsonFormat(pattern = Weibo_Time_Format, locale = "en", timezone = "GMT+8")
	private Date createdAt;
	@JsonProperty(value = "text")
	private String text;
	@JsonProperty(value = "pic")
	private PicInfo pic;
	@JsonProperty(value = "user")
	private User user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public Integer getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(Integer floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public PicInfo getPic() {
		return pic;
	}

	public void setPic(PicInfo pic) {
		this.pic = pic;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}

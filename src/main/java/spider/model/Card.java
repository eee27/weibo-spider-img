package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName Card
 * @Description 单条微博信息的外壳
 * @date 2022/2/8 10:11
 * @Author eee27
 */
public class Card implements Serializable {
	@JsonProperty(value = "card_type")
	private Integer cardType;
	@JsonProperty(value = "itemid")
	private String itemId;
	@JsonProperty(value = "lastWeiboCard")
	private Boolean lastWeibo;
	@JsonProperty(value = "mblog")
	private MBlog mBlog;

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Boolean getLastWeibo() {
		return lastWeibo;
	}

	public void setLastWeibo(Boolean lastWeibo) {
		this.lastWeibo = lastWeibo;
	}

	public MBlog getmBlog() {
		return mBlog;
	}

	public void setmBlog(MBlog mBlog) {
		this.mBlog = mBlog;
	}
}

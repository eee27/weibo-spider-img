package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName CardListInfo
 * @Description 微博外壳列表的基础信息
 * @date 2022/2/8 10:10
 * @Author eee27
 */
public class CardListInfo implements Serializable {
	@JsonProperty(value ="containerid")
	private String containedId;
	@JsonProperty(value = "total")
	private Integer total;
	@JsonProperty(value = "since_id")
	private String sinceId;

	public String getContainedId() {
		return containedId;
	}

	public void setContainedId(String containedId) {
		this.containedId = containedId;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getSinceId() {
		return sinceId;
	}

	public void setSinceId(String sinceId) {
		this.sinceId = sinceId;
	}
}

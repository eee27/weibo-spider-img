package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName Comment
 * @Description 评论列表数据,时buildComments返回的结果
 * @date 2022/2/7 16:01
 * @Author eee27
 */
public class CommentPage implements Serializable {
	@JsonProperty(value = "total_number")
	private Integer totalNumber;
	@JsonProperty(value = "data")
	private List<Comment> commentDatas;
	@JsonProperty(value = "max")
	private Integer max;
	@JsonProperty(value = "max_id")
	private String maxId;
	@JsonProperty(value = "max_id_type")
	private Integer maxIdType;

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public List<Comment> getCommentDatas() {
		return commentDatas;
	}

	public void setCommentDatas(List<Comment> commentDatas) {
		this.commentDatas = commentDatas;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String getMaxId() {
		return maxId;
	}

	public void setMaxId(String maxId) {
		this.maxId = maxId;
	}

	public Integer getMaxIdType() {
		return maxIdType;
	}

	public void setMaxIdType(Integer maxIdType) {
		this.maxIdType = maxIdType;
	}
}

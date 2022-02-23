package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description 用户
 * @date 2022/2/7 16:46
 * @Author eee27
 */
public class User implements Serializable {
	@JsonProperty(value = "id")
	private String id;
	@JsonProperty(value = "name")
	private String name;
	@JsonProperty(value = "screen_name")
	private String screenName;
	@JsonProperty(value = "description")
	private String description;
	@JsonProperty(value = "statuses_count")
	private Integer blogCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getBlogCount() {
		return blogCount;
	}

	public void setBlogCount(Integer blogCount) {
		this.blogCount = blogCount;
	}
}

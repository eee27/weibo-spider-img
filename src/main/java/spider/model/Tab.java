package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName Tab
 * @Description 具体装每个tab信息的对象,用户的微博containerid要从这里取 tabkey=weibo
 * @date 2022/2/8 9:51
 * @Author eee27
 */
public class Tab implements Serializable {
	public static String TAB_TYPE_STR="weibo";

	@JsonProperty(value = "id")
	private Integer id;
	@JsonProperty(value = "tabKey")
	private String tabKey;
	@JsonProperty(value = "tab_type")
	private String tabType;
	@JsonProperty(value = "title")
	private String title;
	@JsonProperty(value = "containerid")
	private String containerId;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTabKey() {
		return tabKey;
	}

	public void setTabKey(String tabKey) {
		this.tabKey = tabKey;
	}

	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}
}

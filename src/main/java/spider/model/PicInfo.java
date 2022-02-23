package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName PicInfo
 * @Description 挂载urlstruct内,通过picids map方式获取本对象
 * @date 2022/2/7 15:49
 * @Author eee27
 */
public class PicInfo implements Serializable{
	@JsonProperty(value = "pid")
	private String pid;
	@JsonProperty(value = "size")
	private String size;
	@JsonProperty(value = "url")
	private String url;


	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

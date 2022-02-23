package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName MediaInfo
 * @Description pageInfo内的MediaInfo,记录了详细地址
 * @date 2022/2/10 11:40
 * @Author eee27
 */
public class MediaInfo implements Serializable {
	@JsonProperty(value = "duration")
	private String duration;
	@JsonProperty(value = "stream_url")
	private String streamUrl;
	@JsonProperty(value = "stream_url_hd")
	private String streamUrlHd;

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	public String getStreamUrlHd() {
		return streamUrlHd;
	}

	public void setStreamUrlHd(String streamUrlHd) {
		this.streamUrlHd = streamUrlHd;
	}
}

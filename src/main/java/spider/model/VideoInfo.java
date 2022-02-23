package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @ClassName VideoInfo
 * @Description 在card->mblog->里面,记录视频相关
 * @date 2022/2/10 11:37
 * @Author eee27
 */
public class VideoInfo implements Serializable {
	@JsonProperty(value = "content2")
	private String videoTitle;
	@JsonProperty(value = "media_info")
	private MediaInfo mediaInfo;


	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public MediaInfo getMediaInfo() {
		return mediaInfo;
	}

	public void setMediaInfo(MediaInfo mediaInfo) {
		this.mediaInfo = mediaInfo;
	}
}

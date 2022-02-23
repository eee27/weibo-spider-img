package spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import spider.page.constant.Statics;
import spider.page.constant.Switches;

/**
 * @ClassName spider.SpiderMain
 * @Description 启动类
 * @date 2022/2/17 16:42
 * @Author eee27
 */
@SpringBootApplication
public class SpiderMain  extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpiderMain.class);
	}

	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		return builder.sources(SpiderMain.class);
	}

	public static void main(String[] args) {
			for (String arg : args) {
				String[] keyValue=arg.split("=");
				if(keyValue.length>=2){
					String key=keyValue[0];
					String value=keyValue[1];

					if(key.equals("d")){//disk
						Statics.Local_Disk=value;
					} if(key.equals("cmti")){//comment img
						Switches.setCaptureCommentImg(Boolean.getBoolean(value));
					} if(key.equals("blgi")){//blog img
						Switches.setCaptureBlogImg(Boolean.valueOf(value));
					} if(key.equals("cmtp")){//comment pages
						Switches.maxCommentsForEveryBlog=Integer.getInteger(value);
					} if(key.equals("blgp")){//blog pages
						Switches.maxBlogPagesForEveryOne=Integer.getInteger(value);
					} if(key.equals("u")){//single user
						Statics.Single_Test_Id=value;
					}
				}
			}
		for (String arg : args) {
			System.out.println("参数:"+arg) ;
		}

		SpringApplication.run(SpiderMain.class, args);
	}








}

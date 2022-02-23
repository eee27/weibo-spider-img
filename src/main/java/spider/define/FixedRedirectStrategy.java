package spider.define;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * 2022/2/11 eee27 按照新版的httpclient实现,代替原本的customStrategy
 *
 *
 *支持post 302跳转策略实现类
 *HttpClient默认跳转：httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());
 *上述代码在post/redirect/post这种情况下不会传递原有请求的数据信息。所以参考了下SeimiCrawler这个项目的重定向策略。
 *原代码地址：https://github.com/zhegexiaohuozi/SeimiCrawler/blob/master/project/src/main/java/cn/wanghaomiao/seimi/http/hc/SeimiRedirectStrategy.java
 */
public class FixedRedirectStrategy extends LaxRedirectStrategy {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        URI uri = getLocationURI(request, response, context);

        /*
        * 原本的做法是302重定向时会将原本请求复制再重定向到新地址
        * 在尝试gitlab自动登录时发现其登录为:
        * 1.get登录地址获取cookie和表单隐藏的key
        * 2.post登录地址带cookie和key
        * 3.302get到根地址访问
        * 所以目前改成了一次302后自动跳转到get请求,如果后续出现post被302后接post的情况需要加个开关(302是如何通知客户端使用什么方式的?)
        */
        final int status=response.getStatusLine().getStatusCode();
        if(status== HttpStatus.SC_TEMPORARY_REDIRECT||status==HttpStatus.SC_MOVED_TEMPORARILY){
            //return RequestBuilder.copy(request).setUri(uri).build();
            return new HttpGet(uri);
        }else{
            return new HttpGet();
        }
    }
}

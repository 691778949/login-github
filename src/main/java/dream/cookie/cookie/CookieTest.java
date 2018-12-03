package dream.cookie.cookie;

import org.apache.http.*;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.*;

public class CookieTest {

    @Test
    public void loginGitLab() throws Exception {
        CloseableHttpClient client=HttpClients.createDefault();
        HttpGet httpGet=new HttpGet("http://**/users/sign_in");
        CloseableHttpResponse httpResponse=null;
        httpResponse=client.execute(httpGet);
        String responseString = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
        Document d= Jsoup.parse(responseString);
        String token=d.select("#new_new_user > input").get(1).attr("value");
        System.out.println(token);
        HttpPost httpPost=new HttpPost("http://**/users/sign_in");
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
        formparams.add(new BasicNameValuePair("authenticity_token", token));
        formparams.add(new BasicNameValuePair("user[login]", "**@**.com"));
        formparams.add(new BasicNameValuePair("user[password]", "***"));
        formparams.add(new BasicNameValuePair("user[remember_me]", "0"));
        formparams.add(new BasicNameValuePair("utf8", "✓"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        httpPost.setEntity(entity);
        httpResponse=client.execute(httpPost);
        printResponse(httpResponse);
        System.out.println(httpResponse.getStatusLine().getStatusCode());
        Header header=httpResponse.getFirstHeader("Set-Cookie");
        HttpGet main=new HttpGet("http://202.196.37.147:18080/");
        main.setHeader(header);
        httpResponse=client.execute(main);
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
//        System.out.println(responseString);
//        Map<String,String > params=new HashMap<String, String>();
    }

    @Test
    public void loginGitHub(){
        HttpClient client=HttpClients.createDefault();
//        CloseableHttpClient client=HttpClients.createDefault();
        HttpGet get=new HttpGet("https://github.com/login");
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        get.setConfig(defaultConfig);
//        CloseableHttpResponse response=null;
        HttpResponse response=null;
        try {
            response=client.execute(get);
//            printResponse(response);
            String responseString = EntityUtils.toString(response.getEntity(),"UTF-8");
            Document d= Jsoup.parse(responseString);
            String token=d.select("form > input").get(1).attr("value");
            System.out.println(token);
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("authenticity_token", token));
            params.add(new BasicNameValuePair("commit", "Sign+in"));
            params.add(new BasicNameValuePair("login", "691778949@qq.com"));
            params.add(new BasicNameValuePair("password", "shijin1221.nb"));
            params.add(new BasicNameValuePair("utf8", "✓"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
            HttpPost post=new HttpPost("https://github.com/session");
            post.setConfig(defaultConfig);
            post.setEntity(entity);
            response=client.execute(post);
            printResponse(response);
            Header header=response.getFirstHeader("Set-Cookie");
            HttpGet main=new HttpGet("https://github.com/");
            main.setConfig(defaultConfig);
            main.setHeader(header);
            response=client.execute(main);
            System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }















    public static void printResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        System.out.println("status:" + httpResponse.getStatusLine());
        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            System.out.println("response length:" + responseString.length());
            System.out.println("response content:"
                    + responseString.replace("\r\n", ""));
        }
    }
}

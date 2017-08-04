package cn.csg;

/**
 * Created by DoutzenShum on 2017/8/4.
 */
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by gavin on 15-7-23.
 */
public class Ggget {


    public static void main(String[] args) {
        //创建一个HttpClient
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        try {
            //构造post数据
            List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
            valuePairs.add(new BasicNameValuePair("user_name", "nfdw@163.com"));
            valuePairs.add(new BasicNameValuePair("password", "931be298a1f46b07c924070442ec559f"));
            valuePairs.add(new BasicNameValuePair("client_id", "04H3Di8vF5eraAW0o6cT4D1W"));
            valuePairs.add(new BasicNameValuePair("client_secret", "1UPx1afIu4EG9i40dSpxjKgX"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
            //创建一个post请求
            HttpPost post = new HttpPost("https://antfact.com/auth2/authc/login");
            //注入post数据
            post.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(post);
            //打印登录是否成功信息
            String jsonString =  printResponse(httpResponse);

            //From JsonTest
            JSONArray jsonArray = JSONArray.fromObject("["+jsonString+"]");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject token = (JSONObject) jsonObject.get("token");
            String realToken = token.get("access_token").toString();
//            System.out.println(token.get("access_token"));
            System.out.println(realToken);


            //构造一个get请求，用来测试登录cookie是否拿到
            HttpGet g = new HttpGet("https://antfact.com/eageye/v4/document/swift?eventId=e6d35e90-618b-4ab4-953c-85a7722679c0&pageSize=10&optionParams=%7B%22isRelative%22:false%7D&from=monitorColumn_ordinary");

            g.setHeader("authorization", "oauth2 "+realToken);

            CloseableHttpResponse r = httpClient.execute(g);
            String content = EntityUtils.toString(r.getEntity());
            System.out.println(content);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //返回含有token（未处理）的json（String）
    public static String printResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
//        System.out.println("status:" + httpResponse.getStatusLine());
//        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        /*while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }*/
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
//            System.out.println("response length:" + responseString.length());
            System.out.println("response content:"
                    + responseString.replace("\r\n", ""));
            String json= responseString.replace("\r\n", "");
            return json;
        }
        return null;
    }

    public static Map<String, String> cookieMap = new HashMap<String, String>(64);

    //从响应信息中获取cookie
    public static String setCookie(HttpResponse httpResponse) {
        System.out.println("----setCookieStore");
        Header headers[] = httpResponse.getHeaders("Set-Cookie");
        if (headers == null || headers.length == 0) {
            System.out.println("----there are no cookies");
            return null;
        }
        String cookie = "";
        for (int i = 0; i < headers.length; i++) {
            cookie += headers[i].getValue();
            if (i != headers.length - 1) {
                cookie += ";";
            }
        }

        String cookies[] = cookie.split(";");
        for (String c : cookies) {
            c = c.trim();
            if (cookieMap.containsKey(c.split("=")[0])) {
                cookieMap.remove(c.split("=")[0]);
            }
            cookieMap.put(c.split("=")[0], c.split("=").length == 1 ? "" : (c.split("=").length == 2 ? c.split("=")[1] : c.split("=", 2)[1]));
        }
        System.out.println("----setCookieStore success");
        String cookiesTmp = "";
        for (String key : cookieMap.keySet()) {
            cookiesTmp += key + "=" + cookieMap.get(key) + ";";
        }

        return cookiesTmp.substring(0, cookiesTmp.length() - 2);
    }
}

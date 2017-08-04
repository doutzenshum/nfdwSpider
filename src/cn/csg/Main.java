package cn.csg;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        CloseableHttpClient closeHttpClient = HttpClients.createDefault();  
        CloseableHttpResponse httpResponse = null;  
          
        HttpPost httpPost = new HttpPost("https://antfact.com/auth2/authc/login");
          
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("user_name", "nfdw@163.com"));  
        params.add(new BasicNameValuePair("password", "931be298a1f46b07c924070442ec559f"));  
        params.add(new BasicNameValuePair("client_id", "04H3Di8vF5eraAW0o6cT4D1W"));  
        params.add(new BasicNameValuePair("client_secret", "1UPx1afIu4EG9i40dSpxjKgX"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
              
            httpResponse = closeHttpClient.execute(httpPost);
              
            System.out.println(httpResponse.getStatusLine());  
              
            HttpEntity httpEntity = httpResponse.getEntity();  
            if(httpEntity != null){  
                  
                InputStream is = httpEntity.getContent();  
                  
                BufferedReader br = new BufferedReader(new InputStreamReader(is,Consts.UTF_8));  
                String line = null;  
                while((line=br.readLine())!=null){  
                    System.out.println(line);  
                } 
                is.close();  
            }  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            if(httpResponse != null){  
                try {  
                    httpResponse.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if(closeHttpClient != null){  
                try {  
                    closeHttpClient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }
        

    }

}

package com.lvwang.osf.service;

import com.lvwang.osf.model.HttpResult;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiService implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Autowired(required = false)
    private RequestConfig requestConfig;

    /**
     * 执行GET请求，响应200返回内容，404返回null
     * @param url
     * @return
     * @throws IOException
     */
    public String doGet(String url) throws IOException {
        //创建Http Get请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        //执行请求
        try {
            response = getHttpClient().execute(httpGet);
            //判断返回状态
            if(response.getStatusLine().getStatusCode() == 200){
                return EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        } finally {
            if (response != null){
                response.close();
            }
        }
        return null;
    }

    /**
     * 带有参数的GET请求，响应200返回内容，404返回null
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String doGet(String url, Map<String,String> params) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder(url);
        for (Map.Entry<String,String> entry : params.entrySet()){
            builder.setParameter(entry.getKey(),entry.getValue());
        }
        return doGet(builder.build().toString());
    }

    /**
     * 执行post请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String,String> params) throws IOException {
        //创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if(params != null){
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String,String> entry : params.entrySet()){
                parameters.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
            //构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters,"UTF-8");
            //将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }

        return getHttpResult(httpPost);
    }

    /**
     * 执行post请求
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public HttpResult doPostPost(String url, String json) throws IOException {
        //创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if(json != null){
            //构造一个form表单式的实体
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            //将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }

        return getHttpResult(httpPost);
    }

    /**
     * 上传文件
     * @param url
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public HttpResult upload(String url,MultipartFile multipartFile) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (multipartFile != null) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file"
                    , multipartFile.getInputStream()
                    , ContentType.MULTIPART_FORM_DATA
                    , multipartFile.getOriginalFilename());
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
        }
        return getHttpResult(httpPost);
    }

    /**
     * 上传文件
     * @param url
     * @param bytes
     * @return
     */
    public HttpResult upload(String url,byte[] bytes,String fileName) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (bytes != null) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file"
                    , bytes
                    , ContentType.MULTIPART_FORM_DATA
                    , fileName);

            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
        }
        return getHttpResult(httpPost);
    }

    private HttpResult getHttpResult(HttpPost httpPost) throws IOException{
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpPost);
            return new HttpResult(response.getStatusLine().getStatusCode()
                    , EntityUtils.toString(response.getEntity(),"UTF-8"));
        } finally {
            if (response != null){
                response.close();
            }
        }
    }

    private CloseableHttpClient getHttpClient(){
        return beanFactory.getBean(CloseableHttpClient.class);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}

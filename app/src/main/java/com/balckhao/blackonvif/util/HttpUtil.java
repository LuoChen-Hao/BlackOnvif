package com.balckhao.blackonvif.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author ： BlackHao
 * Time : 2017/1/9 14:20
 * Description : 网络请求类
 */

public class HttpUtil {

    /**
     * POST 请求
     */
    public static String postRequest(String baseUrl, String params) throws Exception {
        String receive = "";
        // 新建一个URL对象
        URL url = new URL(baseUrl);
        // 打开一个HttpURLConnection连接
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        //设置请求允许输入 默认是true
        urlConn.setDoInput(true);
        // Post请求必须设置允许输出 默认false
        urlConn.setDoOutput(true);
        // 设置为Post请求
        urlConn.setRequestMethod("POST");
        // Post请求不能使用缓存
        urlConn.setUseCaches(false);
        //设置本次连接是否自动处理重定向
        urlConn.setInstanceFollowRedirects(true);
        // 配置请求Content-Type,application/soap+xml
        urlConn.setRequestProperty("Content-Type", "application/soap+xml");
        // 开始连接
        urlConn.connect();
        // 发送请求数据
        urlConn.getOutputStream().write(params.getBytes());
        // 判断请求是否成功
        if (urlConn.getResponseCode() == 200) {
            // 获取返回的数据
            InputStream is = urlConn.getInputStream();
            byte[] data = new byte[1024];
            int n;
            while ((n = is.read(data)) != -1) {
                receive = receive + new String(data, 0, n);
            }
        } else {
            throw new Exception("ResponseCodeError : " + urlConn.getResponseCode());
        }
        // 关闭连接
        urlConn.disconnect();
        return receive;
    }
}

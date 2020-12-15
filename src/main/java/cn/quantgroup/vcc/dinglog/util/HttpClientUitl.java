package cn.quantgroup.vcc.dinglog.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

/**
 * @Describe:
 * @Created by tangfeng 2020-07-08 17:22
 */
public class HttpClientUitl {

    private static Logger log = LoggerFactory.getLogger(HttpClientUitl.class);

    private static int MAX_FAIL_RETRY_COUNT = 3;
    private static HttpClient httpClient;


    public static String postJSON(String url, String json) throws IOException {
        InputStream inStream = null;
        HttpEntity entity = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json, Consts.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            entity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                inStream = entity.getContent();
                return IOUtils.toString(inStream);
            } else {
                throw new IOException("Unexpected code " + httpResponse);
            }
        } finally {
            EntityUtils.consume(entity);
            if (inStream != null) {
                inStream.close();
            }
        }
    }

    public static String doGet(String url) throws IOException {

        HttpEntity entity = null;
        InputStream inStream = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse execute = httpClient.execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == 200) {
                entity = execute.getEntity();
                inStream = entity.getContent();
                return IOUtils.toString(entity.getContent());
            }
        } finally {
            EntityUtils.consume(entity);
            if (inStream != null) {
                inStream.close();
            }
        }
        return null;
    }


    //优化并发场景 HttpClient 单线程问题
    static {
        try {
            // 1. 根据证书来调用
            SSLContext sslContext = sslContent(null, null);
            SSLConnectionSocketFactory sslConnectionSocketFactory = null;
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);


            //存活的策略
            ConnectionKeepAliveStrategy myStrategy = ka();

            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslConnectionSocketFactory == null ? PlainConnectionSocketFactory.INSTANCE : sslConnectionSocketFactory)
                    .build();

            //创建ConnectionManager，添加Connection配置信息
            //最大连接数
            //例如默认每路由最高50并发，具体依据业务来定,一般和setMaxTotal 一致
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(200);
            connectionManager.setDefaultMaxPerRoute(200);
            //检测有效连接的间隔 2s
            connectionManager.setValidateAfterInactivity(2000);

            RequestConfig requestConfig = RequestConfig.custom()
                    //.setConnectionRequestTimeout(6000)//设定连接服务器超时时间
                    //.setConnectTimeout(2000)//设定从连接池获取可用连接的时间
                    //.setSocketTimeout(6000)//设定获取数据的超时时间
                    .build();

            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setBufferSize(4128)
                    .build();

            httpClient = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setKeepAliveStrategy(myStrategy)
                    .setDefaultRequestConfig(requestConfig)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier())
                    //不使用这种方式，不方便看日志，使用下面自定义的retry
                    //.setRetryHandler(new DefaultHttpRequestRetryHandler(3,true))
                    .setRetryHandler(new MyRetryHandler())
                    .setDefaultConnectionConfig(connectionConfig)
                    .build();

            log.info("注册https证书成功");
        } catch (Exception e) {
            log.error("注册https证书失败,e:{}", e);
        }
    }

    /**
     * 请求重试处理器
     */
    private static class MyRetryHandler implements HttpRequestRetryHandler {

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            String uri = request.getRequestLine().getUri();

            if (executionCount >= MAX_FAIL_RETRY_COUNT) {
                log.warn("{}-{}重试次数大于等于3次", uri, executionCount);
                return false;
            }

            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // 如果请求被认为是幂等的，则重试
                log.warn("幂等接口重试：{},次数：{}", uri, executionCount);
                return true;
            }

            //NoHttpResponseException 重试
            if (exception instanceof NoHttpResponseException) {
                log.warn("NoHttpResponseException 异常重试，接口：{},次数：{} ", uri, executionCount);
                return true;
            }

            //连接超时重试
            if (exception instanceof ConnectTimeoutException) {
                log.warn("ConnectTimeoutException异常重试 ，接口：{},次数：{} ", uri, executionCount);
                return true;
            }

            // 响应超时不重试，避免造成业务数据不一致
            if (exception instanceof SocketTimeoutException) {
                return false;
            }

            if (exception instanceof InterruptedIOException) {
                // 超时
                return false;
            }
            if (exception instanceof UnknownHostException) {
                // 未知主机
                return false;
            }

            if (exception instanceof SSLException) {
                // SSL handshake exception
                return false;
            }

            return false;
        }
    }

    private static ConnectionKeepAliveStrategy ka() {

        //就是通过Keep-Alive头信息中，获得timeout的值，作为超时时间；单位毫秒；
        //如请求头中 Keep-Alive: timeout=5, max=100
        //DefaultConnectionKeepAliveStrategy strategy = DefaultConnectionKeepAliveStrategy.INSTANCE;
        ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                HeaderElementIterator it = new BasicHeaderElementIterator
                        (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        return Long.parseLong(value) * 1000;
                    }
                }
                //如果没有约定，则默认定义时长为60s
                return 60 * 1000;
            }
        };
        return myStrategy;
    }

    private static SSLContext sslContent(String path, String pwd) throws Exception {

        SSLContext sslcontext = null;
        InputStream instream = null;
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance("jks");
            /*instream = new ClassPathResource("config/release/test.jks").getInputStream();
            trustStore.load(instream, "123456".toCharArray());*/
            if (StringUtils.isEmpty(path) || StringUtils.isEmpty(pwd)) {
                return null;
            }

            instream = new ClassPathResource(path).getInputStream();
            trustStore.load(instream, pwd.toCharArray());

            // 相信自己的CA和所有自签名的证书
            TrustStrategy ts = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true;
                }
            };
            sslcontext = SSLContexts.custom()
                    //.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                    .loadTrustMaterial(trustStore, ts)
                    .build();
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
            }
        }

        return sslcontext;
    }


}

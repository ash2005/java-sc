package io.loli.sc.api;

import io.loli.sc.Config;
import io.loli.util.MD5Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class ImageCloudAPI extends APITools implements API {

    private Config config;

    public ImageCloudAPI() {
    }

    public ImageCloudAPI(Config config) {
        this.config = config;
    }

    @Override
    public String upload(File fileToUpload) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost hp = new HttpPost(UPLOAD_URL);
        CloseableHttpResponse response = null;
        String result = null;

        String token = config.getImageCloudConfig().getToken();
        String email = config.getImageCloudConfig().getEmail();

        try {
            MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder
                    .create();
            multiPartEntityBuilder
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            // 可以直接addBinary
            multiPartEntityBuilder.addPart("image", new FileBody(fileToUpload));
            // 可以直接addText
            multiPartEntityBuilder.addPart("token", new StringBody(token,
                    ContentType.create("text/plain", Consts.UTF_8)));
            multiPartEntityBuilder.addPart("email", new StringBody(email,
                    ContentType.create("text/plain", Consts.UTF_8)));
            multiPartEntityBuilder.addPart(
                    "desc",
                    new StringBody(fileToUpload.getName(), ContentType.create(
                            "text/plain", Consts.UTF_8)));

            hp.setEntity(multiPartEntityBuilder.build());
            response = httpclient.execute(hp);
            result = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        UploadedImage img = null;
        try {
            img = mapper.readValue(result, UploadedImage.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BASE_URL + img.getPath();
    }

    public static class ClientToken {
        public ClientToken() {
        }

        private int id;
        private User user;
        private String token;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class User {
        public User() {
        }

        private int id;
        private String email;
        private Date regDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Date getRegDate() {
            return regDate;
        }

        public void setRegDate(Date regDate) {
            this.regDate = regDate;
        }

    }

    public static class UploadedImage {
        private int id;

        private User user;

        private Date date;
        /**
         * 图片描述显示在alt标签中
         */
        private String description;
        private String path;
        /**
         * 原始名字显示在title标签中
         */
        private String originName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getDesc() {
            return description;
        }

        public void setDesc(String desc) {
            this.description = desc;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getOriginName() {
            return originName;
        }

        public void setOriginName(String originName) {
            this.originName = originName;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private ClientToken token;

    public ClientToken getToken() {
        return token;
    }

    private static final String BASE_URL = "http://loli.io:8080/sc-server/";
    private static final String TOKEN_URL = BASE_URL + "api/token";
    private static final String UPLOAD_URL = BASE_URL + "api/upload";

    @Override
    public void auth() {
        String email = JOptionPane.showInputDialog("请输入登录邮箱");
        String password = null;
        // TODO 对邮箱地址进行验证
        if (null != email && !"".equals(email.trim())) {
            password = PasswordBox.showInputDialog("请输入密码(本应用不会保存你的密码)");
            if (null != password && !"".equals(password.trim())) {

            } else {
                JOptionPane.showMessageDialog(null, "你没有输入密码");
            }
        } else {
            JOptionPane.showMessageDialog(null, "你没有输入邮箱");
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.addAll(Arrays.asList(new NameValuePair[] {
                new BasicNameValuePair("email", email),
                new BasicNameValuePair("password", MD5Util.hash(password)) }));
        String result = post(TOKEN_URL, params);
        
        ObjectMapper mapper = new ObjectMapper();
        token = null;
        try {
            token = mapper.readValue(result, ClientToken.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public class PasswordBox {

        static JPasswordField passwd = new JPasswordField(10);

        static public String showInputDialog(String s) {
            JOptionPane localJOptionPane = new JOptionPane(s,
                    JOptionPane.QUESTION_MESSAGE);
            localJOptionPane.add(passwd);
            passwd.setEchoChar('*');
            JDialog localJDialog = localJOptionPane.createDialog(
                    localJOptionPane, "Input");
            localJDialog.setVisible(true);
            String localObject = String.valueOf(passwd.getPassword());
            localJDialog.dispose();
            return localObject;
        }
    }

}

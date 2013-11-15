package io.loli.sc.api;

import io.loli.sc.Config;
import io.loli.sc.ScreenCaptor;
import io.loli.sc.api.GDriveAuth.CodeExchangeException;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.api.client.auth.oauth2.Credential;

public class GDriveAPI extends APITools implements API {

    private final static String CLIENT_ID = "843116795212.apps.googleusercontent.com";

    private final static String CLIENT_SECRET = "7dtggnvbXOVsV0GV0N3FieXp";

    private final static String REDIRECT_URL = "urn:ietf:wg:oauth:2.0:oob";
    private final static String AUTH = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id="
            + CLIENT_ID
            + "&redirect_uri="
            + REDIRECT_URL
            + "&scope=https://www.googleapis.com/auth/drive.file%20https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile&approval_prompt=auto";

    private static final String PIN_TO_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    
    @Override
    public String upload(File fileToUpload) {
        com.google.api.services.drive.model.File f = null;
        try {
            f = GDriveUpload.uploadFile(fileToUpload,
                    null);
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        f.setShared(true);
        return f.getDownloadUrl();
    }

    private String code;

    @Override
    public void auth() {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI(AUTH));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public AccessToken pinToToken(String pin) {
        Credential c = null;
        try {
            c = GDriveAuth.exchangeCode(pin);
        } catch (CodeExchangeException e) {
            e.printStackTrace();
        }
        AccessToken token = new AccessToken();
        token.setAccess_token(c.getAccessToken());
        token.setExpires_in(c.getExpiresInSeconds());
        token.setRefresh_token(c.getRefreshToken());
        return token;
    }

    public NewAccessToken refreshToken(String refreshToken) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.addAll(Arrays.asList(new NameValuePair[] {
                new BasicNameValuePair("refresh_token", refreshToken),
                new BasicNameValuePair("client_id", CLIENT_ID),
                new BasicNameValuePair("client_secret", CLIENT_SECRET),
                new BasicNameValuePair("grant_type", "refresh_token") }));
        ObjectMapper mapper = new ObjectMapper();
        NewAccessToken token = null;
        try {
            token = mapper.readValue(post(PIN_TO_TOKEN_URL, params),
                    NewAccessToken.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    public String getCode() {
        return code;
    }

    /**
     * post上传文件
     */
    private String postFile(String postUrl, File imgFileToUpload,
            String accessToken) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost hp = new HttpPost(postUrl);
        hp.addHeader("Authorization", "Bearer " + accessToken);
        hp.addHeader("Content-Type", "image/png");
        CloseableHttpResponse response = null;
        String result = null;
        try {
            MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder
                    .create();
            multiPartEntityBuilder.addBinaryBody("image", imgFileToUpload);

            hp.setEntity(multiPartEntityBuilder.build());
            response = httpclient.execute(hp);
            result = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws HeadlessException,
            CodeExchangeException, IOException {
        GDriveAPI g = new GDriveAPI();
        g.auth();
        ScreenCaptor sc = ScreenCaptor.newInstance();
        sc.setConfig(new Config());
        com.google.api.services.drive.model.File f = GDriveUpload.uploadFile(
                sc.screenShotSave(),
                GDriveAuth.exchangeCode(JOptionPane.showInputDialog("")));
        System.out.println(f.getDownloadUrl());
    }

    public static class AccessToken {
        private String access_token;
        private long expires_in;
        private String refresh_token;

        public String getAccess_token() {
            return access_token;
        }

        public void setExpires_in(long expires_in) {
            this.expires_in = expires_in;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public long getExpires_in() {
            return expires_in;
        }
    }

    public static class NewAccessToken {
        private String Access_token;
        private int Expires_in;
        private String Token_type;

        public String getAccess_token() {
            return Access_token;
        }

        public void setAccess_token(String access_token) {
            Access_token = access_token;
        }

        public int getExpires_in() {
            return Expires_in;
        }

        public void setExpires_in(int expires_in) {
            Expires_in = expires_in;
        }

        public String getToken_type() {
            return Token_type;
        }

        public void setToken_type(String token_type) {
            Token_type = token_type;
        }

    }
}

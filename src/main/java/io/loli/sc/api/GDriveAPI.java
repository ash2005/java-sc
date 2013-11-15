package io.loli.sc.api;

import io.loli.sc.api.ImgurAPI.AccessToken;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        // TODO Auto-generated method stub
        return null;
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

    private AccessToken pinToToken(String pin) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.addAll(Arrays.asList(new NameValuePair[] {
                new BasicNameValuePair("client_id", CLIENT_ID),
                new BasicNameValuePair("client_secret", CLIENT_SECRET),
                new BasicNameValuePair("grant_type", "authorization_code"),
                new BasicNameValuePair("redirect_uri",
                        "urn:ietf:wg:oauth:2.0:oob"),
                new BasicNameValuePair("code", pin) }));
        String result = post(PIN_TO_TOKEN_URL, params);
        ObjectMapper mapper = new ObjectMapper();
        AccessToken token = null;
        try {
            token = mapper.readValue(result, AccessToken.class);
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

    public static void main(String[] args) {
        GDriveAPI g = new GDriveAPI();
        // g.auth();
        System.out
                .println(g
                        .pinToToken("4/oVBqDg7tip5LTFab_4diflW2DGqG.YrahaPEEcPQbXE-sT2ZLcbQJndiuhAI"));
    }

    @SuppressWarnings("unused")
    private static class AccessToken {
        private String access_token;
        private String token_type;
        private int expires_in;
        private String id_token;
        private String refresh_token;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getId_token() {
            return id_token;
        }

        public void setId_token(String id_token) {
            this.id_token = id_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }
    }
}

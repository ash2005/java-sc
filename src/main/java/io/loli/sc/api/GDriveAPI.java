package io.loli.sc.api;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GDriveAPI implements API {

    private final static String CLIENT_ID = "843116795212.apps.googleusercontent.com";

    private final static String CLIENT_SECRET = "7dtggnvbXOVsV0GV0N3FieXp";

    private final static String REDIRECT_URL = "http://localhost:18431/gDriveAuth";
    private final static String AUTH = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id="
            + CLIENT_ID
            + "&redirect_uri="
            + REDIRECT_URL
            + "&scope=https://www.googleapis.com/auth/drive.file%20https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile&approval_prompt=auto";

    @Override
    public String upload(File fileToUpload) {
        // TODO Auto-generated method stub
        return null;
    }

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

    public static void main(String[] args) {
        new GDriveAPI().auth();
    }

}

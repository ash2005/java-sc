package io.loli.sc.api;

/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

/**
 * A sample application that runs multiple requests against the Drive API. The
 * requests this sample makes are:
 * <ul>
 * <li>Does a resumable media upload</li>
 * <li>Updates the uploaded file by renaming it</li>
 * <li>Does a resumable media download</li>
 * <li>Does a direct media upload</li>
 * <li>Does a direct media download</li>
 * </ul>
 * 
 * @author rmistry@google.com (Ravi Mistry)
 */
public class GDriveUpload {

    private static final String DIR_FOR_DOWNLOADS = "Enter Download Directory";

    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /** Uploads a file using either resumable or direct media upload. */
    public static File uploadFile(java.io.File file, Credential credential)
            throws IOException {
        File fileMetadata = new File();
        fileMetadata.setTitle(file.getName());

        InputStreamContent mediaContent = new InputStreamContent("image/png",
                new BufferedInputStream(new FileInputStream(file)));
        mediaContent.setLength(file.length());
        Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                credential).setApplicationName("Google-DriveSample/1.0")
                .build();
        Drive.Files.Insert insert = drive.files().insert(fileMetadata,
                mediaContent);
        MediaHttpUploader uploader = insert.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(true);
        return insert.execute();
    }
}
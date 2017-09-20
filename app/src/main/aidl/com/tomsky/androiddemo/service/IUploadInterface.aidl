// IUploadInterface.aidl
package com.tomsky.androiddemo.service;

import android.os.Bundle;
import com.tomsky.androiddemo.service.IUploadCallback;

// Declare any non-default types here with import statements

interface IUploadInterface {

    void startUpload(in Bundle bundle);

    void registerCallback(in IUploadCallback callback);
}

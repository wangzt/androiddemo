// IUploadCallback.aidl
package com.tomsky.androiddemo.service;

import android.os.Bundle;

// Declare any non-default types here with import statements

interface IUploadCallback {
    void uploadCallback(inout Bundle bundle);
}

//
// Created by wangzhitao on 2/17/16.
//

/* 头文件begin */
#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <android/log.h>
#include <unistd.h>
#include <sys/inotify.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>

/* 头文件end */

/* 宏定义begin */
//清0宏
#define MEM_ZERO(pDest, destSize) memset(pDest, 0, destSize)

//LOG宏定义
#define LOG_TAG "wzt-sec"
#define LOG_INFO(tag, msg) __android_log_print(ANDROID_LOG_INFO, tag, msg)
#define LOG_DEBUG(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOG_WARN(tag, msg) __android_log_print(ANDROID_LOG_WARN, tag, msg)
#define LOG_ERROR(tag, msg) __android_log_print(ANDROID_LOG_ERROR, tag, msg)
/* 宏定义end */

#ifndef _Included_com_huajiao_utils_Security
#define _Included_com_huajiao_utils_Security
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL Java_com_huajiao_utils_Security_init(JNIEnv *, jobject, jobject, jobject, jstring);

JNIEXPORT jstring JNICALL Java_com_huajiao_utils_Security_convertKey(JNIEnv *, jobject, jstring);

JNIEXPORT jstring JNICALL Java_com_huajiao_utils_Security_decode(JNIEnv *, jclass, jstring, jstring);

#ifdef __cplusplus
}
#endif
#endif

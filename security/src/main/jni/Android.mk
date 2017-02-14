LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_LDLIBS :=-llog
LOCAL_MODULE    := hjsecurity
LOCAL_SRC_FILES := md5/md5.c md5/md5_dgst.c md5/md5_one.c mem_clr.c security.c
include $(BUILD_SHARED_LIBRARY)

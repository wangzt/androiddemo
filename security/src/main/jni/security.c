//
// Created by wangzhitao on 2/17/16.
//

#include "security.h"
#include "openssl/md5.h"

#ifdef __cplusplus
extern "C" {
#endif

static char c_TAG[] = "Security";
static char key1[] = "mfwwdqyjkozihv2naqebb0adsw7wsajq";
static char key2[] = "3082030d308aqebbqadswawsa1dkikws";
static char key3[] = "mfwwdqyjkozihvcnaqebbqadswawsajb";
static char key4[] = "eac63e66d8c4a6f0303f00bc76d0217c";
static char key5[] = "DPF5HCBTSJXZVKQX";
static char key6[] = "ZThhNzM0NGY0ZTQ3";

jstring byteArrayToHex(JNIEnv* env, const char* byteArray, int length)
{
    char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f' };
    char resultCharArray[2*length+1];
    int index = 0;
    int i = 0;
    for (i = 0; i < length; i ++) {
        resultCharArray[index++] = hexDigits[byteArray[i]>>4&0xf];
        resultCharArray[index++] = hexDigits[byteArray[i]&0xf];
    }
    resultCharArray[2*length] = '\0';
    jstring ret = (*env)->NewStringUTF(env, resultCharArray);
    return ret;
}

jstring strToJstring(JNIEnv* env, const char* pStr)
{
    int strLen = strlen(pStr);
    jclass jstrObj = (*env)->FindClass(env, "java/lang/String");
    jmethodID methodId = (*env)->GetMethodID(env, jstrObj, "<init>", "([BLjava/lang/String;)V");
    jbyteArray byteArray = (*env)->NewByteArray(env, strLen);
    jstring encode = (*env)->NewStringUTF(env, "utf-8");
    (*env)->SetByteArrayRegion(env, byteArray, 0, strLen, (jbyte*)pStr);
    return (jstring)(*env)->NewObject(env, jstrObj, methodId, byteArray, encode);
}

void myStrcat(char source[], char dest[], int begin, int length)
{
    int i = begin;
    int j = 0;
    for (; j < length; i ++, j ++)
    {
        dest[i] = source[j];
    }
}

void myStrcat2(const char *source, char dest[], int begin, int length)
{
    int i = begin;
    int j = 0;
    for (; j < length; i ++, j ++)
    {
        dest[i] = source[j];
    }
}

jstring infoFiled(JNIEnv *env, jobject input, jclass info_clazz,  const char* filed)
{
    jfieldID device_fId = (*env)->GetFieldID(env, info_clazz, filed, "Ljava/lang/String;");
    jstring deviceId = (jstring)(*env)->GetObjectField(env, input, device_fId);
    return deviceId;

}

JNIEXPORT jstring JNICALL Java_com_huajiao_utils_Security_init(JNIEnv *env, jobject obj, jobject context, jobject input, jstring sec)
{
    jclass info_clazz = (*env)->GetObjectClass(env, input);

    jstring array[14];
    int size[14];

    array[0] = (*env)->NewStringUTF(env, "deviceid=");
    array[1] = infoFiled(env, input, info_clazz, "deviceId");
    size[0] = (*env)->GetStringLength(env, array[0]);
    size[1] = (*env)->GetStringLength(env, array[1]);

    array[2] = (*env)->NewStringUTF(env, "netspeed=1024");
    size[2] = (*env)->GetStringLength(env, array[2]);

    array[3] = (*env)->NewStringUTF(env, "network=");
    array[4] = infoFiled(env, input, info_clazz, "network");
    size[3] = (*env)->GetStringLength(env, array[3]);
    size[4] = (*env)->GetStringLength(env, array[4]);

    array[5] = (*env)->NewStringUTF(env, "platform=android");
    size[5] = (*env)->GetStringLength(env, array[5]);

    array[6] = (*env)->NewStringUTF(env, "rand=");
    array[7] = infoFiled(env, input, info_clazz, "rand");
    size[6] = (*env)->GetStringLength(env, array[6]);
    size[7] = (*env)->GetStringLength(env, array[7]);

    array[8] = (*env)->NewStringUTF(env, "time=");
    array[9] = infoFiled(env, input, info_clazz, "time");
    size[8] = (*env)->GetStringLength(env, array[8]);
    size[9] = (*env)->GetStringLength(env, array[9]);

    array[10] = (*env)->NewStringUTF(env, "userid=");
    array[11] = infoFiled(env, input, info_clazz, "userid");
    size[10] = (*env)->GetStringLength(env, array[10]);
    size[11] = (*env)->GetStringLength(env, array[11]);

    array[12] = (*env)->NewStringUTF(env, "version=");
    array[13] = infoFiled(env, input, info_clazz, "version");
    size[12] = (*env)->GetStringLength(env, array[12]);
    size[13] = (*env)->GetStringLength(env, array[13]);

    int length = 0;
    int j;
    for (j = 0; j < 14; j++) {
        length += size[j];
    }

    int lengthEnd = strlen(key4);
    length = length + lengthEnd;

    char sb[length+1];
    int prefixLength = 0;
    int i;
    for (i = 0; i < 14; i++) {
        const char *strChar = (*env)->GetStringUTFChars(env, array[i], NULL);
        myStrcat2(strChar, sb, prefixLength, size[i]);
        prefixLength += size[i];
        (*env)->ReleaseStringUTFChars(env, array[i], strChar);
    }

    myStrcat(key4, sb, prefixLength, lengthEnd);
    sb[length] = '\0';

    jstring str = (*env)->NewStringUTF(env, sb);
    int totalSize = (*env)->GetStringLength(env, str);
    const char *nativeString = (*env)->GetStringUTFChars(env, str, NULL);

    unsigned char result[16];
    MD5(nativeString, totalSize, result);
    (*env)->ReleaseStringUTFChars(env, str, nativeString);
    return byteArrayToHex(env, result, 16);

    /*int size = (*env)->GetStringLength(env, input);
    const char *nativeString = (*env)->GetStringUTFChars(env, input, NULL);
    unsigned char result[16];
    MD5(nativeString, size, result);
    (*env)->ReleaseStringUTFChars(env, input, nativeString);
    return byteArrayToHex(env, result, 16);*/

}

JNIEXPORT jstring JNICALL Java_com_huajiao_utils_Security_convertKey(JNIEnv *env, jobject obj, jstring input)
{
    return (*env)->NewStringUTF(env, key5);
}

JNIEXPORT jstring JNICALL Java_com_huajiao_utils_Security_decode(JNIEnv *env, jclass clazz, jstring in, jstring k) {

    const char *nativeString = (*env)->GetStringUTFChars(env, k, NULL);
    int totalSize = (*env)->GetStringLength(env, k);
    unsigned char result[16];
    MD5(nativeString, totalSize, result);
    (*env)->ReleaseStringUTFChars(env, k, nativeString);
    jstring key = byteArrayToHex(env, result, 8);

    // 常量字符串
    jstring ascii = (*env)->NewStringUTF(env, "ASCII");
    jstring aes = (*env)->NewStringUTF(env, "AES");
    jstring aes_ecb_no_padding = (*env)->NewStringUTF(env, "AES/ECB/NoPadding");
    jstring utf8 = (*env)->NewStringUTF(env, "UTF-8");
    //jstring regular = (*env)->NewStringUTF(env, "[\\s*\t\n\r]");
    //jstring replace = (*env)->NewStringUTF(env, "");

    // String
    jclass String = (*env)->FindClass(env, "java/lang/String");
    jmethodID String_init = (*env)->GetMethodID(env, String, "<init>", "([B)V");
    jmethodID String_getBytes = (*env)->GetMethodID(env, String, "getBytes",
                                                    "(Ljava/lang/String;)[B");
    //jmethodID String_replaceAll = (*env)->GetMethodID(env, String, "replaceAll", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    jmethodID String_trim = (*env)->GetMethodID(env, String, "trim", "()Ljava/lang/String;");

    // SecretKeySpec
    jclass SecretKeySpec = (*env)->FindClass(env, "javax/crypto/spec/SecretKeySpec");
    jmethodID SecretKeySpec_init = (*env)->GetMethodID(env, SecretKeySpec, "<init>",
                                                       "([BLjava/lang/String;)V");

    // Cipher
    jclass Cipher = (*env)->FindClass(env, "javax/crypto/Cipher");
    jmethodID Cipher_getInstance = (*env)->GetStaticMethodID(env, Cipher, "getInstance",
                                                             "(Ljava/lang/String;)Ljavax/crypto/Cipher;");
    jmethodID Cipher_init = (*env)->GetMethodID(env, Cipher, "init", "(ILjava/security/Key;)V");
    jmethodID Cipher_doFinal = (*env)->GetMethodID(env, Cipher, "doFinal", "([B)[B");
    jint Cipher_DECRYPT_MODE = (*env)->GetStaticIntField(env, Cipher,
                                                         (*env)->GetStaticFieldID(env, Cipher,
                                                                                  "DECRYPT_MODE",
                                                                                  "I"));

    // Base64
    jclass Base64 = (*env)->FindClass(env, "com/huajiao/utils/Base64");
    jmethodID Base64_decode = (*env)->GetStaticMethodID(env, Base64, "decode",
                                                        "(Ljava/lang/String;)[B");

    // 算法实现
    jbyteArray keyBytes = (*env)->CallObjectMethod(env, key, String_getBytes, ascii);
    jobject keySpec = (*env)->NewObject(env, SecretKeySpec, SecretKeySpec_init, keyBytes, aes);
    jobject cipher = (*env)->CallStaticObjectMethod(env, Cipher, Cipher_getInstance,
                                                    aes_ecb_no_padding);
    (*env)->CallVoidMethod(env, cipher, Cipher_init, Cipher_DECRYPT_MODE, keySpec);
    jbyteArray inBytes = (*env)->CallStaticObjectMethod(env, Base64, Base64_decode, in);
    if (inBytes == NULL) {
        return in;
    }
    jbyteArray bytes = (*env)->CallObjectMethod(env, cipher, Cipher_doFinal, inBytes);
    jstring s = (*env)->NewObject(env, String, String_init, bytes);
    // return (*env)->CallObjectMethod(env, s, String_replaceAll, regular, replace);
    return (*env)->CallObjectMethod(env, s, String_trim);
}

#ifdef __cplusplus
}
#endif
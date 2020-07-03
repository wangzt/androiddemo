package com.tomsky.androiddemo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Set;

import static com.tomsky.androiddemo.provider.SPConstants.*;

/**
 * Created by j-wangzhitao on 17-10-20.
 */

public class SPContentProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String[] path= uri.getPath().split(SEPARATOR);
        String type=path[1];
        if (type.equals(TYPE_GET_ALL)){
            Map<String, ?> all = SPImpl.getAll(getContext());
            if (all!=null) {
                MatrixCursor cursor = new MatrixCursor(new String[]{CURSOR_COLUMN_NAME,CURSOR_COLUMN_TYPE,CURSOR_COLUMN_VALUE});
                Set<String> keySet = all.keySet();
                for (String key:keySet){
                    Object[] rows=new Object[3];
                    rows[0]=key;
                    rows[2]=all.get(key);
                    if (rows[2] instanceof Boolean) {
                        rows[1]=TYPE_BOOLEAN;
                    }else
                    if (rows[2] instanceof String) {
                        rows[1]=TYPE_STRING;
                    }else
                    if (rows[2] instanceof Integer) {
                        rows[1]=TYPE_INT;
                    }else
                    if (rows[2] instanceof Long) {
                        rows[1]=TYPE_LONG;
                    }else
                    if (rows[2] instanceof Float) {
                        rows[1]=TYPE_FLOAT;
                    }
                    cursor.addRow(rows);
                }
                return cursor;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // 用这个来取数值
        String[] path= uri.getPath().split(SEPARATOR);
        String type=path[1];
        String key=path[2];
        if (type.equals(TYPE_CONTAIN)){
            return SPImpl.contains(getContext(),key)+"";
        }
        return  ""+SPImpl.get(getContext(),key,type);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String[] path= uri.getPath().split(SEPARATOR);
        String type=path[1];
        String key=path[2];
        Object obj= (Object) values.get(VALUE);
        if (obj!=null)
            SPImpl.save(getContext(),key,obj);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String[] path= uri.getPath().split(SEPARATOR);
        String type=path[1];
        if (type.equals(TYPE_CLEAN)){
            SPImpl.clear(getContext());
            return 0;
        }
        String key=path[2];
        if (SPImpl.contains(getContext(),key)){
            SPImpl.remove(getContext(),key);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        insert(uri,values);
        return 0;
    }
}

package com.tomsky.androiddemo.view.opengl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by j-wangzhitao on 17-8-4.
 */


public class SquareShape {
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int muMVPMatrixHandle;

    public SquareShape(float r) {
        initVetexData(r);
    }

    public void initVetexData(float r) {
        // 初始化顶点坐标
        float[] vertexArray = new float[] {
                -0.5f, -0.5f, -0.1f * r, r,
                0.5f, -0.5f, -0.1f * r, r,
                0.5f, 0.5f, -0.1f * r, r,
                -0.5f, 0.5f, -0.1f * r, r
        };

        ByteBuffer buffer = ByteBuffer.allocateDirect(vertexArray.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = buffer.asFloatBuffer();
        mVertexBuffer.put(vertexArray);
        mVertexBuffer.position(0);

        /**
         *
         * 着色器源代码被编译成一个目标形式（类似obj文件），编译之后，着色器对象可以连接到一个程序对象，程序对象可以连接多个着色器对象。
         * 获得连接后的着色器对象的过程：
         * 1、创建一个顶点着色器和一个片元着色器
         * 2、将源代码连接到每个着色器对象
         * 3、编译着色器对象
         * 4、创建一个程序对象
         * 5、将编译后的着色器对象连接到程序对象
         * 6、连接程序对象
         */

        int vertexShader = loaderShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode); // 顶点着色器
        int fragmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode); // 片元着色器s

        mProgram = GLES20.glCreateProgram(); // 创建一个程序对象
        GLES20.glAttachShader(mProgram, vertexShader); // 将顶点着色器对象连接到程序对象
        GLES20.glAttachShader(mProgram, fragmentShader); // 将片元着色器对象连接到程序对象
        GLES20.glLinkProgram(mProgram); // 连接程序对象

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);
        // 将顶点数据传递到管线，顶点着色器
        GLES20.glVertexAttribPointer(mPositionHandle, 4, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        // 将变换矩阵传递到管线
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // 绘制图元
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
    }

    private int loaderShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type); // 创建一个着色器
        GLES20.glShaderSource(shader, shaderCode); // 将源代码连接到着色器对象
        GLES20.glCompileShader(shader); // 编译着色器对象
        return shader;
    }

    private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
            + "attribute vec4 aPosition;"
            + "void main(){"
            + "gl_Position = uMVPMatrix * aPosition;"
            + "}";

    private String fragmentShaderCode = "precision mediump float;"
            + "void main(){"
            + "gl_FragColor = vec4(1,0,0,0);"
            + "}";
}

package com.skt.realedu.mission.dragonfly;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 카메라 프리뷰 화면
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder holder;
    Camera camera;

    public CameraPreview(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * 화면 설정 변경시 호출됨
     * 
     * @param holder SurfaceHolder
     * @param format format
     * @param width width
     * @param height height
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(camera == null)
            return;
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(width, height);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    /**
     * 화면 생성시 호출됨
     * 
     * @param holder SurfaceHolder
     */
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(holder);
        } catch(Exception e) {
            if(camera != null)
                camera.release();
            camera = null;
        }
    }

    /**
     * 화면 종료시 호출됨
     * 
     * @param holder SurfaceHolder
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}

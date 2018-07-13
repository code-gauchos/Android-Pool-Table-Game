package com.jordan.androidpooltablegame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder _surfaceHolder;
    private GamePanel _gamePanel;
    private boolean isRunning;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        this._surfaceHolder = surfaceHolder;
        this._gamePanel = gamePanel;

    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;

        while (isRunning) {
            startTime = System.nanoTime();
            canvas = null;

            // try locking the canvas for pixel editing
            try {
                canvas = this._surfaceHolder.lockCanvas();

                synchronized (_surfaceHolder) {
                    this._gamePanel.update();
                    this._gamePanel.draw(canvas);
                }
            } catch (Exception ex) {

            }finally {
                if(canvas != null){
                    try{
                        _surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;

            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception ex) {
            }

            totalTime += System.nanoTime() - startTime;

            frameCount++;

            if (frameCount == FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);

                totalTime = 0;

                frameCount = 0;

                System.out.println("Average FPS: " + averageFPS);
            }
        }
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }
}
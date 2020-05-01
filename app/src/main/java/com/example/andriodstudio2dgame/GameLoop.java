package com.example.andriodstudio2dgame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

//Game manages all objects in the game and is responsible for updating all states and rendar all
// objects to the screen
class GameLoop extends Thread{
    private static final double MAX_UPS = 30.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;
    private Game game;
    private boolean isRunning = false;
    private SurfaceHolder surfaceHolder;
    private double averageUPS;
    private double averageFPS;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public double GetAverageUPS() {
        return averageUPS;
    }

    public double GetAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();
        //declare time and cycle count variables
        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        Canvas canvas = null;
        startTime = System.currentTimeMillis();

        // ********************
        // ********************
        //  HERE IS MY GAMELOOP
        // ********************
        // ********************
        while(isRunning)
        {
            //tRY TO UPDATE AND RENDER GAME
            try
            {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                        game.update();
                        updateCount++;
                        game.draw(canvas);
                }

            }
            catch(IllegalArgumentException e)
            {
                    e.printStackTrace();
            }
            finally
            {
                if(canvas != null)
                {
                    try
                    {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    }
                    catch (IllegalArgumentException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //Pause the game loop to not exceed target ups
            elapsedTime =  System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            if(sleepTime >0 )
            {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // skip frames to keep up with target ups
            while(sleepTime < 0 && updateCount <MAX_UPS - 1)
            {
                game.update();
                updateCount++;
                elapsedTime =  System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            }
            //calculate average ups and fps
            elapsedTime =  System.currentTimeMillis() - startTime;
            if(elapsedTime >= 1000)
            {
                averageUPS = updateCount / (1E-3 * elapsedTime);
                averageFPS = frameCount / (1E-3 * elapsedTime);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
}

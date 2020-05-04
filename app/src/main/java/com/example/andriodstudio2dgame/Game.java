package com.example.andriodstudio2dgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import object.Circle;
import object.Enemy;
import object.Player;
import object.Spell;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    //private final Enemy enemy;
    private GameLoop gameLoop;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Spell> spellList = new ArrayList<Spell>();
    private int joystickPointerID = 0;
    private int numberOfSpellsToCast = 0;


    public Game(Context context) {
        super(context);

        //get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        joystick = new Joystick(275,700,70,40);
        gameLoop = new GameLoop(this, surfaceHolder);
        player = new Player(getContext(),joystick, 1000,500,30);
        //enemy = new Enemy(getContext(), player, 2000,500,30);
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //handling the touch stuff

        switch(event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.getIsPressed())
                {
                    numberOfSpellsToCast++;
                }
                else if(joystick.isPressed((double) event.getX(), (double) event.getY()))
                {
                    joystickPointerID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);

                }
                else
                {
                    numberOfSpellsToCast++;
                }

                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed())
                {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerID == event.getPointerId(event.getActionIndex()))
                {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //adding all rendering calls to this method
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);
        joystick.draw(canvas);
        player.draw(canvas);
        for( Enemy enemy : enemyList)
        {
            enemy.draw(canvas);
        }

        for (Spell spell : spellList)
        {
            spell.draw(canvas);
        }
        //enemy.draw(canvas);
    }

    public void drawUPS(Canvas canvas)
    {
        String averageUPS = Double.toString(gameLoop.GetAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: "+ averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas)
    {
        String averageFPS = Double.toString(gameLoop.GetAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: "+ averageFPS, 100, 200, paint);
    }

    public void update() {
        joystick.update();
        player.update();
        //enemy.update();
        if (Enemy.readyToSpawn())
        {
            enemyList.add(new Enemy(getContext(), player));
        }

        while(numberOfSpellsToCast > 0)
        {
            spellList.add(new Spell (getContext(), player));
            numberOfSpellsToCast--;
        }
        for (Enemy enemy : enemyList)
        {
            enemy.update();
        }

        for (Spell spell : spellList)
        {
            spell.update();
        }

        Iterator<Enemy> iteratorEnemy = enemyList.iterator();

        while (iteratorEnemy.hasNext())
        {
            Circle enemy = iteratorEnemy.next();
            if (Circle.isColliding(enemy, player))
            {
                iteratorEnemy.remove();
                player.setHealthPoints((int) (player.getHealthPoints() - 1));
                continue;
            }
            Iterator<Spell> iteratorSpell = spellList.iterator();
            while(iteratorSpell.hasNext())
            {
                Circle spell = iteratorSpell.next();
                if(Circle.isColliding(spell, enemy))
                {
                    iteratorSpell.remove();
                    iteratorEnemy.remove();
                    break;
                }
            }
        }
    }
}

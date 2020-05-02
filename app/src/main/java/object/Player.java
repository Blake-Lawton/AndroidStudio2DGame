package com.example.andriodstudio2dgame.object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.andriodstudio2dgame.GameLoop;
import com.example.andriodstudio2dgame.Joystick;
import com.example.andriodstudio2dgame.R;

public class Player extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;




    

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius)
    {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;


    }


    public void update() {
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;
    }

    public void setPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
}

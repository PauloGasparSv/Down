package com.pvale.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.pvale.down.MyGame    ;

public class In
{
    public static Controller controller = new FakeController();
    public static boolean hasController = false;

    public static int right = Input.Keys.RIGHT;
    public static int left = Input.Keys.LEFT;
    public static int up = Input.Keys.UP;
    public static int down = Input.Keys.DOWN;

    public static int cameraRight = Input.Keys.D;
    public static int cameraLeft = Input.Keys.A;
    public static int cameraUp = Input.Keys.W;
    public static int cameraDown = Input.Keys.S;

    public static int menu = Input.Keys.ESCAPE;
    public static int buttonMenu = 7;

    public static int xAxis = 1;
    public static int yAxis = 0;

    public static int keyJump = Input.Keys.Z;
    public static int buttonJump = 0;
    
    public static int keyAttack = Input.Keys.X;
    public static int buttonAttack = 2;
    
    public static boolean attacking = false;
    public static boolean jumping = false;
    public static boolean goingRight = false;
    public static boolean goingLeft = false;
    public static boolean goingUp = false;
    public static boolean goingDown = false;
    public static boolean pressingMenu = false;

    public static void getControllerInfo()
    {
        System.out.println("Axis 0 :" + controller.getAxis(0));
        System.out.println("Axis 1 :" + controller.getAxis(1));
        System.out.println("Axis 2 :" + controller.getAxis(2));
        System.out.println("Axis 3 :" + controller.getAxis(3));

        System.out.print("Button: " + getPressedButton() + " pressed");

    }

    public static void loadPreferences()
    {
        Preferences prefs = MyGame.prefs;
        if(prefs.contains("buttonJump"))
            buttonJump = prefs.getInteger("buttonJump");
        if(prefs.contains("keyJump"))
            buttonJump = prefs.getInteger("keyJump");
        if(prefs.contains("buttonAttack"))
            buttonAttack = prefs.getInteger("buttonAttack");
        if(prefs.contains("keyAttack"))
            keyAttack = prefs.getInteger("keyAttack");
        
    }

    public static int getPressedButton()
    {
        for(int i = 0; i < 512; i++)
        {
            if(controller.getButton(i))
                return i;
        }
        
        return -1;
    }

    public static void updateController()
    {
        goingRight = controllerRight();
        goingLeft = controllerLeft();
        goingUp = controllerUp();
        goingDown = controllerDown();
        jumping = controllerJump();
        attacking = controllerAttack();
        pressingMenu = menu();
    }

    public static void loadController()
    {
        if(Controllers.getControllers().size > 0)
        {
            controller = Controllers.getControllers().first();
            hasController = true;
        }
    }

    public static boolean right()
    {
        return Gdx.input.isKeyPressed(right) || controllerRight(); 
    }

    public static boolean justRight()
    {
        return Gdx.input.isKeyJustPressed(right) || (controllerRight() && !goingRight);
    }
    
    public static boolean controllerRight()
    {
        return controller.getAxis(xAxis) > 0.5f || controller.getPov(0) == PovDirection.east;
    }

    public static boolean left()
    {
        return Gdx.input.isKeyPressed(left) || controllerLeft();
    }

    public static boolean justLeft()
    {
        return Gdx.input.isKeyJustPressed(left)  || (controllerLeft() && !goingLeft);
    }

    public static boolean controllerLeft()
    {
        return controller.getAxis(xAxis) < -0.5f || controller.getPov(0) == PovDirection.west;
    }

    public static boolean up()
    {
        return Gdx.input.isKeyPressed(up) || controllerUp();
    }

    public static boolean justUp()
    {
        return Gdx.input.isKeyJustPressed(up) || (controllerUp() && !goingUp);
    }

    public static boolean controllerUp()
    {
        return controller.getAxis(yAxis) > 0.5f || controller.getPov(0) == PovDirection.north;
    }

    public static boolean down()
    {
        return Gdx.input.isKeyPressed(down) || controllerDown();
    }

    public static boolean justDown()
    {
        return Gdx.input.isKeyJustPressed(down) || (controllerDown() && !goingDown);
    }

    public static boolean controllerDown()
    {
        return controller.getAxis(yAxis) < -0.5f || controller.getPov(0) == PovDirection.south;
    }


    public static boolean jump()
    {
        return Gdx.input.isKeyPressed(keyJump) || controllerJump();
    }

    public static boolean justJumped()
    {
        return Gdx.input.isKeyJustPressed(keyJump) || (controllerJump() && !jumping);
    }

    public static boolean controllerJump()
    {
        return controller.getButton(buttonJump);
    }

    public static boolean attack()
    {
        return Gdx.input.isKeyPressed(keyAttack) || controllerAttack();
    }

    public static boolean justAttacked()
    {
        return Gdx.input.isKeyJustPressed(keyAttack) || (controllerAttack() && !attacking);
    }

    public static boolean controllerAttack()
    {
        return controller.getButton(buttonAttack);
    }

    public static boolean cameraRight()
    {
        return Gdx.input.isKeyPressed(cameraRight);
    }
    
    public static boolean cameraLeft()
    {
        return Gdx.input.isKeyPressed(cameraLeft);
    }

    public static boolean cameraUp()
    {
        return Gdx.input.isKeyPressed(cameraUp);
    }

    public static boolean cameraDown()
    {
        return Gdx.input.isKeyPressed(cameraDown);
    }

    public static boolean menu()
    {
        return Gdx.input.isKeyPressed(menu) || controller.getButton(buttonMenu); 
    }

    public static boolean justMenu()
    {
        return Gdx.input.isKeyJustPressed(menu) || (menu() && !pressingMenu);
    }

    public static float getX()
    {
        return Gdx.input.getX() * 0.25f;
    }
    
    public static float getY()
    {
        float y = 540 - Gdx.input.getY(); 
        return y * 0.25f;
    }

    public static boolean clicked()
    {
        return Gdx.input.justTouched();
    }

    public static boolean mousePress(int button)
    {
        switch(button)
        {
            case 0:
                return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
            case 1:
                return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
            default:
                return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        }
    }

    public static boolean copy()
    {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.C);
    }

    public static boolean paste()
    {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.V);
    }

    public static boolean save()
    {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.S);
    }

}
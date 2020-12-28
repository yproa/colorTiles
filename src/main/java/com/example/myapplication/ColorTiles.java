package com.example.myapplication;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;


class Card {
    Paint p = new Paint();
    int outline = 10;

    ArrayList<Integer> colors2 = new ArrayList<Integer>(Arrays.asList(
            Color.rgb(192,192,192), Color.rgb(0,128,0),
            Color.rgb(128,0,128), Color.rgb(0,128,128)
    ));
    public void CardProperties(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Card(int color,int rand) {
        this.color = color;
        this.r = rand;
    }
    public int getColor() {
        return nColor;
    }

    int color;
    float x, y, r;
    int nColor;

    public void draw(Canvas c) {
        p.setColor(color);
        c.drawCircle(x+r+outline,y+r+outline,r,p);
    }

    public boolean flip (float touch_x, float touch_y) {
        if (touch_x >= x-r && touch_x <= x + r && touch_y >= y-r && touch_y <= y + r) {
            nColor+=1;
            nColor=(nColor+1)%colors2.size();//я знаю что это дикий костыль, но пытаюсь сделать быстро и чтоб работало
            return true;
        }
        else return false;
    }
    public void updateColor() {
        color=colors2.get(nColor);
    }
}

public class ColorTiles extends View {


    int row = 4, col = 4;
    int openedCard = 0;

    Card[] openedCards = new Card[2];
    Card[][] cards = new Card[row][col];

    ArrayList<Integer> colors = new ArrayList<Integer>(Arrays.asList(
            Color.rgb(192,192,192), Color.rgb(0,128,0),
            Color.rgb(128,0,128), Color.rgb(0,128,128)
    ));// 4 сцета пока хватит

    public ColorTiles(Context context) {
        super(context);
    }

    public ColorTiles(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int random = (int) (Math.random() * colors.size());//выбираем случайный из цветов
                cards[i][j] = new Card(colors.get(random),random);//вписываем на номер этой карты цвет который нарандомили
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int card_height = canvas.getHeight() / row;
        int card_width = canvas.getWidth() / col;
        float r =  (canvas.getWidth()/10*9) / col/2;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                int x = j * card_width;
                int y = i * card_height;

                cards[i][j].CardProperties(x, y+r, r);
                cards[i][j].draw(canvas);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();


        int counter=0;
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (cards[i][j].flip(x, y)) {
                        //cards[i][j]+=1;
                        cards[i][j].updateColor();

                    }
                }
            }

        }
        int color=cards[0][0].getColor();//тут берём цвет первого круга
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (cards[i][j].getColor() == color) {
                    counter += 1;
                }
            }
        }
        invalidate();
        if (counter==col*row){
            Toast toast = Toast.makeText(getContext(), "Вы молодец!!! Победа!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return true;
    }

}
package a84934.droidterpreter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GView extends View implements View.OnTouchListener{

    class Block {
        int width, height, color, deltaX, deltaY;
        Rect r;
    }

    @Override
    public boolean performClick(){
        return super.performClick();
    }

    int canvasHeight, canvasWidth;

    private void init(){
        this.blocks = new ArrayList<>();
        this.setOnTouchListener(this);
    }

    public GView(Context context) {
        super(context);
        init();
    }

    public GView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        canvasWidth = w;
        canvasHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }


    Random random = new Random();

    @Override
    public void onDraw(Canvas canvas){

        // clear
        canvas.drawColor(Color.WHITE);

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);

        for(Block b : blocks){
            p.setColor(b.color);
            canvas.drawRect(b.r, p);
        }
    }

    public void addPlus(){
        Block b = new Block();
        b.color =  Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        b.width = b.height = 200;
        int left = 300;
        int top = 100;
        b.r = new Rect(left,top, left + b.width, top +  b.height);
        this.blocks.add(b);
        this.invalidate();
    }

    List<Block> blocks;

    Block current = null;

    private void moveBlock(Block b, float _x, float _y){
        int x = (int)_x;
        int y = (int)_y;

        x = x - b.deltaX;
        y -= b.deltaY;

        if(x < 0){
            x = 0;
        }
        if(y < 0){
            y = 0;
        }

        int right = x + b.width;
        int bottom = y + b.height;

        if(right > canvasWidth){
            x = canvasWidth - b.width;
        }
        if(bottom > canvasHeight){
            y = canvasHeight - b.height;
        }

        right = x + b.width;
        bottom = y + b.height;

        b.r = new Rect(x,y, right, bottom);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Log.d("touch coor", "x: " + x + " y: " + y);

        String addOn = ": " + x + "," + y;
        switch (motionEvent.getActionMasked()){
            case MotionEvent.ACTION_DOWN:

                current = null;

                for(int i = blocks.size() - 1; i > -1; i--){
                    Block b = blocks.remove(i);
                    blocks.add(b);
                    Rect r = b.r;
                    if(x >= r.left && x <= r.right && y >= r.top && y <= r.bottom){
                        current = b;
                        current.deltaX = (int)x - r.left;
                        current.deltaY = (int)y - r.top;
                        Log.d("down", "found block");
                        break;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                current = null;
                Toast.makeText(getContext(), "Up" + addOn, Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_CANCEL:
                current = null;
                Toast.makeText(getContext(), "Cancel" + addOn, Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_MOVE:
                if(current != null){
                    Log.d("move", "moving");
                    moveBlock(current, x, y);
                    this.invalidate();
                } else {
                    Log.d("move", "not down");
                }
                break;
            default:
                //Toast.makeText(getContext(), "other" + addOn, Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}

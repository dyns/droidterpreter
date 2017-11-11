package a84934.droidterpreter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.SupportActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GView extends View implements View.OnTouchListener{

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
    Paint p = new Paint();

    @Override
    public void onDraw(Canvas canvas){
        // clear
        canvas.drawColor(Color.WHITE);

        p.setStyle(Paint.Style.FILL);
        for(Block b : blocks){
            p.setColor(b.color);
            canvas.drawRect(b.r, p);
            p.setColor(Color.WHITE);
            p.setTextSize(75);

            //canvas.drawRect(new Rect(b.r.left, b.r.top, b.r.left + 30, b.r.top + 30), p);

            p.setTextAlign(Paint.Align.LEFT);
            //canvas.drawText("L", b.r.left, b.r.bottom, p);


            if(b.type == Block.BlockType.NUM){
                String v = Integer.toString((int)b.value);
                canvas.drawText(v, b.r.left, b.r.bottom, p);
            } else if(b.type == Block.BlockType.ADD){
                canvas.drawText("+", b.r.left, b.r.bottom, p);
            }

        }
    }

    public void addPlus(Block.BlockType type){
        Block b = new Block();
        b.type = type;
        b.color =  Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        b.width = b.height = 100;
        int left = 0;
        int top = 0;
        b.r = new Rect(left,top, left + b.width, top +  b.height);

        switch (type){
            case ADD:
                break;
            case NUM:
                b.value = 0;
                break;
        }

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

    static class LongHoldTask extends AsyncTask<Void, Void, Boolean> {

        final int blockIndex;
        final WeakReference<SupportActivity> context;
        WeakReference<GView> gview;
        LongHoldTask(int blockIndex, WeakReference<GView> gview, WeakReference<SupportActivity> context){
            super();
            this.blockIndex = blockIndex;
            this.context = context;
            this.gview = gview;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            long start = System.currentTimeMillis();
            long delay = (long)(1.5 * 1000);
            long end = start + delay;
            while(System.currentTimeMillis() < end){
                if(isCancelled()){
                    return false;
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean finished){
            if(finished != null &&
                    finished &&
                    !isCancelled() &&
                    context.get() != null &&
                    !context.get().isFinishing()
                    && gview.get() != null){
                //Toast.makeText(context.get(), "long on: " + blockIndex, Toast.LENGTH_SHORT).show();
                gview.get().showBlockOptions(blockIndex);
            }
        }
    }

    private void showBlockOptions(int i){
        // disable drag
        current = null;
        stopLongHold();

        Block b = blocks.get(i);

        switch (b.type){
            case NUM:
                Intent intent = new Intent(getContext(), NumResultActivity.class);
                intent.putExtra("i", i);
                ((MainActivity)getContext()).startActivityForResult(intent, MainActivity.Companion.getNUM_RESULT());
                break;
            case ADD:
                break;
        }
    }

    LongHoldTask longHoldTask;

    private void stopLongHold(){
        if(longHoldTask != null){
            longHoldTask.cancel(true);
        }
        longHoldTask = null;
    }

    public void setBlockValue(int i, int v){
        Block b = blocks.get(i);
        switch (b.type){
            case ADD:
                break;
            case NUM:
                b.value = v;
                break;
        }
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        stopLongHold();

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

                        // found block
                        longHoldTask = new LongHoldTask(i,
                                new WeakReference<>(this),
                                new WeakReference<>((SupportActivity) getContext()));
                        longHoldTask.execute();

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
                //Toast.makeText(getContext(), "Up" + addOn, Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_CANCEL:
                current = null;
                // Toast.makeText(getContext(), "Cancel" + addOn, Toast.LENGTH_SHORT).show();
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

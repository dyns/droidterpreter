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
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import a84934.droidterpreter.BlockValSetActivities.SetAddValActivity;
import a84934.droidterpreter.BlockValSetActivities.SetNumValActivity;
import a84934.droidterpreter.BlockVals.BlockValMain;

public class GView extends View implements View.OnTouchListener {

    public void execute(){
        new AlertDialog.Builder(getContext())
                .setTitle("Result")
                .setMessage("none yet!")
                .setPositiveButton("done", null)
                .show();
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
            p.setStrokeWidth(20);

            //canvas.drawRect(new Rect(b.r.left, b.r.top, b.r.left + 30, b.r.top + 30), p);

            p.setTextAlign(Paint.Align.LEFT);
            //canvas.drawText("L", b.r.left, b.r.bottom, p);

            int offset = 15;
            int textLeft = b.r.left + offset;
            int textBottom = b.r.bottom - offset;

            int centerX = b.r.left + (BLOCK_WIDTH / 2);
            int centerOffset = (BLOCK_WIDTH / 4);

            Block toBlock;

            if(b.type == Block.BlockType.NUM){
                String v = Integer.toString((int)b.value);
                canvas.drawText(v, textLeft, textBottom, p);
            } else if(b.type == Block.BlockType.ADD){

                canvas.drawText("+", textLeft, textBottom, p);
                if(b.value != null){
                    p.setColor(Color.BLACK);
                    AddBlockVal v = (AddBlockVal)b.value;
                    if(v.left >= 0) {
                        toBlock = blocks.get(v.left);
                        canvas.drawLine(centerX - centerOffset, b.r.bottom, toBlock.r.left, toBlock.r.top, p);
                    }
                    if(v.right >= 0){
                        toBlock = blocks.get(v.right);
                        canvas.drawLine(centerX + centerOffset, b.r.bottom, toBlock.r.left, toBlock.r.top, p);
                    }
                }
            } else if(b.type == Block.BlockType.MAIN){
                canvas.drawText("M", textLeft, textBottom, p);
                BlockValMain v = (BlockValMain) b.value;
                if(v != null && v.startIndex > 0){
                    p.setColor(Color.BLACK);
                    toBlock = blocks.get(v.startIndex);
                    canvas.drawLine(centerX + centerOffset, b.r.bottom, toBlock.r.left, toBlock.r.top, p);
                }
            }

        }
    }

    private final int BLOCK_WIDTH = 130;

    public void addPlus(Block.BlockType type){
        Block b = new Block();
        b.type = type;
        b.color =  Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        b.width = b.height = BLOCK_WIDTH;
        int left = 0;
        int top = 0;
        b.r = new Rect(left,top, left + b.width, top +  b.height);

        switch (type){
            case ADD:
                b.value = new AddBlockVal();
                break;
            case NUM:
                b.value = 0;
                break;
            case MAIN:
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
            long delay = (long)(1 * 1000);
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

    List<Integer> clickedBlocks;
    Integer toClick;
    int addBlockWaitIndex;

    private void showBlockOptions(int i){
        // disable drag
        current = null;
        stopLongHold();

        Block b = blocks.get(i);

        Intent intent;
        switch (b.type){
            case NUM:
                intent = new Intent(getContext(), SetNumValActivity.class);
                intent.putExtra("i", i);
                ((MainActivity)getContext()).startActivityForResult(intent, MainActivity.Companion.getNUM_RESULT());
                break;
            case ADD:

                Toast.makeText(getContext(), "Select left and then right blocks", Toast.LENGTH_LONG).show();

                clickedBlocks = new ArrayList<>();
                toClick = 2;
                addBlockWaitIndex = i;

                /*
                intent = new Intent(getContext(), SetAddValActivity.class);
                intent.putExtra("i", i);
                ((MainActivity)getContext()).startActivityForResult(intent, MainActivity.Companion.getADD_BLOCK_RESULT());
                */

                break;
            case MAIN:
                Toast.makeText(getContext(), "Select start block", Toast.LENGTH_LONG).show();

                clickedBlocks = new ArrayList<>();
                toClick = 1;
                addBlockWaitIndex = i;

                break;
        }
    }

    private void setAddBlocksLeftRight(){
        Block editBlock = blocks.get(addBlockWaitIndex);

        switch (editBlock.type){
            case ADD:
                AddBlockVal v = new AddBlockVal();
                v.left = clickedBlocks.get(0);
                v.right = clickedBlocks.get(1);
                editBlock.value = v;
                break;
            case MAIN:
                BlockValMain mv = new BlockValMain();
                mv.startIndex = clickedBlocks.get(0);
                editBlock.value = mv;
                break;
        }

        clickedBlocks = null;
        toClick = null;

        invalidate();
    }

    LongHoldTask longHoldTask;

    private void stopLongHold(){
        if(longHoldTask != null){
            longHoldTask.cancel(true);
        }
        longHoldTask = null;
    }

    public void setBlockValue(int i, Object v){
        Block b = blocks.get(i);
        b.value = v;
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
                    Block b = blocks.get(i);
                    Rect r = b.r;
                    if(x >= r.left && x <= r.right && y >= r.top && y <= r.bottom){

                        if(toClick != null){
                            toClick--;
                            clickedBlocks.add(i);

                            if(toClick <= 0){
                                setAddBlocksLeftRight();
                            }
                            return true;
                        }

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

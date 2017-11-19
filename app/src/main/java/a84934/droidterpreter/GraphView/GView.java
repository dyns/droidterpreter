package a84934.droidterpreter.GraphView;

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

import a84934.droidterpreter.Block;
import a84934.droidterpreter.BlockValSetActivities.SetNumValActivity;
import a84934.droidterpreter.BlockVals.AddBV;
import a84934.droidterpreter.BlockVals.MainBV;
import a84934.droidterpreter.BlockVals.NumBV;
import a84934.droidterpreter.Interpreter.Interpreter;
import a84934.droidterpreter.MainActivity;

public class GView extends View implements View.OnTouchListener {

    GController controller;
    BlockLongClickListener blockLongClickListener;

    public void showResultDialog(String val){
        new AlertDialog.Builder(getContext())
                .setTitle("Result")
                .setMessage(val)
                .setPositiveButton("done", null)
                .show();
    }

    @Override
    public boolean performClick(){
        return super.performClick();
    }

    int canvasHeight, canvasWidth;

    public void initGView(BlockLongClickListener blockLongClickListener, GController controller){
        this.controller = controller;
        this.setOnTouchListener(this);
        this.blockLongClickListener = blockLongClickListener;
    }

    public GView(Context context) {
        super(context);
        //init();
    }

    public GView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //init();
    }

    public GView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init();
    }

    public GView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        canvasWidth = w;
        canvasHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

   // Random random = new Random();
    Paint p = new Paint();

    @Override
    public void onDraw(Canvas canvas){
        // clear
        canvas.drawColor(Color.WHITE);

        p.setStyle(Paint.Style.FILL);
        for(Block b : controller.getBlocks()){
            p.setColor(b.color);
            canvas.drawRect(b.r, p);
            p.setColor(Color.WHITE);
            p.setTextSize(75);
            p.setStrokeWidth(10);

            //canvas.drawRect(new Rect(b.r.left, b.r.top, b.r.left + 30, b.r.top + 30), p);

            p.setTextAlign(Paint.Align.LEFT);
            //canvas.drawText("L", b.r.left, b.r.bottom, p);

            int offset = 15;
            int textLeft = b.r.left + offset;
            int textBottom = b.r.bottom - offset;

            int centerX = b.r.left + (b.width / 2);
            int centerOffset = (b.width / 4);

            Block toBlock;

            if(b.type == Block.BlockType.NUM){
                String v = Integer.toString(((NumBV)b.value).getNumber());
                canvas.drawText(v, textLeft, textBottom, p);
            } else if(b.type == Block.BlockType.ADD){

                canvas.drawText("+", textLeft, textBottom, p);
                if(b.value != null){
                    p.setColor(Color.BLACK);
                    AddBV v = (AddBV)b.value;
                    if(v.getLeftBlock() != null) {
                        toBlock = v.getLeftBlock();
                        canvas.drawLine(centerX - centerOffset, b.r.bottom, toBlock.r.left, toBlock.r.top, p);
                    }
                    if(v.getRightBlock() != null){
                        toBlock = v.getRightBlock();
                        canvas.drawLine(centerX + centerOffset, b.r.bottom, toBlock.r.left, toBlock.r.top, p);
                    }
                }
            } else if(b.type == Block.BlockType.MAIN){
                canvas.drawText("M", textLeft, textBottom, p);
                MainBV v = (MainBV) b.value;
                if(v.getStartBlock() != null){
                    p.setColor(Color.BLACK);
                    toBlock = v.getStartBlock();
                    canvas.drawLine(centerX + centerOffset, b.r.bottom, toBlock.r.left, toBlock.r.top, p);
                }
            }

        }
    }

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
        final WeakReference<GView> gview;
        final WeakReference<GController> controller;
        final WeakReference<BlockLongClickListener> blcl;

        LongHoldTask(int blockIndex, WeakReference<GView> gview,
                     WeakReference<SupportActivity> context,
                     WeakReference<GController> controller,
                     WeakReference<BlockLongClickListener> blcl){
            super();
            this.blockIndex = blockIndex;
            this.context = context;
            this.gview = gview;
            this.controller = controller;
            this.blcl = blcl;
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
                    && gview.get() != null
                    && controller.get() != null
                    && blcl.get() != null){

                blcl.get().onBlockLongCLick(blockIndex);

            }
        }
    }

    List<Integer> clickedBlocks;
    Integer toClick;
    int addBlockWaitIndex;

    /*
    private void showBlockOptions(int i){
        // disable drag
        current = null;
        stopLongHold();

        Block b = controller.getBlock(i); //blocks.get(i);

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


                //intent = new Intent(getContext(), SetAddValActivity.class);
                //intent.putExtra("i", i);
                //((MainActivity)getContext()).startActivityForResult(intent, MainActivity.Companion.getADD_BLOCK_RESULT());

                break;
            case MAIN:
                Toast.makeText(getContext(), "Select start block", Toast.LENGTH_LONG).show();

                clickedBlocks = new ArrayList<>();
                toClick = 1;
                addBlockWaitIndex = i;

                break;
        }
    }
    */


    private void setAddBlocksLeftRight(){
        Block editBlock = controller.getBlock(addBlockWaitIndex); //blocks.get(addBlockWaitIndex);

        switch (editBlock.type){
            case ADD:
                AddBV addBV = (AddBV) editBlock.value;
                addBV.setLeftBlock(controller.getBlock(clickedBlocks.get(0)));
                addBV.setRightBlock(controller.getBlock(clickedBlocks.get(1)));
                break;
            case MAIN:
                MainBV mainBV = (MainBV) editBlock.value;
                mainBV.setStartBlock(controller.getBlock(clickedBlocks.get(0)));
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

    /*
    public void setBlockValue(int i, Object v){
        Block b = blocks.get(i);
        b.value = v;
        invalidate();
    }*/

    Float _oldX, _oldY;

    private final int MOVE_DELTA_THRESHOLD = 5; //TODO change this to something with DP
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        if(motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE){
            boolean xPast = _oldX == null || (Math.abs(_oldX - x) > MOVE_DELTA_THRESHOLD);
            boolean yPast = _oldY == null || (Math.abs(_oldY - y) > MOVE_DELTA_THRESHOLD);

            if(!xPast && !yPast){
                return true;
            }
        }

        _oldX = x;
        _oldY = y;

        stopLongHold();

        Log.d("touch coor", "x: " + x + " y: " + y);

        String addOn = ": " + x + "," + y;

        switch (motionEvent.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.d("move", "ACTION_DOWN");

                current = null;

                for(int i = controller.blockCount() - 1; i > -1; i--){
                    Block b = controller.getBlock(i); //blocks.get(i);
                    Rect r = b.r;
                    if(x >= r.left && x <= r.right && y >= r.top && y <= r.bottom){

                        if(toClick != null){
                            toClick--;
                            clickedBlocks.add(i);

                            if(toClick <= 0){
                                setAddBlocksLeftRight();
                            } else {
                                Toast.makeText(getContext(), "Click " + toClick + " more block(s)", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }

                        // found block
                        longHoldTask = new LongHoldTask(i,
                                new WeakReference<>(this),
                                new WeakReference<>((SupportActivity) getContext()),
                                new WeakReference<>(controller),
                                new WeakReference<>(blockLongClickListener));
                        longHoldTask.execute();

                        current = b;
                        current.deltaX = (int)x - r.left;
                        current.deltaY = (int)y - r.top;
                        Log.d("down", "found block");
                        break;
                    }

                    if(toClick != null){
                        Toast.makeText(getContext(),
                                "Not a block, click " + toClick + " more",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.d("move", "ACTION_UP");

                current = null;
                //Toast.makeText(getContext(), "Up" + addOn, Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("move", "ACTION_CANCEL");

                current = null;
                // Toast.makeText(getContext(), "Cancel" + addOn, Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("move", "ACTION_MOVE");

                if(current != null){
                    Log.d("move", "moving");
                    moveBlock(current, x, y);
                    this.invalidate();
                } else {
                    Log.d("move", "not down");
                }
                break;
            default:
                Log.d("move", "ACTION OTHER");
                break;
        }

        return true;
    }

}

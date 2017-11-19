package a84934.droidterpreter;

import android.graphics.Rect;

public class Block {
    public BlockType type;

    public int width, height, color, deltaX, deltaY;
    public Rect r;
    public Object value;

    public enum BlockType {
        ADD,
        NUM,
        MAIN
    }

    public static BlockType fromIndex(int i){
        switch (i){
            case 0:
                return BlockType.ADD;
            case 1:
                return BlockType.NUM;
            default:
                return BlockType.NUM;
        }
    }

    public static BlockType typeFromString(String type){
        switch (type){
            case "Add":
                return BlockType.ADD;
            case "Num":
                return BlockType.NUM;
            default:
                return BlockType.NUM;
        }
    }
}


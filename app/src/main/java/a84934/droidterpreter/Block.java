package a84934.droidterpreter;

import android.graphics.Rect;

public class Block {
    BlockType type;
    int width, height, color, deltaX, deltaY;
    Rect r;
    Object value;

    enum BlockType {
        ADD,
        NUM
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


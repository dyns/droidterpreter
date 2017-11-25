package a84934.droidterpreter.GraphView

import a84934.droidterpreter.BlockVals.AddBV
import a84934.droidterpreter.BlockVals.MainBV
import a84934.droidterpreter.BlockVals.MultBV
import a84934.droidterpreter.BlockVals.NumBV
import a84934.droidterpreter.Interpreter.Interpreter

fun parseBlocks(b : Block?) : Interpreter.Expr{
    if(b == null){
        throw IllegalArgumentException("")
    }

    val bVal = b.value

    return when(bVal){
        is MainBV -> {
            parseBlocks(bVal.startBlock)
        }
        is AddBV -> {
            Interpreter.Expr.AddE(
                    parseBlocks(bVal.leftBlock),
                    parseBlocks(bVal.rightBlock)
            )
        }
        is NumBV -> {
            Interpreter.Expr.NumE(bVal.number)
        }
        is MultBV -> {
            Interpreter.Expr.AddE(
                    parseBlocks(bVal.leftBlock),
                    parseBlocks(bVal.rightBlock)
            )
        }
    }

}

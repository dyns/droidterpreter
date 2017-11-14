package a84934.droidterpreter.BlockVals

import a84934.droidterpreter.Block


sealed class BlockVals
data class MainBV(var startBlock : Block?) : BlockVals()
data class AddBV(var leftBlock : Block?, var rightBlock : Block?) : BlockVals()
data class NumBV(var number : Int)



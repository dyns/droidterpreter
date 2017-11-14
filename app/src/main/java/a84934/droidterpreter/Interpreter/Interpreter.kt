package a84934.droidterpreter.Interpreter

class Interpreter {

    sealed class Expr {
        data class NumE(val n : Int) : Expr()
        data class AddE(val left : Expr, val right: Expr) : Expr()
    }

    sealed class Value {
        data class NumV(val n : Int) : Value()
    }

    fun interpret(e : Expr) : Value {
        return when(e){
            is Expr.NumE -> Value.NumV(e.n)
            is Expr.AddE -> {
                val vl = interpret(e.left)
                if( vl is Value.NumV){
                    val vr = interpret(e.right)
                    if(vr is Value.NumV){
                        Value.NumV(vl.n + vr.n)
                    } else {
                        throw Exception("right not a number")
                    }
                } else {
                    throw Exception("left not a number")
                }
            }
        }
    }

}
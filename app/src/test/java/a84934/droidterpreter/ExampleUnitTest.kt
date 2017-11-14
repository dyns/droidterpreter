package a84934.droidterpreter

import a84934.droidterpreter.Interpreter.Interpreter
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun interp_num(){
        val interp = Interpreter()


        val e = Interpreter.Expr.AddE(
                Interpreter.Expr.AddE(
                        Interpreter.Expr.NumE(3),
                        Interpreter.Expr.NumE(5)
                ),
                Interpreter.Expr.NumE(1))


        val e2 = Interpreter.Expr.AddE(
                Interpreter.Expr.NumE(11),
                Interpreter.Expr.NumE(2))

        assertEquals(Interpreter.Value.NumV(9), interp.interpret(e))
        assertEquals(Interpreter.Value.NumV(13), interp.interpret(e2))
    }

}

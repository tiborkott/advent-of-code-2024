data class ClawMachine(
    var a_x: Double = 0.0,
    var a_y: Double = 0.0,
    var b_x: Double = 0.0,
    var b_y: Double = 0.0,
    var p_x: Double = 0.0,
    var p_y: Double = 0.0
)

data class Arcade(
    var clawMachines: MutableList<ClawMachine> = mutableListOf()
){
    init {
        clawMachines = readInputLines("Day13")
            .filter { it.isNotBlank() }
            .chunked(3)
            .map { (buttonA, buttonB, prize) ->
                val (ax, ay) = buttonA.parse( """\+(\d+)""")
                val (bx, by) = buttonB.parse( """\+(\d+)""")
                val (px, py) = prize.parse("""=(\d+)""")

                ClawMachine(ax, ay, bx, by, px, py)
            }
            .toMutableList()
    }
}

fun String.parse(pattern: String): List<Double> {
    return Regex(pattern)
        .findAll(this)
        .map { it.groupValues[1].toDouble() }
        .toList()
}

/**
 * Given two linear equations:
 *  a_x * s + b_x * t = p_x
 *  a_y * s + b_y * t = p_y
 *
 * The number of times we have to press button A is 's'.
 * The number of times we have to press button B is 't'.
 * Solving the linear equation system gives:
 *  s = (p_x * b_y - p_y * b_x) / (a_x * b_y - a_y * b_x)
 *  t = (p_x - a_x * s) / b_x
 *
 */
fun ClawMachine.solve(): Pair<Long, Long> {
    val s = (p_x * b_y - p_y * b_x) / (a_x * b_y - a_y * b_x)
    val t = (p_x - a_x * s) / b_x
    /**
     * Check if both s and t are whole numbers
     * - if they are, the solution exists
     * - if they are not, the solution does not exist
     */
    if (s % 1.0 == 0.0 && t % 1.0 == 0.0) {
        return s.toLong() to t.toLong()
    }else{
        return 0L to 0L
    }
}

fun Arcade.win(): Long{
    var result = 0L
    clawMachines.forEach {
        val (a, b) = it.solve()
        result += 3 * a + b
    }
    return result
}

fun main() {

    val arcade = Arcade()

    fun part1(): Long {
        /**
         * What is the fewest tokens you would have to spend to win all possible prizes?
         */
        return arcade.win()
    }

    fun part2(): Long {
        /**
         * What is the fewest tokens you would have to spend to win all possible prizes?
         */
        arcade.clawMachines.forEach {
            it.p_x += 10000000000000L
            it.p_y += 10000000000000L
        }
        return arcade.win()
    }

    val (result1, duration1)  = measure { part1() }
    val (result2, duration2)  = measure { part2() }

    println("+-------------------------------------------")
    println("| Day 13: Claw Contraption")
    println("+-------------------------------------------")
    println("| Part 1 solution = $result1 (took $duration1 ms)")
    println("| Part 2 solution = $result2 (took $duration2 ms)")
    println("+-------------------------------------------")
}
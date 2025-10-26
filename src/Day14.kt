const val WIDTH = 101
const val HEIGHT = 103

data class Position(var x: Int, var y: Int){}
data class Velocity(var dx: Int, var dy: Int){}

data class Robot(var position: Position = Position(0,0), val velocity: Velocity = Velocity(0,0))

data class Security(
    // The robots outside the actual bathroom are in a space that is 101 tiles wide and 103 tiles tall.
    var tiles: MutableList<MutableList<Int>> = MutableList(HEIGHT){ MutableList<Int>(WIDTH){ 0 } },
    val robots: MutableList<Robot> = mutableListOf(),
    val quadrants: MutableList<Int> = MutableList(4) { 0 }
){
    init {
        readInputLines("Day14").forEach { line ->
            val (x, y, dx, dy) = line.split(" ")
                .flatMap { it.drop(2).split(",") }
                .map { it.toInt() }
            robots.add(Robot(Position(x,y), Velocity(dx,dy)))
        }
    }
}

/**
 * Calculates and updates the robot's position based on the number of seconds the robot is moving with it's velocity.
 */
fun Robot.move(seconds: Int) {
    position = Position(
        (position.x + velocity.dx * seconds).mod(WIDTH),
        (position.y + velocity.dy * seconds).mod(HEIGHT)
    )
}

fun Security.string(): String = buildString {
    tiles.forEach { row ->
        appendLine(row.joinToString("") { if (it == 0) " " else it.toString() })
    }
}

fun Security.clear() {
    repeat(HEIGHT) { row ->
        repeat(WIDTH) { col ->
            tiles[row][col] = 0
        }
    }
}

fun Security.count(): Security {
    clear()
    robots.forEach { robot ->
        tiles[robot.position.y][robot.position.x]++
    }
    return this
}

fun Security.move(seconds: Int): Security {
    robots.forEach { it.move(seconds) }
    return this
}


fun Security.quadrants(): Security {
    // First quadrant
    var firstQuadrant = 0
    var secondQuadrant = 0
    var thirdQuadrant = 0
    var fourthQuadrant = 0

    repeat(HEIGHT) { row ->
        repeat(WIDTH) { col ->
            if(row < (HEIGHT-1)/2){
                // Top side: first or second quadrant
                if(col < (WIDTH-1)/2){
                    firstQuadrant += tiles[row][col]
                }
                // col == (WIDTH-1)/2 is the middle
                if(col > (WIDTH-1)/2){
                    secondQuadrant += tiles[row][col]
                }
            }
            // row == (HEIGHT-1)/2 is the middle
            if(row > (HEIGHT-1)/2){
                // Bottom side: third or fourth quadrant
                if(col < (WIDTH-1)/2){
                    thirdQuadrant += tiles[row][col]
                }
                if(col > (WIDTH-1)/2){
                    fourthQuadrant += tiles[row][col]
                }
            }
        }
    }
    quadrants[0] = firstQuadrant
    quadrants[1] = secondQuadrant
    quadrants[2] = thirdQuadrant
    quadrants[3] = fourthQuadrant
    return this
}

fun Security.safety(): Int = quadrants.reduce(Int::times)


fun main() {


    fun part1(): Int {
        /**
         * What will the safety factor be after exactly 100 seconds have elapsed?
         */
        return Security()
            .move(100)
            .count()
            .quadrants()
            .safety()
    }

    fun part2(): Int {
        /**
         * What is the fewest number of seconds that must elapse for the robots to display the Easter egg??
         */
        val security = Security()
        var bestSafety = Int.MAX_VALUE
        var fewestSeconds = 0

        (1..10_000).forEach { second ->
            security.move(1).count().quadrants()

            val safety = security.safety()
            if (safety < bestSafety) {
                bestSafety = safety
                fewestSeconds = second
                //File("second_${second}.txt").writeText(security.string())
            }
        }
        return fewestSeconds
    }


    val (result1, duration1)  = measure { part1() }
    val (result2, duration2)  = measure { part2() }

    println("+-------------------------------------------")
    println("| Day 14: Restroom Redoubt")
    println("+-------------------------------------------")
    println("| Part 1 solution = $result1 (took $duration1 ms)")
    println("| Part 2 solution = $result2 (took $duration2 ms)")
    println("+-------------------------------------------")
}
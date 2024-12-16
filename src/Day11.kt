data class StraightLine(
    var stones: MutableList<Long> = mutableListOf(),
    val input: List<String>,
    var map: MutableMap<Pair<Long,Int>, Long> = mutableMapOf(),
)
{
    init {
        stones = input.joinToString("").split(" ").map { it.toLong() }.toMutableList()
    }
}

fun Long.digits(): Int {
    return this.toString().length
}

fun Long.split(): Pair<Long,Long>{
    val length = this.toString().length
    val first = this.toString().substring(0,length/2).toLong()
    val second = this.toString().substring(length/2)
    if(second.length >= 2){
        second.substring(0,second.length-2).trimStart { it == '0' }
    }
    return Pair(first,second.toLong())
}

fun Long.children(): List<Long>{
    val list = mutableListOf<Long>()
    if(this == 0L){
        list.add(1)
    }
    if(this.digits() % 2 == 0){
        this.split().toList().forEach {
            list.add(it)
        }
    }
    if(this != 0L && this.digits()%2 == 1){
        list.add(this*2024)
    }
    return list
}

fun StraightLine.blink() {
    val list = mutableListOf<Long>()
    stones.forEach { stone ->
        stone.children().forEach { child ->
            list.add(child)
        }
    }
    stones.clear()
    stones.addAll(list)
}


fun main() {

    fun part1(line: StraightLine): Int {
        /**
         * How many stones will you have after blinking 25 times?
         */
        repeat(25){
            line.blink()
        }
        return line.stones.size
    }

    fun part2(): Int {
        /**
         * How many stones would you have after blinking a total of 75 times?
         */
        return 0
    }

    val sl = StraightLine(input = readInputLines("Day11"))

    println("+-------------------------------------------")
    println("| Day 11: Plutonian Pebbles")
    println("+-------------------------------------------")
    println("| Part 1 solution = " + part1(sl.copy()))
    println("| Part 2 solution = " + part2())
    print("+-------------------------------------------")
}
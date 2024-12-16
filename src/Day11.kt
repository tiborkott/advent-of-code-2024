data class StraightLine(
    val input: List<String>,
    var stones: MutableList<Long> = mutableListOf(),
    var distinct: MutableMap<Long,Long> = mutableMapOf()  // Store the number of appearance for each unique stone
)
{
    init {
        stones = input
            .joinToString("")
            .split(" ")
            .map { it.toLong() }
            .toMutableList()
        distinct = input
            .joinToString("")
            .split(" ")
            .map { it.toLong() }
            .groupingBy { it }
            .eachCount()
            .toMutableMap()
            .mapValues { it.value.toLong() }
            .toMutableMap()
    }
}

fun Long.digits(): Int {
    return this.toString().length
}

fun Long.split(): Pair<Long,Long>{
    val str = this.toString()
    val length = str.length
    val first = str.substring(0, length/2).toLong()
    val second = str.substring(length/2).toLong()
    return Pair(first,second)
}

fun Long.children(): List<Long>{
    val children = mutableListOf<Long>()
    if(this == 0L){
        children.add(1L)
    }
    if(this.digits() % 2 == 0){
        this.split().toList().forEach {
            children.add(it)
        }
    }
    if(this != 0L && this.digits() % 2 == 1){
        children.add(this*2024L)
    }
    return children
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

fun StraightLine.blinkFast() {
    val new = distinct.toMutableMap()

    distinct.keys.forEach { stone ->
        stone.children().forEach { child ->
            if(new.containsKey(child)){
                new[child] = new.getValue(child) + distinct.getValue(stone)
            }else{
                new.put(child,distinct.getValue(stone))
            }
        }
        new[stone] = new.getValue(stone) - distinct.getValue(stone)
        if(new.getValue(stone) == 0L){
            new.remove(stone)
        }
    }
    distinct = new
}

fun StraightLine.count(): Long = distinct.values.sum()

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

    fun part2(line: StraightLine): Long {
        /**
         * How many stones would you have after blinking a total of 75 times?
         */
        repeat(75){
            line.blinkFast()
        }
        return line.count()
    }

    val sl = StraightLine(input = readInputLines("Day11"))

    val start1 = System.nanoTime()
    val result1 = part1(sl.copy())
    val duration1 = (System.nanoTime() - start1) / 1_000_000 // Convert to milliseconds

    val start2 = System.nanoTime()
    val result2 = part2(sl.copy())
    val duration2 = (System.nanoTime() - start2) / 1_000_000 // Convert to milliseconds

    println("+-------------------------------------------")
    println("| Day 11: Plutonian Pebbles")
    println("+-------------------------------------------")
    println("| Part 1 solution = $result1 (took $duration1 ms)")
    println("| Part 2 solution = $result2 (took $duration2 ms)")
    print("+-------------------------------------------")
}
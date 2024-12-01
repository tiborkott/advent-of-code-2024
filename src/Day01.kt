import kotlin.math.abs

fun main() {
    fun part1(firstList: MutableList<Int>, secondList: MutableList<Int>): Int {
        firstList.sort()
        secondList.sort()
        var sumOfDistances = 0
        for (i in firstList.indices) {
            sumOfDistances += abs(firstList[i] - secondList[i])
        }
        return sumOfDistances
    }

    fun part2(firstList: MutableList<Int>, secondList: MutableList<Int>): Int {
        var similarityScore = 0

        firstList.forEach{ number ->
            similarityScore += number * secondList.count { it == number }
        }

        return similarityScore
    }

    val input = readInput("Day01")
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()

    input.forEach { line ->
        firstList.add(line.split("\\s+".toRegex())[0].toInt())
        secondList.add(line.split("\\s+".toRegex())[1].toInt())
    }

    println("Part 1 solution = " + part1(firstList,secondList))
    println("Part 2 solution = " + part2(firstList,secondList))
}

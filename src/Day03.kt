
fun main() {

    fun part1(input: List<String>): Int {
        var result = 0
        Regex("""mul\(\d{1,3},\d{1,3}\)""")
            .findAll(input.joinToString())
            .map {
                it.value.removePrefix("mul(").removeSuffix(")").split(",")
            }
            .forEach {
                result += it[0].toInt() * it[1].toInt()
            }
        return result
    }

    fun part2(input: List<String>): Int {
        var isEnabled = true
        var result = 0
        Regex("""mul\(\d{1,3},\d{1,3}\)|do\(\)|don't\(\)""")
            .findAll(input.joinToString())
            .map {
                it.value
            }
            .forEach {
                when(it){
                    "do()"->isEnabled = true
                    "don't()"->isEnabled = false
                    else -> {
                        if(isEnabled){
                            val numbers = it.removePrefix("mul(").removeSuffix(")").split(",")
                            result += numbers[0].toInt() * numbers[1].toInt()
                        }
                    }
                }
            }
        return result
    }

    val input = readInput("Day03")

    println("Part 1 solution = " + part1(input))
    println("Part 2 solution = " + part2(input))
}
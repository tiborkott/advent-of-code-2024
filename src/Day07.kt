import kotlin.math.pow

data class Equation(var testValue: Long, var variables: List<Long>){}

fun Equation.solvedWithTwoOperators(): Boolean {
    val combinations = 2.0.pow(variables.size-1).toInt()
    for (combination in 0..<combinations) {
        val operations = combination.toString(2)
            .padStart(variables.size-1,'0')
            .replace('0', '+')
            .replace('1', '*')
            .toList()
        var result = 0L
        when(operations[0]){
            '+' -> { result = variables[0] + variables[1] }
            '*' -> { result = variables[0] * variables[1] }
        }
        //println("start: $result")
        operations.forEachIndexed { index, operation ->
            if(index >= 1){
                when(operation){
                    '+' -> {
                        //println("operation[$index]: $result + ${variables[index+1]}")
                        result += variables[index+1]
                    }
                    '*' -> {
                        //println("operation[$index]: $result * ${variables[index+1]}")
                        result *= variables[index+1]
                    }
                }
            }
        }
        if(result == testValue){
            //println("Equals test value! ($testValue)")
            return true
        }
    }
    //println("Not equals test value! ($testValue)")
    return false
}

fun Equation.solvedWithThreeOperators(): Boolean {
    val combinations = 3.0.pow(variables.size-1).toInt()
    for (combination in 0..<combinations) {
        val operations = combination.toString(3)
            .padStart(variables.size-1,'0')
            .replace('0', '+')
            .replace('1', '*')
            .replace('2', '|')
            .toList()
        var result = 0L
        when(operations[0]){
            '+' -> { result = variables[0] + variables[1]}
            '*' -> { result = variables[0] * variables[1]}
            '|' -> { result = variables[0].toString().plus(variables[1].toString()).toLong() }
        }
        operations.forEachIndexed { index, operation ->
            if(index >= 1){
                when(operation){
                    '+' -> {
                        //println("operation[$index]: $result + ${variables[index+1]}")
                        result += variables[index+1]
                    }
                    '*' -> {
                        //println("operation[$index]: $result * ${variables[index+1]}")
                        result *= variables[index+1]
                    }
                    '|' -> {
                        //println("operation[$index]: ${result} | ${variables[index+1]}")
                        result = result.toString().plus(variables[index+1].toString()).toLong()
                    }
                }
            }
        }

        if(result == testValue){
            //println("Equals test value! ($testValue)")
            return true
        }
    }
    //println("Not equals test value! ($testValue)")
    return false
}

fun main() {

    fun part1(equations: MutableList<Equation>): Long {
        /**
         * What is their total calibration result?
         */
        var totalCalibrationResult = 0L
        equations.forEach { equation ->
            if(equation.solvedWithTwoOperators()){
                totalCalibrationResult+=equation.testValue
            }
        }
        return totalCalibrationResult
    }

    fun part2(equations: MutableList<Equation>): Long {
        /**
         * What is their total calibration result?
         */
        var totalCalibrationResult = 0L
        equations.forEach { equation ->
            if(equation.solvedWithThreeOperators()){
                totalCalibrationResult+=equation.testValue
            }
        }
        return totalCalibrationResult
    }

    val input = readInputLines("Day07")
    val equations = mutableListOf<Equation>()
    input.forEach {
        equations += Equation(
            testValue = it.split(":")[0].toLong(),
            variables = it.split(":")[1].removePrefix(" ").split(" ").map { it.toLong() }
        )
    }

    println("Day 7: Bridge Repair")
    println("Part 1 solution = " + part1(equations))
    println("Part 2 solution = " + part2(equations))

}
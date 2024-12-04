import kotlin.math.abs

fun main() {

    fun part1(reports: List<Report>): Int {
        // How many reports are safe?
        var numberOfSafeReports = 0
        reports.forEach {
            if(it.isSafe()){
                numberOfSafeReports++
            }
        }
        return numberOfSafeReports
    }

    fun part2(reports: List<Report>): Int {
        // How many reports are now safe?
        var numberOfSafeReports = 0
        reports.forEach {
            if(it.isProblemDampenerlySafe()){
                numberOfSafeReports++
            }
        }
        return numberOfSafeReports
    }

    val input = readInputLines("Day02")
    val reports = mutableListOf<Report>()
    input.forEach { line ->
        val report = Report()
        line.split("\\s+".toRegex()).forEach{ report.levels.add(it.toInt()) }
        reports.add(report)
    }

    println("Part 1 solution = " + part1(reports.toList()))
    println("Part 2 solution = " + part2(reports.toList()))
}

data class Report(var levels: MutableList<Int> = mutableListOf()) {}

fun Report.isProblemDampenerlySafe(): Boolean {
    if(isSafe()){
        return true
    }else{
        for (index in 0 until levels.size) {
            val savedLevel = levels[index]
            levels.removeAt(index)

            if(isSafe()){
                return true
            }else{
                levels.add(index, savedLevel)
            }
        }
    }
    return false
}

fun Report.isSafe(): Boolean {
    return (isAllIncreasing() or isAllDecreasing()) and isDifferenceWithinThreshold()
}

fun Report.isAllIncreasing(): Boolean {
    for (index in 0 until levels.size - 1) {
        if(levels[index] >= levels[index + 1]){
            return false
        }
    }
    return true
}

fun Report.isAllDecreasing(): Boolean {
    for (index in 0 until levels.size - 1) {
        if(levels[index] <= levels[index + 1]){
            return false
        }
    }
    return true
}

fun Report.isDifferenceWithinThreshold(): Boolean {
    for (index in 0 until levels.size - 1) {
        val diff = abs(levels[index] - levels[index + 1])
        if((diff > 3) or (diff < 1)){
            return false
        }
    }
    return true
}
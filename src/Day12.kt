
data class Garden(
    val size: Int,
    var garden: MutableList<MutableList<Char>> = MutableList(size){
        MutableList<Char>(size){' '}
    },
    var plots: MutableList<MutableList<Plot>> = MutableList(size){
        MutableList<Plot>(size){
            Plot('_',0,0)
        }
    }
)
{
    init {
        val input = readInputLines("Day12")
        for (i in input.indices){
            for (j in input.indices){
                garden[i][j] = input[i][j]
                plots[i][j] = Plot(input[i][j],i, j)
            }
        }
    }
}


data class Plot(
    val char: Char,
    var y: Int,
    var x: Int,
    var perimeter: Int = 0,
    var region: Int = 0,
    var sides: MutableList<Direction> = mutableListOf()
)

fun Garden.calculatePerimeter(plot: Plot) {
    val char = garden[plot.y][plot.x]

    if (plot.y - 1 < 0 || garden[plot.y-1][plot.x] != char) {
        plot.perimeter++
        plot.sides.add(Direction.UP)
    }

    if (plot.y + 1 >= size || garden[plot.y+1][plot.x] != char) {
        plot.perimeter++
        plot.sides.add(Direction.DOWN)
    }

    if (plot.x - 1 < 0 || garden[plot.y][plot.x-1] != char) {
        plot.perimeter++
        plot.sides.add(Direction.LEFT)
    }

    if (plot.x + 1 >= size || garden[plot.y][plot.x+1] != char) {
        plot.perimeter++
        plot.sides.add(Direction.RIGHT)
    }
}

fun Garden.checkSides(plot: Plot): List<Plot> {
    val char = plot.char
    val connectablePlots = mutableListOf<Plot>()

    if(plot.y - 1 >= 0 && plots[plot.y - 1][plot.x].char == char && plots[plot.y - 1][plot.x].region == 0) {
        connectablePlots.add(plots[plot.y - 1][plot.x])
    }

    if(plot.y + 1 < size && plots[plot.y + 1][plot.x].char == char && plots[plot.y + 1][plot.x].region == 0){
        connectablePlots.add(plots[plot.y + 1][plot.x])
    }

    if(plot.x - 1 >= 0 && plots[plot.y][plot.x - 1].char == char && plots[plot.y][plot.x - 1].region == 0){
        connectablePlots.add(plots[plot.y][plot.x - 1])
    }


    if(plot.x + 1 < size && plots[plot.y][plot.x + 1].char == char && plots[plot.y][plot.x + 1].region == 0){
        connectablePlots.add(plots[plot.y][plot.x + 1])
    }

    return connectablePlots
}

/**
 * The area of a region is simply the number of garden plots the region contains.
 */
fun Garden.area(region: Int): Int {
    var sum = 0
    for (i in plots.indices){
        for (j in plots.indices) {
            if (plots[i][j].region == region) {
                sum++
            }
        }
    }
    return sum
}

/**
 * The perimeter of a region is the number of sides of garden plots in the region that do not touch another garden plot in the same region.
 */
fun Garden.perimeter(region: Int): Int {
    var sum = 0
    for (i in plots.indices){
        for (j in plots.indices) {
            if (plots[i][j].region == region) {
                sum += plots[i][j].perimeter
            }
        }
    }
    return sum
}


fun Garden.sides(region: Int): Int{
    val prevDirections: MutableSet<Direction> = mutableSetOf()
    var upSides = 0
    var downSides = 0
    var leftSides = 0
    var rightSides = 0

    for (i in plots.indices){
        for (j in plots.indices) {
            if(plots[i][j].sides.contains(Direction.DOWN) && plots[i][j].region == region){
                if(prevDirections.isEmpty() || !prevDirections.contains(Direction.DOWN)){
                    downSides += 1
                    prevDirections.add(Direction.DOWN)
                }
            }else{
                prevDirections.remove(Direction.DOWN)
            }

            if(plots[i][j].sides.contains(Direction.UP) && plots[i][j].region == region){
                if(prevDirections.isEmpty() || !prevDirections.contains(Direction.UP)){
                    upSides += 1
                    prevDirections.add(Direction.UP)
                }
            }else{
                prevDirections.remove(Direction.UP)
            }
        }

    }
    prevDirections.clear()

    for (i in plots.indices){
        for (j in plots.indices) {
            if(plots[j][i].sides.contains(Direction.LEFT) && plots[j][i].region == region){
                if(prevDirections.isEmpty() || !prevDirections.contains(Direction.LEFT)){
                    leftSides += 1
                    prevDirections.add(Direction.LEFT)
                }
            }else{
                prevDirections.remove(Direction.LEFT)
            }

            if(plots[j][i].sides.contains(Direction.RIGHT) && plots[j][i].region == region){
                if(prevDirections.isEmpty() || !prevDirections.contains(Direction.RIGHT)){
                    rightSides += 1
                    prevDirections.add(Direction.RIGHT)
                }
            }else{
                prevDirections.remove(Direction.RIGHT)
            }
        }
    }

    return upSides + downSides + leftSides + rightSides
}

/**
 * Discovers a region on the current position.
 * Check all sides if the plot on a side is connectable to add it to the region.
 * Do this while any of the plots in the current region has a connectable side.
 */
fun Garden.discoverRegion(plot: Plot, regionId: Int){
    val currentRegion = mutableListOf<Plot>()
    plot.region = regionId
    currentRegion.add(plot)  // add a starting plot

    var condition = currentRegion.all { checkSides(it).isEmpty() }
    while(!condition)
    {
        val newPlots = mutableListOf<Plot>()
        currentRegion.forEach { plot ->
            checkSides(plot).forEach{
                it.region = regionId
                newPlots.add(it)
            }
        }
        newPlots.forEach { currentRegion.add(it) }

        condition = currentRegion.all { checkSides(it).isEmpty() }
    }
}

/**
 * Discovers all regions.
 * Go over all plots, if a plot does not have a region, discover a new region.
 * Do this until every plot has a region assigned.
 */
fun Garden.discoverAllRegions(): Int {
    var regionId = 1
    var start = plots[0][0]
    while(plots.flatten().any { it.region == 0 }){
        discoverRegion(start,regionId)
        start = nextWithoutRegion() ?: return regionId
        regionId++
    }
    return regionId
}

/**
 * Searches for a plot that has a region id of 0.
 * If it does not find such a plot, then returns null.
 */
fun Garden.nextWithoutRegion(): Plot? {
    for (i in plots.indices){
        for (j in plots.indices){
            if(plots[i][j].region == 0){
                return plots[i][j]
            }
        }
    }
    return null
}

fun main() {

    fun part1(): Long {
        /**
         * What is the total price of fencing all regions on your map?
         */
        var price = 0L
        val massiveFarm = Garden(size = 140)
        massiveFarm.plots.forEach{
            it.forEach {
                massiveFarm.calculatePerimeter(it)
            }
        }
        val regions = massiveFarm.discoverAllRegions()
        for (i in 1..regions){
            price += (massiveFarm.perimeter(i)*massiveFarm.area(i))
            //println("Region $i has perimeter ${massiveFarm.perimeter(i)}, area ${massiveFarm.area(i)} and price = ${(massiveFarm.perimeter(i)*massiveFarm.area(i))}")
        }

        return price
    }

    fun part2(): Long {
        /**
         * What is the new total price of fencing all regions on your map?
         */
        var price = 0L
        val massiveFarm = Garden(size = 140)
        massiveFarm.plots.forEach{
            it.forEach {
                massiveFarm.calculatePerimeter(it)
            }
        }
        val regions = massiveFarm.discoverAllRegions()
        for (i in 1..regions){
            val sides = massiveFarm.sides(i)
            val area = massiveFarm.area(i)
            price += (sides * area)
        }

        return price
    }


    val (result1, duration1)  = measure { part1() }
    val (result2, duration2)  = measure { part2() }

    println("+-------------------------------------------")
    println("| Day 12: Garden Groups")
    println("+-------------------------------------------")
    println("| Part 1 solution = $result1 (took $duration1 ms)")
    println("| Part 2 solution = $result2 (took $duration2 ms)")
    println("+-------------------------------------------")
}
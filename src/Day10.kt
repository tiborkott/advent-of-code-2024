enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);
}

data class TopographicMap(
    var input: List<String>,
    var data: MutableList<MutableList<Int>> = MutableList(input.size){ MutableList(input.size){ 0 } },
    var trailHeads: MutableList<Location> = mutableListOf(),
    val graph: MutableMap<Location,MutableList<Location>> = mutableMapOf(),
    var scores: Int = 0,
    var ratings: Int = 0,
){
    init {
        for (i in input.indices){
            for (j in input.indices){
                data[i][j] = input[i][j].digitToInt()
            }
        }
    }
}

fun TopographicMap.findHeads() {
    for (i in input.indices){
        for (j in input.indices){
            if(data[i][j] == 0){
                trailHeads.add(Location(j,i))
            }

        }
    }
}

fun TopographicMap.buildGraph(){
    /**
     * This function builds a graph representation of the map, containing only the valid step directions as edges.
     */
    for (i in input.indices){
        for (j in input.indices){
            val vertex = Location(j,i)
            val neighbors = mutableListOf<Location>()
            Direction.entries.forEach { direction ->
                if(
                    vertex.x + direction.dx >= 0 && vertex.x + direction.dx < input.size &&
                    vertex.y + direction.dy >= 0 && vertex.y + direction.dy < input.size &&
                    data[vertex.y][vertex.x] + 1 == data[vertex.y+direction.dy][vertex.x+direction.dx]
                ){
                    neighbors.add(Location(vertex.x+direction.dx, vertex.y+direction.dy))
                }
            }
            graph[vertex] = neighbors
        }
    }
}

fun TopographicMap.calculateScores() {
    trailHeads.forEach { head ->
        val visited = mutableListOf<Location>()
        depthFirstSearch(head,visited)
    }
}

fun TopographicMap.calculateRatings() {
    val paths = mutableListOf<List<Location>>()
    trailHeads.forEach { head ->
        val visited = mutableListOf<Location>()
        depthFirstSearchWithBacktrack(head,visited,paths)
    }
    ratings = paths.size
}

fun TopographicMap.depthFirstSearch(
    start: Location,
    visited: MutableList<Location>
){
    visited.add(start)

    if(data[start.y][start.x] == 9){
        scores++
    }
    for (neighbor in graph[start]!!) {
        if (!visited.contains(neighbor)) {
            depthFirstSearch(neighbor, visited)
        }
    }
}

fun TopographicMap.depthFirstSearchWithBacktrack(
    start: Location,
    visited: MutableList<Location>,
    paths: MutableList<List<Location>>
) {
    visited.add(start)

    if (data[start.y][start.x] == 9) {
        paths.add(visited.toList())
    }

    for (neighbor in graph[start]!!) {
        if (!visited.contains(neighbor)) {
            depthFirstSearchWithBacktrack(neighbor, visited, paths)
        }
    }

    visited.remove(start)
}

fun main() {

    fun part1(topographicMap: TopographicMap): Int {
        /**
         * What is the sum of the scores of all trailheads on your topographic map?
         *
         * Score: A trailhead's score is the number of 9-height positions reachable from that trailhead via a hiking trail.
         */
        topographicMap.findHeads()
        topographicMap.buildGraph()
        topographicMap.calculateScores()
        return topographicMap.scores
    }

    fun part2(topographicMap: TopographicMap): Int {
        /**
         * What is the sum of the ratings of all trailheads?
         *
         * Rating: A trailhead's rating is the number of distinct hiking trails which begin at that trailhead.
         */
        topographicMap.calculateRatings()
        return topographicMap.ratings
    }

    val topographicMap = TopographicMap(readInputLines("Day10"))

    println("+-------------------------------------------")
    println("| Day 10: Hoof It")
    println("+-------------------------------------------")
    println("| Part 1 solution = " + part1(topographicMap))
    println("| Part 2 solution = " + part2(topographicMap))
    println("+-------------------------------------------")
}
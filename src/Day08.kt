data class Location(var x: Int, var y: Int){}

data class City(
    var input: List<String>,
    var data: MutableList<MutableList<Char>> = MutableList(input.size){ MutableList<Char>(input.size){' '} },
    var frequencyTypes: MutableSet<Char> = mutableSetOf(),
    var frequencyLocations: MutableMap<Char,MutableSet<Location>> = mutableMapOf(),
    var antinodes: Int  = 0
){
    init {
        for (i in input.indices){
            for (j in input.indices){
                data[i][j] = input[i][j]
            }
        }
    }
}

fun main() {

    fun part1(city: City): Int {
        /**
         * How many unique locations within the bounds of the map contain an antinode?
         */
        //city.print()
        //println()
        city.discoverFrequencies()
        city.countAntinodesLimited()
        //city.print()
        return city.antinodes
    }

    fun part2(city: City): Int {
        /**
         * How many unique locations within the bounds of the map contain an antinode?
         */
        //city.print()
        //println()
        city.discoverFrequencies()
        city.countAntinodesUnLimited()
        //city.print()
        return city.antinodes
    }

    val city = City(readInputLines("Day08"))


    println()

    println("Day 8: Resonant Collinearity")
    println("Part 1 solution = " + part1(city))
    println("Part 2 solution = " + part2(city))
}

fun City.print(){
    for (i in input.indices){
        for (j in input.indices){
            print(data[i][j])
        }
        println()
    }
}

fun City.discoverFrequencies(){

    for (i in input.indices){
        for (j in input.indices) {
            val frequency = input[i][j]

            if (!frequencyTypes.contains(frequency) && frequency != '.') {
                frequencyTypes.add(frequency)
            }

            if (frequency != '.') {
                if (!frequencyLocations.containsKey(frequency)) {
                    frequencyLocations.put(
                        key = frequency,
                        value = mutableSetOf(Location(i,j))
                    )
                } else {
                    val currentSet = frequencyLocations[frequency]
                    currentSet!!.add(Location(i,j))
                    frequencyLocations.put(
                        key = frequency,
                        value = currentSet
                    )
                }
            }
        }
    }
}

fun City.countAntinodesLimited() {
    val height = input.size
    val width = input.first().length
    frequencyLocations.forEach { frequency ->
        frequency.value.forEach { location ->
            frequency.value.forEach { otherLocation ->
                if(location != otherLocation){
                    val direction = Pair(otherLocation.x - location.x,otherLocation.y - location.y)
                    if(
                        otherLocation.x + direction.first >= 0 &&
                        otherLocation.y + direction.second >= 0 &&
                        otherLocation.x + direction.first < width &&
                        otherLocation.y + direction.second < height &&
                        (
                            data[otherLocation.x + direction.first][otherLocation.y + direction.second] == '.' ||
                            frequencyTypes.any { it ==  data[otherLocation.x + direction.first][otherLocation.y + direction.second]}
                        )
                    ) {
                        val antinode = Location(otherLocation.x + direction.first,otherLocation.y + direction.second)
                        if(frequencyTypes.any { it == data[antinode.x][antinode.y] }) {
                            antinodes++
                        }else{
                            data[antinode.x][antinode.y] = '#'
                        }
                    }
                }
            }
        }
    }
    data.forEach {
        it.forEach {
            if(it == '#'){
                antinodes++
            }
        }
    }
}

fun City.countAntinodesUnLimited() {
    val map = data
    val height = input.size
    val width = input.first().length
    antinodes = 0
    frequencyLocations.forEach { frequency ->
        frequency.value.forEach { location ->
            frequency.value.forEach { otherLocation ->
                if(location != otherLocation){
                    val direction = Pair(otherLocation.x - location.x,otherLocation.y - location.y)
                    map[location.x][location.y] = '#'
                    map[otherLocation.x][otherLocation.y] = '#'

                    var loc = Location(otherLocation.x + direction.first, otherLocation.y + direction.second)
                    while( loc.x >= 0 && loc.y >= 0 && loc.x < width && loc.y < height &&
                        (
                         data[loc.x][loc.y] == '.' ||
                         frequencyTypes.any { it ==  data[loc.x][loc.y]} ||
                         data[loc.x][loc.y] == '#'
                        )
                    ){
                        map[loc.x][loc.y] = '#'
                        loc = Location(loc.x + direction.first, loc.y + direction.second)
                    }
                }
            }
        }
    }
    data.forEach {
        it.forEach {
            if(it == '#'){
                antinodes++
            }
        }
    }
}
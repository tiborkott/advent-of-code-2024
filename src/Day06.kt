data class Map(
    var input: List<String>,
    var data: MutableList<MutableList<Char>> = MutableList(input.size){ MutableList<Char>(input.size){' '} },
    val guard: List<Char> = listOf('>','<','V','^')
){
    init {
        for (i in input.indices){
            for (j in input.indices){
                data[i][j] = input[i][j]
            }
        }
    }
}

fun Map.reload(){
    input = readInputLines("Day06")
    for (i in input.indices){
        for (j in input.indices){
            data[i][j] = input[i][j]
        }
    }
}

fun Map.start(): Pair<Int,Int>{
    var start = Pair(0, 0)
    for (i in data.indices){
        for (j in data.indices){
            if(guard.any { it == data[i][j]}){
                start = Pair(i,j)  // (y,x)
            }
        }
    }
    return start
}

fun Map.escape(): Boolean {
    var repetition = 0
    var escaped = false
    var start = start()
    while(true){
        if(repetition > data.size*data.size){
            break
        }
        when(data[start.first][start.second]){
            '>' -> {
                if(start.second+1 >= data.size){
                    // we are out if the index would run out of the boundaries
                    data[start.first][start.second] = 'X'
                    escaped = true
                    break
                }
                if(data[start.first][start.second+1] == '#' || data[start.first][start.second+1] == 'O'){
                    // turn right
                    data[start.first][start.second] = 'V'
                }else{
                    if(data[start.first][start.second+1] == 'X'){
                        repetition++
                    }
                    data[start.first][start.second+1] = '>'
                    data[start.first][start.second] = 'X'

                    start = Pair(start.first,start.second+1)

                }
            }
            '<' -> {
                if(start.second-1 < 0){
                    // we are out if the index would run out of the boundaries
                    data[start.first][start.second] = 'X'
                    escaped = true
                    break
                }

                if(data[start.first][start.second-1] == '#' || data[start.first][start.second-1] == 'O'){
                    // turn right
                    data[start.first][start.second] = '^'
                }else{
                    if(data[start.first][start.second-1] == 'X'){
                        repetition++
                    }
                    data[start.first][start.second-1] = '<'
                    data[start.first][start.second] = 'X'
                    start = Pair(start.first,start.second-1)
                }
            }
            'V' -> {
                if(start.first+1 >= data.size){
                    // we are out if the index would run out of the boundaries
                    data[start.first][start.second] = 'X'
                    escaped = true
                    break
                }
                if(data[start.first+1][start.second] == '#' || data[start.first+1][start.second] == 'O'){
                    // turn right
                    data[start.first][start.second] = '<'
                }else{
                    if(data[start.first+1][start.second] == 'X'){
                        repetition++
                    }
                    data[start.first+1][start.second] = 'V'
                    data[start.first][start.second] = 'X'
                    start = Pair(start.first+1,start.second)
                }
            }
            '^' -> {
                if(start.first-1 < 0){
                    // we are out if the index would run out of the boundaries
                    data[start.first][start.second] = 'X'
                    escaped = true
                    break
                }
                if(data[start.first-1][start.second] == '#' || data[start.first-1][start.second] == 'O'){
                    // turn right
                    data[start.first][start.second] = '>'
                }else{
                    if(data[start.first-1][start.second] == 'X'){
                        repetition++
                    }
                    data[start.first-1][start.second] = '^'
                    data[start.first][start.second] = 'X'
                    start = Pair(start.first-1,start.second)
                }
            }
        }
    }
    return escaped
}

fun main() {

    fun part1(map: Map): Int {
        // How many distinct positions will the guard visit before leaving the mapped area?
        var start = map.start()
        val positions = mutableListOf<Pair<Int,Int>>()
        while(true){
            when(map.data[start.first][start.second]){
                '>' -> {
                    if(start.second+1 >= map.data.size){
                        // we are out if the index would run out of the boundaries
                        map.data[start.first][start.second] = 'X'
                        positions+=start
                        break
                    }
                    if(map.data[start.first][start.second+1] == '#'){
                        // turn right
                        map.data[start.first][start.second] = 'V'
                    }else{
                        map.data[start.first][start.second+1] = '>'
                        map.data[start.first][start.second] = 'X'
                        positions+=start
                        start = Pair(start.first,start.second+1)
                    }
                }
                '<' -> {
                    if(start.second-1 < 0){
                        // we are out if the index would run out of the boundaries
                        map.data[start.first][start.second] = 'X'
                        positions+=start
                        break
                    }
                    if(map.data[start.first][start.second-1] == '#'){
                        // turn right
                        map.data[start.first][start.second] = '^'
                    }else{
                        map.data[start.first][start.second-1] = '<'
                        map.data[start.first][start.second] = 'X'
                        positions+=start
                        start = Pair(start.first,start.second-1)
                    }
                }
                'V' -> {
                    if(start.first+1 >= map.data.size){
                        // we are out if the index would run out of the boundaries
                        map.data[start.first][start.second] = 'X'
                        positions+=start
                        break
                    }
                    if(map.data[start.first+1][start.second] == '#'){
                        // turn right
                        map.data[start.first][start.second] = '<'
                    }else{
                        map.data[start.first+1][start.second] = 'V'
                        map.data[start.first][start.second] = 'X'
                        positions+=start
                        start = Pair(start.first+1,start.second)
                    }
                }
                '^' -> {
                    if(start.first-1 < 0){
                        // we are out if the index would run out of the boundaries
                        map.data[start.first][start.second] = 'X'
                        positions+=start
                        break
                    }
                    if(map.data[start.first-1][start.second] == '#'){
                        // turn right
                        map.data[start.first][start.second] = '>'
                    }else{
                        map.data[start.first-1][start.second] = '^'
                        map.data[start.first][start.second] = 'X'
                        positions+=start
                        start = Pair(start.first-1,start.second)
                    }
                }
            }
        }
        return positions.distinct().size
    }

    fun part2(map: Map): Int {
        //How many different positions could you choose for this obstruction?
        var obstructions = 0

        for (i in map.data.indices){
            for (j in map.data.indices){
                if(map.data[i][j] != '#' && map.guard.none { it == map.data[i][j]} && map.data[i][j] != 'O' ){
                    map.data[i][j] = 'O'
                    val escaped = map.escape()
                    if(!escaped){
                        obstructions++
                    }
                    map.reload()
                }
            }
        }
        return obstructions
    }

    val map = Map(readInputLines("Day06"))


    println("Part 1 solution = " + part1(map = map.copy()))
    println("Part 2 solution = " + part2(map = map.copy()))
}
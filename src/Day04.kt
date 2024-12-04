data class Matrix(var data: MutableList<MutableList<Char>> = MutableList(140){ MutableList<Char>(140){' '} }){
    init {
        val input = readInputLines("Day04")
        for (i in input.indices){
            for (j in input.indices){
                data[i][j] = input[i][j]
            }
        }
    }
}


fun main() {

    fun part1(): Int {
        return Matrix().getXMAS()
    }

    fun part2(): Int {
        return Matrix().getX_MAS()
    }

    println("Part 1 solution = " + part1())
    println("Part 2 solution = " + part2())

    fun part1_trouble(): Int{
        var counter = 0
        val matrix = Matrix()
        val rows = matrix.getRows()
        val columns = matrix.getColumns()
        val diagonals = matrix.getDiagonals()
        rows.forEach { row ->
            Regex("""XMAS""")
                .findAll(row)
                .map {
                    it.value
                }.forEach { _ ->
                    counter++
                }
            Regex("""XMAS""")
                .findAll(row.reversed())
                .map {
                    it.value
                }.forEach { _ ->
                    counter++
                }
        }
        columns.forEach { column ->
            Regex("""XMAS""")
                .findAll(column)
                .map {
                    it.value
                }.forEach { _ ->
                    counter++
                }
            Regex("""XMAS""")
                .findAll(column.reversed())
                .map {
                    it.value
                }.forEach { _ ->
                    counter++
                }
        }
        diagonals.forEach { diagonal ->
            Regex("""XMAS""")
                .findAll(diagonal)
                .map {
                    it.value
                }.forEach { _ ->
                    counter++
                }
            Regex("""XMAS""")
                .findAll(diagonal.reversed())
                .map {
                    it.value
                }.forEach { _ ->
                    counter++
                }
        }
        return counter
    }
}

fun Matrix.getXMAS(): Int {
    var counter = 0

    for (i in data.indices){
        for (j in data.indices){
            if(data[i][j] == 'X'){
                // Left
                if(j-3 >= 0){
                    val string = listOf(data[i][j-3],data[i][j-2],data[i][j-1],data[i][j]).joinToString("")
                    if(string == "XMAS" || string == "SAMX"){
                        counter++
                    }
                }
                // Rigth
                if(j+3 < data.size){
                    val string = listOf(data[i][j],data[i][j+1],data[i][j+2],data[i][j+3]).joinToString("")
                    if(string == "XMAS" || string == "SAMX"){
                        counter++
                    }
                }
                // Up
                if(i-3>=0){
                    val string = listOf(data[i][j],data[i-1][j],data[i-2][j],data[i-3][j]).joinToString("")
                    if(string == "XMAS" || string == "SAMX"){
                        counter++
                    }
                }
                // Down
                if(i+3<data.size){
                    val string = listOf(data[i][j],data[i+1][j],data[i+2][j],data[i+3][j]).joinToString("")
                    if(string == "XMAS" || string == "SAMX"){
                        counter++
                    }
                }

                // Down Right
                if((i+3<data.size) and (j+3<data.size)){
                    val string = listOf(data[i][j],data[i+1][j+1],data[i+2][j+2],data[i+3][j+3]).joinToString("")
                    if(string == "XMAS" || string == "SAMX"){
                        counter++
                    }
                }

                // Down Left
                if((i+3<data.size) and (j-3>=0)) {
                    val string = listOf(data[i][j],data[i+1][j-1],data[i+2][j-2],data[i+3][j-3]).joinToString("")
                    if(string == "XMAS" || string == "SAMX"){
                        counter++
                    }
                }
                // Up Right
                if((i-3>=0) and (j+3<data.size)){
                    val string = listOf(data[i][j],data[i-1][j+1],data[i-2][j+2],data[i-3][j+3]).joinToString("")
                    if(string == "XMAS" || string == "SAMX"){
                        counter++
                    }
                }
                // Up Left
                if((i-3>=0) and (j-3>=0)){
                    val string = listOf(data[i][j],data[i-1][j-1],data[i-2][j-2],data[i-3][j-3]).joinToString("")
                    if(string == "XMAS" || string == "SAMX"){
                        counter++
                    }
                }

            }
        }
    }
    return counter
}

fun Matrix.getX_MAS(): Int {
    var counter = 0

    for (i in data.indices){
        for (j in data.indices){
            if(data[i][j] == 'A'){
                if((i-1 >= 0) and (j-1 >=0) and
                   (i-1 >= 0) and (j+1 < data.size) and
                   (i+1 < data.size) and (j-1 >=0) and
                   (i+1 < data.size) and (j+1 < data.size)
                ) {
                    /*
                    | M M | M S | S S | S M |
                    |  A  |  A  |  A  |  A  |
                    | S S | M S | M M | S M |
                    */
                    if(
                        (data[i-1][j-1] == 'M' &&  data[i+1][j-1] == 'M' && data[i-1][j+1] == 'S' && data[i+1][j+1] == 'S')
                        ||
                        (data[i-1][j-1] == 'M' && data[i-1][j+1] == 'M' && data[i+1][j-1] == 'S' && data[i+1][j+1] == 'S')
                        ||
                        (data[i-1][j-1] == 'S' &&  data[i+1][j-1] == 'S' && data[i-1][j+1] == 'M' && data[i+1][j+1] == 'M')
                        ||
                        (data[i-1][j-1] == 'S' && data[i-1][j+1] == 'S' && data[i+1][j-1] == 'M' && data[i+1][j+1] == 'M')
                    ){
                        counter++
                    }

                }
            }
        }
    }
    return counter
}

fun Matrix.getRows(): List<String>{
    val list = mutableListOf<String>()
    for (i in data.indices){
        list.add(data[i].joinToString(separator = ""))
    }
    return list
}

fun Matrix.getColumns(): List<String>{
    val list = mutableListOf<String>()
    for (i in data.indices){
        var column = ""
        for (j in data.indices){
            column += data[j][i]
        }
        list.add(column)
    }
    return list
}

fun Matrix.getDiagonals(): List<String>{

    val diagonals = mutableListOf<List<Char>>()

    val rows = data.size
    val cols = if (rows > 0) data[0].size else 0

    // Top-left to bottom-right diagonals
    for (d in 0 until (rows + cols - 1)) {
        val diagonal = mutableListOf<Char>()
        for (i in 0 until rows) {
            val j = d - i
            if (j in 0 until cols) {
                diagonal.add(data[i][j])
            }
        }
        if (diagonal.size > 3) {
            diagonals.add(diagonal)
        }
    }

    // Top-right to bottom-left diagonals
    for (d in 0 until (rows + cols - 1)) {
        val diagonal = mutableListOf<Char>()
        for (i in 0 until rows) {
            val j = i - d + (cols - 1)
            if (j in 0 until cols) {
                diagonal.add(data[i][j])
            }
        }
        if (diagonal.size > 3) {
            diagonals.add(diagonal)
        }
    }

    return diagonals.toList().map { it.joinToString("") }
}



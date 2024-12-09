data class FileSystem(
    val diskMap: String,
    var fileBlocks: MutableList<String> = mutableListOf(),
    val listOfIDs: MutableList<String> = mutableListOf()
)

fun FileSystem.process(){
    var fileID = 0
    var isFile = true
    listOfIDs.add(0.toString())
    diskMap.forEach {
        for (i in 0..< it.digitToInt()) {
            if(isFile){
                fileBlocks.add(fileID.toString())
            }else{
                fileBlocks.add(".")
            }
        }
        if(isFile){
            fileID++
            listOfIDs.add(fileID.toString())
        }
        isFile = !isFile
    }
}

fun FileSystem.compact(){
    while(!noGaps()){
        val lastNumber = fileBlocks.findLast { it != "." }
        val firstDot = fileBlocks.firstOrNull { it == "." }
        if(lastNumber != null && firstDot != null){
            val firstIndex = fileBlocks.indexOf(firstDot)
            val lastIndex = fileBlocks.lastIndexOf(lastNumber)
            fileBlocks[firstIndex] = lastNumber
            fileBlocks[lastIndex] = firstDot
        }
    }
}

fun FileSystem.compactWithoutFragmentation(){
    // drop last, because at processing it adds an extra ID at the end
    listOfIDs.dropLast(1).reversed().forEach { id ->

        val fileRange = mutableListOf<Int>()
        fileBlocks.forEachIndexed { index, block ->
            if(block == id){
                fileRange.add(index)
            }
        }

        val dotRange = mutableListOf<Int>()
        for(i in 0..fileBlocks.lastIndex){
            if(fileBlocks[i] == "." ){
                dotRange.add(i)
            }
            if(fileBlocks[i] != "." && i > 0 && fileBlocks[i-1] == "."){
                // the ... range just ended, if file fits in ... try to fit it
                if(dotRange.size >= fileRange.size && dotRange.all { dot -> fileRange.all { it > dot } }) {
                    // move
                    for (j in 0..< fileRange.size) {
                        fileBlocks[dotRange[j]] = fileBlocks[fileRange[j]]
                        fileBlocks[fileRange[j]] = "."
                    }
                    dotRange.clear()
                    break
                }else{
                    // can't fit, clear dot range!!
                    dotRange.clear()
                }
            }
        }
        // the file didn't fit, clear it from range
        fileRange.clear()
    }

}

fun FileSystem.noGaps():Boolean{
    return fileBlocks.dropLastWhile { it == "." }.none { it == "." }
}

fun FileSystem.checksum(): Long{
    var checksum = 0L
    fileBlocks.dropLastWhile { it == "." }.forEachIndexed { index, block ->
        // skip "." block in checksum
        if(block != "."){
            checksum += (index * block.toLong())
        }
    }
    return checksum
}

fun main() {

    fun part1(diskMap: String): Long {
        /**
         * What is the resulting filesystem checksum?
         */
        val fileSystem = FileSystem(diskMap)
        fileSystem.process()
        fileSystem.compact()
        return fileSystem.checksum()
    }

    fun part2(diskMap: String): Long {
        /**
         *  What is the resulting filesystem checksum?
         */
        val fileSystem = FileSystem(diskMap)
        fileSystem.process()
        fileSystem.compactWithoutFragmentation()
        return fileSystem.checksum()
    }

    val input = readInputLines("Day09")[0]

    println("Day 9: Disk Fragmenter")
    println()
    println("Part 1 solution = " + part1(input))
    println("Part 2 solution = " + part2(input))
}
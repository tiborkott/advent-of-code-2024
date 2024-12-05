fun main() {

    val rules = mutableListOf<Pair<Int,Int>>()
    val pages = mutableListOf<MutableList<Int>>()
    val incorrectlyOrderedPages = mutableListOf<MutableList<Int>>()

    fun part1(rules: MutableList<Pair<Int,Int>>, pages: MutableList<MutableList<Int>>): Int {
        var counter = 0
        pages.forEach { page ->
            if(page.size == page.distinct().size){
                var valid = true
                rules.forEach { rule ->
                    if(page.contains(rule.first) and page.contains(rule.second)){
                        if(page.indexOf(rule.first) > page.indexOf(rule.second)){
                            valid = false
                        }
                    }
                }
                if(valid){
                    counter += page[page.size/2]
                }else{
                    incorrectlyOrderedPages += page
                }
            }
        }
        return counter
    }

    fun part2(rules: MutableList<Pair<Int,Int>>, pages: MutableList<MutableList<Int>>): Int {
        var counter = 0
        pages.forEach { page ->
            /*
            If the page was not valid.
            While there are any rules broken, swap the elements contained in the broken rule.
            */
            while(rules.any {page.contains(it.first) and page.contains(it.second) and (page.indexOf(it.first) > page.indexOf(it.second)) }){
                rules.forEach { rule ->
                    if(page.contains(rule.first) and page.contains(rule.second)){
                        if(page.indexOf(rule.first) > page.indexOf(rule.second)){
                            // Swap
                            val tmp = page[page.indexOf(rule.first)]
                            page[page.indexOf(rule.first)] = page[page.indexOf(rule.second)]
                            page[page.indexOf(rule.second)] = tmp
                        }
                    }
                }
            }
        }
        pages.forEach { page ->
            counter += page[page.size/2]
        }
        return counter
    }

    val input = readInputLines("Day05")
    input.forEach { line ->
        when {
            line.contains('|') -> rules += Pair(line.split('|')[0].toInt(), line.split('|')[1].toInt())
            line.contains(',') -> pages += line.split(',').map { it.toInt() }.toMutableList()
        }
    }

    println("Part 1 solution = " + part1(rules,pages))
    println("Part 2 solution = " + part2(rules,incorrectlyOrderedPages))
}
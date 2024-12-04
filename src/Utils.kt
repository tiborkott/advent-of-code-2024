import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInputLines(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Reads the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim()

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


fun <T : Number> measure(part: () -> T): Pair<T, Long> {
    val start = System.nanoTime()
    val result = part()
    val duration = (System.nanoTime() - start) / 1_000_000 // Convert to milliseconds

    return Pair(result, duration)
}
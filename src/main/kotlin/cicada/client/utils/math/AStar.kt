package cicada.client.utils.math

import cicada.client.utils.math.AStar.findPath
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Grid-based A* pathfinder operating on integer block coordinates.
 *
 * All positions represent the **feet** block of the entity.
 * Collision checks account for a 1 × 2 × 1 player bounding box,
 * so both the feet block and the head block must be passable.
 *
 * [findPath] returns:
 *  - empty list  → already within [range] of the target
 *  - non-null    → waypoint list from [from] to the node closest to [to]
 *  - null        → no path found within [maxNodes] budget
 */
object AStar {

    // ── constants ────────────────────────────────────────────────────────────

    private const val DEFAULT_MAX_NODES = 4_096

    /**
     * Blocks occupied by the player relative to the feet position.
     * Shape: 1 wide × 2 tall × 1 deep  (standard Steve hitbox, grid-snapped).
     */
    private val PLAYER_BODY = arrayOf(
        intArrayOf(0, 0, 0),   // feet
        intArrayOf(0, 1, 0),   // head
    )

    private val DIRECTIONS = arrayOf(
        intArrayOf( 1,  0,  0),
        intArrayOf(-1,  0,  0),
        intArrayOf( 0,  1,  0),
        intArrayOf( 0, -1,  0),
        intArrayOf( 0,  0,  1),
        intArrayOf( 0,  0, -1),
    )

    // ── public API ───────────────────────────────────────────────────────────

    /**
     * @param from       start position (world-space, floating-point)
     * @param to         target position
     * @param stepLength how many blocks each move advances; clamped to ≥ 1
     * @param range      success radius around [to]
     * @param maxNodes   node budget; search aborts (→ null) when exceeded
     * @param isFree     returns true if a single block at (x, y, z) is passable
     */
    fun findPath(
        from: Vec3,
        to: Vec3,
        stepLength: Double,
        range: Double,
        isFree: (Int, Int, Int) -> Boolean,
        maxNodes: Int = DEFAULT_MAX_NODES,
    ): List<Vec3>? {
        if (euclidean(from, to) <= range) return emptyList()

        val step = max(1, stepLength.floorToInt())

        val nodes = HashMap<Long, Node>(maxNodes * 2)
        val open  = java.util.PriorityQueue<Node>(compareBy { it.f })

        val startX = from.x.fastFloor()
        val startY = from.y.fastFloor()
        val startZ = from.z.fastFloor()

        nodes.getOrCreate(startX, startY, startZ).also { start ->
            start.g = 0.0
            start.h = heuristic(startX, startY, startZ, to, range)
            open += start
        }

        while (open.isNotEmpty()) {
            val current = open.poll()!!
            if (current.closed) continue
            current.closed = true

            if (current.distanceTo(to) <= range) return buildPath(current)
            if (nodes.size >= maxNodes) break

            for ((dx, dy, dz) in DIRECTIONS) {
                val nx = current.x + dx * step
                val ny = current.y + dy * step
                val nz = current.z + dz * step

                if (!playerCanOccupy(nx, ny, nz, isFree)) continue

                val neighbor = nodes.getOrCreate(nx, ny, nz)
                if (neighbor.closed) continue

                val tentativeG = current.g + euclidean(current, neighbor)
                if (tentativeG < neighbor.g) {
                    neighbor.g = tentativeG
                    neighbor.h = heuristic(nx, ny, nz, to, range)
                    neighbor.parent = current
                    open += neighbor
                }
            }
        }

        return null
    }

    // ── collision ─────────────────────────────────────────────────────────────

    /**
     * Returns true only when every block the player would occupy
     * at the given feet position is passable according to [isFree].
     */
    private fun playerCanOccupy(
        x: Int, y: Int, z: Int,
        isFree: (Int, Int, Int) -> Boolean,
    ): Boolean = PLAYER_BODY.all { (bx, by, bz) -> isFree(x + bx, y + by, z + bz) }

    // ── node ──────────────────────────────────────────────────────────────────

    private class Node(x: Int, y: Int, z: Int) : Vec3i(x, y, z) {
        var g: Double = Double.MAX_VALUE / 2.0
        var h: Double = 0.0
        var parent: Node? = null
        var closed: Boolean = false

        val f: Double get() = g + h

        fun distanceTo(v: Vec3): Double {
            val dx = v.x - x; val dy = v.y - y; val dz = v.z - z
            return sqrt(dx * dx + dy * dy + dz * dz)
        }
    }

    // ── node map helpers ──────────────────────────────────────────────────────

    private fun HashMap<Long, Node>.getOrCreate(x: Int, y: Int, z: Int): Node =
        getOrPut(packKey(x, y, z)) { Node(x, y, z) }

    /**
     * Packs three coordinates into a Long using 21 bits each.
     * Safe for Minecraft world coordinates (±~1 000 000 blocks per axis).
     */
    private fun packKey(x: Int, y: Int, z: Int): Long {
        val lx = (x + 1_048_576L) and 0x1F_FFFFL
        val ly = (y + 1_048_576L) and 0x1F_FFFFL
        val lz = (z + 1_048_576L) and 0x1F_FFFFL
        return (lx shl 42) or (ly shl 21) or lz
    }

    // ── math helpers ──────────────────────────────────────────────────────────

    private fun heuristic(x: Int, y: Int, z: Int, target: Vec3, range: Double): Double =
        max(0.0, euclidean(x.toDouble(), y.toDouble(), z.toDouble(), target.x, target.y, target.z) - range)

    private fun euclidean(a: Vec3, b: Vec3): Double =
        euclidean(a.x, a.y, a.z, b.x, b.y, b.z)

    private fun euclidean(a: Vec3i, b: Vec3i): Double =
        euclidean(a.x.toDouble(), a.y.toDouble(), a.z.toDouble(),
            b.x.toDouble(), b.y.toDouble(), b.z.toDouble())

    private fun euclidean(x1: Double, y1: Double, z1: Double,
                          x2: Double, y2: Double, z2: Double): Double {
        val dx = x2 - x1; val dy = y2 - y1; val dz = z2 - z1
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    /** Equivalent to floor().toInt() but avoids the boxing that Math.floor causes. */
    private fun Double.fastFloor(): Int = if (this >= 0.0) toInt() else toInt() - 1

    private fun Double.floorToInt(): Int = fastFloor()

    // ── path reconstruction ───────────────────────────────────────────────────

    private fun buildPath(goal: Node): List<Vec3> {
        val path = ArrayDeque<Vec3>()
        var node: Node? = goal
        while (node != null) {
            path.addFirst(Vec3(node.x.toDouble(), node.y.toDouble(), node.z.toDouble()))
            node = node.parent
        }
        return path
    }
}
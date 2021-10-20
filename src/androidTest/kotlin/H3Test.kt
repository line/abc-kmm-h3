
import com.linecorp.abc.h3.H3
import com.linecorp.abc.h3.polygons
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class H3Test {

    @Before
    fun setUp() {
    }

    @Test
    fun testPolygons() {
        val h3Indexes = listOf(
            "87283082bffffff",
            "872830870ffffff",
            "872830820ffffff",
            "87283082effffff",
        )
        val polygons = H3.polygons(h3Indexes)
        println("polygons -> $polygons")

        assertEquals(h3Indexes.count(), polygons.count())
    }
}
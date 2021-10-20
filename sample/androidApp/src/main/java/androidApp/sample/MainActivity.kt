package androidApp.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.linecorp.abc.h3.H3
import com.linecorp.abc.h3.polygons

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val h3Indexes = listOf(
            "87283082bffffff",
            "872830870ffffff",
            "872830820ffffff",
            "87283082effffff",
        )
        val polygons = H3.polygons(h3Indexes)

        println("polygons -> $polygons")
    }
}
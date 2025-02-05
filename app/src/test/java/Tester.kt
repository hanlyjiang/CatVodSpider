import android.graphics.Bitmap.Config
import com.github.hanlyjiang.config_tester.ConfigTester
import org.junit.Test

class Tester {

    @Test
    fun testStart() {
        println("testStart")
        ConfigTester().apply {
            testMultiUrls("https://ghfast.top/https://raw.githubusercontent.com/hanlyjiang/Tvbox1/main/cr.json")
        }
    }
}
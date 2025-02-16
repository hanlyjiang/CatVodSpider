import android.graphics.Bitmap.Config
import com.github.catvod.crawler.Spider
import com.github.catvod.spider.Duopan
import com.github.hanlyjiang.config_tester.ConfigTester
import org.junit.Test

class Tester {

    @Test
    fun testStart() {
        println("testStart")
//        ConfigTester().apply {
//            testMultiUrls("https://ghfast.top/https://raw.githubusercontent.com/hanlyjiang/Tvbox1/main/cr.json")
//        }
        val duopan = Duopan()
        var homeVideoContent = duopan.homeContent(false)
        println(homeVideoContent)
    }
}
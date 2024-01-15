package eu.kanade.tachiyomi.extension.en.webdexscans

import android.annotation.SuppressLint
import eu.kanade.tachiyomi.multisrc.madara.Madara
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.source.model.Page
import okhttp3.Request
import org.jsoup.nodes.Element
import java.nio.charset.StandardCharsets
import java.util.Base64

class WebdexScans : Madara("Webdex Scans", "https://webdexscans.com", "en") {
    override val useNewChapterEndpoint = true
    override val mangaDetailsSelectorStatus =
        "div.summary-heading:contains(Status) + div.summary-content"
    override val mangaSubString = "series"

    override fun searchPage(page: Int): String = if (page == 1) "" else "page/$page/"

    @SuppressLint("NewApi")
    override fun imageFromElement(element: Element): String? {
        val data = element.attr("src")
        val decodedBytes = Base64.getDecoder().decode(data.substringAfter(";base64,"))
        val imgUrl = String(decodedBytes, StandardCharsets.UTF_8) as? String
        val newData = imgUrl?.substringAfter("data-u=\"")?.substringBefore("\"")
            ?.replace("%3A", ":")
            ?.replace("%2F", "/")
        return newData
    }

    override fun imageRequest(page: Page): Request {
        if (page.url.isEmpty()) {
            return GET(
                page.imageUrl!!,
                headers.newBuilder().set("Content-Type", "image/svg+xml").build(),
            )
        }
        return super.imageRequest(page)
    }
}

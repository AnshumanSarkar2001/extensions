package eu.kanade.tachiyomi.extension.en.rizzcomic

import eu.kanade.tachiyomi.multisrc.mangathemesia.MangaThemesia
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.network.interceptor.rateLimit
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SManga
import kotlinx.serialization.json.Json
import okhttp3.Headers
import okhttp3.OkHttpClient
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import uy.kohesive.injekt.injectLazy
import java.util.concurrent.TimeUnit

class RizzComic : MangaThemesia("Rizzcomic", "https://rizzcomic.com", "en", "/series") {

    override val client: OkHttpClient = super.client.newBuilder()
        .rateLimit(1, 1, TimeUnit.SECONDS)
        .build()

    override fun headersBuilder(): Headers.Builder = Headers.Builder()
        .add("Referer", baseUrl)

    override fun pageListParse(document: Document): List<Page> {
        return super.pageListParse(document)
            .distinctBy { it.imageUrl }
            .mapIndexed { i, page -> Page(i, imageUrl = page.imageUrl) }
    }
}

package sabat.sabatownik.logic

import android.content.Context

class AssetsBaseProvider(private val context: Context) {

    fun getAvailableBases(): List<String> {
        return context.assets.list("bazy")?.toList() ?: emptyList()
    }

    fun getFilesForBase(baseName: String): List<String> {
        return context.assets.list("bazy/$baseName")?.toList() ?: emptyList()
    }
}
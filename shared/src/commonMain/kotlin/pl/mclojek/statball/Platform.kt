package pl.mclojek.statball

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
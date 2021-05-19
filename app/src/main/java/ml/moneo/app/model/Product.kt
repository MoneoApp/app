package ml.moneo.app.model

//TODO: Image path for async loading
open class Product(open val name: String, open val resId: Int) {
}

data class Remote(override val name: String, val remoteId: Int, override val resId: Int) :
    Product(name = name, resId = resId) {
}

data class Guide(
    override val name: String,
    val guideId: Int,
    val remoteId: Int,
    override val resId: Int
) : Product(name = name, resId = resId) {
}
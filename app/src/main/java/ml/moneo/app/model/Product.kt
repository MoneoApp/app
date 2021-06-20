package ml.moneo.app.model

//TODO: Image path for async loading
open class Product(open val name: String, open val resId: Int, open val remoteId: String) {
}

data class Remote(override val name: String, override val remoteId: String, override val resId: Int) :
    Product(name = name, resId = resId, remoteId) {
}

data class Guide(
    override val name: String,
    val guideId: String,
    override val remoteId: String,
    override val resId: Int
) : Product(name = name, resId = resId, remoteId) {
}
package ml.moneo.app.model

class Manual(
    val id: String,
    val name: String,
    val steps: List<Step>
) {
    fun getStepCount(): Int {
        return steps.size;
    }

    fun getStep(id: String): Step? {
        return steps.find { it.id === id }
    }
}

data class Step(
    val id: String,
    val description: String,
    val interaction: Interaction
)

data class Overlay(
    val id: String,
    val name: String,
    val interactions: List<Interaction>
)

data class Interaction(
    val id: String,
    val x: Double,
    val y: Double,
    val width: Double,
    val height: Double,
    val text: String,
    val overlay: Overlay,
//    val overlayId: String,
    val blocks: List<ContentBlock>
)

data class ContentBlock(
    val id: String,
    val type: ContentBlockType,
    val data: String,
    val interaction: Interaction,
    val interactionId: String
)

enum class ContentBlockType {
    TEXT,
    LIST,
    IMAGE,
}

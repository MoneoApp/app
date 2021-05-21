package ml.moneo.app.model

class HelpManual(
    val id: String,
    val name: String,
    val steps: List<HelpStep>
) {
    fun getStepCount(): Int {
        return steps.size;
    }
}

data class HelpStep(
    val id: String,
    val description: String,
    val drawable: Int
)

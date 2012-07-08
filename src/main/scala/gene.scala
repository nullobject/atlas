import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, property="name")
abstract class Gene(val value: Float) {
  def mix(other: Gene): Gene
}

case class FeedFrequency(override val value: Float) extends Gene(value) {
  def mix(other: Gene): Gene = FeedFrequency((this.value + other.value) / 2)
}

case class ReproduceFrequency(override val value: Float, minParents: Float) extends Gene(value) {
  def mix(other: Gene): Gene = ReproduceFrequency((this.value + other.value) / 2, 1)
}

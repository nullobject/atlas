import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, property="name")
abstract class Gene(val value: Double) {
  def build(value: Double): Gene
}

object Gene {
  def mix(genes: Seq[Gene]): Gene = {
    val value: Double = (0.0 /: genes) { _ + _.value } / genes.size
    genes.head.build(value)
  }
}

case class FeedFrequency(override val value: Double) extends Gene(value) {
  def build(value: Double): Gene = FeedFrequency(value)
}

case class FeedAmount(override val value: Double) extends Gene(value) {
  def build(value: Double): Gene = FeedAmount(value)
}

case class ReproduceFrequency(override val value: Double) extends Gene(value) {
  def build(value: Double): Gene = ReproduceFrequency(value)
}

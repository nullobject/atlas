import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, property="name")
abstract class Gene(val value: BigDecimal) {
  def build(value: BigDecimal): Gene
}

object Gene {
  def mix(genes: Seq[Gene]): Gene = {
    val value: BigDecimal = (BigDecimal(0) /: genes) { _ + _.value } / genes.size
    /* genes.head.getClass.getConstructor(classOf[BigDecimal]).newInstance(value) */
    genes.head.build(value)
  }
}

case class FeedFrequency(override val value: BigDecimal) extends Gene(value) {
  def build(value: BigDecimal): Gene = FeedFrequency(value)
}

case class FeedAmount(override val value: BigDecimal) extends Gene(value) {
  def build(value: BigDecimal): Gene = FeedAmount(value)
}

case class ReproduceFrequency(override val value: BigDecimal) extends Gene(value) {
  def build(value: BigDecimal): Gene = ReproduceFrequency(value)
}

case class Speed(override val value: BigDecimal) extends Gene(value) {
  def build(value: BigDecimal): Gene = Speed(value)
}

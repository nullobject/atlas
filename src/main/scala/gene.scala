import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.PROPERTY, property="name")
abstract class Gene(value: Long)

case class FeedFrequency(value: Long) extends Gene(value)
case class ReproduceFrequency(value: Long, minParents: Long) extends Gene(value)

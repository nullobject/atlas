object Hello {
  val json = """{"name":"Rabbit","genes":[{"name":"FeedFrequency","value":1},{"name":"ReproduceFrequency","value":2,"minParents":2}]}"""
  val result = Genome.deserialize(json)
  def main(args: Array[String]) = println(result)
}

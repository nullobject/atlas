object Hello {
  val json = """{"name":"Rabbit","genes":[{"name":"FeedFrequency","value":1.0},{"name":"ReproduceFrequency","value":2.0,"minParents":2.0}]}"""

  def main(args: Array[String]) = {
    val genome = Genome.deserialize(json)
    println(genome)

    val gene = FeedFrequency(2)
    println(genome.genes(0).mix(gene))
  }
}

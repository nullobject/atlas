object Hello {
  val rabbitJson = """
    {
      "name": "Rabbit",
      "genes": [
        {"name": "FeedAmount",         "value": 1.0},
        {"name": "FeedFrequency",      "value": 2.0},
        {"name": "ReproduceFrequency", "value": 3.0}
      ]
    }
  """

  val pigJson = """
    {
      "name": "Pig",
      "genes": [
        {"name": "FeedFrequency",      "value": 4.0},
        {"name": "ReproduceFrequency", "value": 5.0},
        {"name": "Speed",              "value": 6.0}
      ]
    }
  """

  def main(args: Array[String]) = {
    val rabbit = Genome.deserialize(rabbitJson)
    val pig    = Genome.deserialize(pigJson)
    val hybrid = rabbit * pig
    println(rabbit, pig, hybrid)

    val organism = Organism(hybrid, 100)
    println(organism)
  }
}

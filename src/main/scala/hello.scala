object Hello {
  val rabbitJson = """
    {
      "name": "Rabbit",
      "genes": [
        {"name": "FeedFrequency",      "value": 1.0},
        {"name": "FeedAmount",         "value": 1.0},
        {"name": "ReproduceFrequency", "value": 2.0}
      ]
    }
  """

  val pigJson = """
    {
      "name": "Pig",
      "genes": [
        {"name": "FeedFrequency",      "value": 3.0},
        {"name": "ReproduceFrequency", "value": 4.0}
      ]
    }
  """

  def main(args: Array[String]) = {
    val rabbit = Genome.deserialize(rabbitJson)
    val pig = Genome.deserialize(pigJson)
    println(rabbit)
    println(pig)
    println(rabbit * pig)
  }
}

object Main {
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

    var world = World(List(Organism(rabbit, 100), Organism(pig, 100), Organism(hybrid, 100)), 0)
    for (i <- 0 until 100) {
      println(world)
      world = world.tick
    }
  }
}

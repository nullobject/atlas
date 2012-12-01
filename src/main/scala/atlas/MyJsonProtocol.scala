package atlas

import spray.json._

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val genomeFormat = jsonFormat2(Genome.apply)
}

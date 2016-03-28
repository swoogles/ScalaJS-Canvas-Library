package shared

import scala.util.Random

object CollectionFunctions {
  val rand = new Random(System.currentTimeMillis());

  def randomElement[T](list: List[T]): T = {
    val random_index = rand.nextInt(list.length);
    list(random_index)
  }

}


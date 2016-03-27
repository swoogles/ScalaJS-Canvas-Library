package shared

import scala.util.Random

object CollectionFunctions {

  def randomElement[T](list: List[T]): T = {
    val rand = new Random(System.currentTimeMillis());
    val random_index = rand.nextInt(list.length);
    list(random_index)
  }

}


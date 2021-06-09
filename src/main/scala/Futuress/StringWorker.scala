package Futuress

import scala.io.Source

object StringWorker {
  def readStrings() = {
    val source = Source.fromFile("text.txt")
    val lines = source
      .getLines()
      .flatMap(_.split(Array('\n', ' ')))
      .filter(_.nonEmpty)
      .map(x => x.length)
      .toArray
    source.close()
    lines
  }
  val isEven: Int => Boolean = _ % 2 == 0
  val isOdd: Int => Boolean = _ % 2 == 1
  def average(arr: Array[Int]): Double = 1.0 * arr.sum / arr.length
}

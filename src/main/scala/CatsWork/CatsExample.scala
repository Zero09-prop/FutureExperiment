package CatsWork

import Futuress.StringWorker.{average, isEven, isOdd, readStrings}
import cats.effect.IO
import cats.effect.unsafe.IORuntime

object CatsExample {
  def main(args: Array[String]): Unit = {
    doSomething(cats.effect.unsafe.implicits.global)
  }

  private def doSomething(implicit contextImplicit: IORuntime): Unit = {
    val ioInt: IO[Array[Int]] = IO {
      readStrings()
    }

    def avg(pred: Int => Boolean): IO[Double] = {
      val filterValues = ioInt
        .map(x => x.filter(pred))
      filterValues.map(x => average(x))
    }

    val ioAvgEven = avg(isEven)
    val ioAvgOdd = avg(isOdd)

    val ioDiff: IO[Double] =
      for {
        evenAvg <- ioAvgEven
        oddAvg <- ioAvgOdd
      } yield (evenAvg + oddAvg) / 2

    val avgLength = ioDiff.unsafeRunSync()
    println(s"Average length of words is $avgLength")
  }

}

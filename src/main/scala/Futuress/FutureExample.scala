package Futuress

import Futuress.StringWorker.{average, isEven, isOdd, readStrings}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

object FutureExample {
  def main(args: Array[String]): Unit = {
    doSomething(scala.concurrent.ExecutionContext.Implicits.global)
  }

  private def doSomething(implicit contextImplicit: ExecutionContext): Unit = {
    val futureInt: Future[Array[Int]] = Future {
      readStrings()
    }
    def avg(pred: Int => Boolean): Future[Double] = {
      val filteredValues = futureInt
        .map(x => x.filter(pred))
      filteredValues.map(x => average(x))
    }

    val futureAvgEven = avg(isEven)
    val futureAvgOdd = avg(isOdd)

    val futureDiff: Future[Double] =
      for {
        evenAvg <- futureAvgEven
        oddAvg <- futureAvgOdd
      } yield (evenAvg + oddAvg) / 2

    val avgLength = Await.result(futureDiff, 10.seconds)
    println(s"Average length of words is $avgLength")
  }
}

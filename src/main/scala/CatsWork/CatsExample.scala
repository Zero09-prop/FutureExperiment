package CatsWork

import Futuress.StringWorker.{average, isEven, isOdd, readStrings}
import cats.effect.IO
import cats.effect.unsafe.IORuntime

object CatsExample {
  def main(args: Array[String]): Unit = {
    val ioInt: IO[Array[Int]] = IO {
      readStrings()
    }
    val ExecutionCtx = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())
    val cpuPool = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))

    def cpuEval[A](ioa: IO[A]): IO[A] =
      ioa.evalOn(cpuPool)
    def avg(pred: Int => Boolean)(ints: Array[Int]): IO[Double] =
      IO {
        average(ints.filter(pred))
      }
    val ioAvgEven = avg(isEven)(_)
    val ioAvgOdd = avg(isOdd)(_)

    val ioDiff: IO[Double] =
      for {
        ints <- ioInt
        evenAvg <- cpuEval(ioAvgEven(ints))
        oddAvg <- cpuEval(ioAvgOdd(ints))
      } yield (evenAvg + oddAvg) / 2

    val avgLength = ioDiff.unsafeRunSync()
    println(s"Average length of words is $avgLength")
  }
}

import io.threadcso._

class ConcGraphSearch[N](g: Graph[N]) extends GraphSearch[N](g){
  /**The number of workers. */
  val numWorkers = 8

  /** Perform a depth-first search in g, starting from start, for a node that
    * satisfies isTarget. */
  def apply(start: N, isTarget: N => Boolean): Option[N] = {
  	val stack = new TerminatingPartialStack[N](numWorkers); stack.push(start)

  	// Channel on which a worker tells the coordinator that it has found a
    // solution.
    val pathFound = ManyOne[N]

    // A single worker
    def worker = proc("worker"){
      repeat{
        val n = stack.pop
        for(n1 <- g.succs(n)){
          if(isTarget(n1)) pathFound!(n1) // done!
          else stack.push(n1)
        }
      }
      pathFound.close // causes coordinator to close down
    }

    // Variable that ends up holding the result; written by coordinator. 
    var result: Option[N] = None

    def coordinator = proc("coordinator"){
      attempt{ result = Some(pathFound?()) }{ }
      stack.shutdown // close stack; this will cause most workers to terminate
      pathFound.close // in case another thread has found solution
    }

    val workers = || (for(_ <- 0 until numWorkers) yield worker)
    (workers || coordinator)()
    result
  }
}

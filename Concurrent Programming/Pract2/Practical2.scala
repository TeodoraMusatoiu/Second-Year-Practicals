import io.threadcso._
import scala.language.postfixOps

/** Simulation of the Dining Philosophers example. */
object Practical2 {
  val N = 5 // Number of philosophers

  // Simulate basic actions
  def Eat = Thread.sleep(500)
  def Think = Thread.sleep(scala.util.Random.nextInt(900))
  def Pause = Thread.sleep(500)

  // Each philosopher will send "pick" and "drop" commands to her forks, which
  // we simulate using the following values.
  type Command = Boolean
  val Pick = true; val Drop = false
 
  /** A single left-handed philosopher. */
  def phil(me: Int, left: ![Command], right: ![Command]) = proc("Phil"+me){
    repeat{
      Think
      println(me+" sits"); Pause
      left!Pick; println(me+" picks up left fork"); Pause
      right!Pick; println(me+" picks up right fork"); Pause
      println(me+" eats"); Eat
      left!Drop; Pause; right!Drop; Pause
      println(me+" leaves")
    }
  }

   /** A single right-handed philosopher. */
  def philright(me: Int, left: ![Command], right: ![Command]) = proc("Phil"+me){
    repeat{
      Think
      println(me+" sits"); Pause
      right!Pick; println(me+" picks up right fork"); Pause
      left!Pick; println(me+" picks up left fork"); Pause
      println(me+" eats"); Eat
      right!Drop; Pause; left!Drop; Pause
      println(me+" leaves")
    }
  }
  
  val sits, leaves = ManyOne[Int]
   /** A single philosopher that sends info to butler. */
  def philbut(me: Int, left: ![Command], right: ![Command]) = proc("Phil"+me){
    repeat{
      sits!(me);
      Think; 
      println(me+" sits"); Pause
      left!Pick; println(me+" picks up left fork"); Pause
      right!Pick; println(me+" picks up right fork"); Pause
      println(me+" eats"); Eat
      left!Drop; Pause; right!Drop; Pause
      println(me+" leaves"); leaves!(me);
    }
  }

/** Philosophers waiting. */  
def philwait(me: Int, left: ![Command], right: ![Command]) = proc("Phil"+me){
    repeat{
      Think
      println(me+" sits"); Pause
      left!Pick; println(me+" picks up left fork"); Pause;
      if(right.writeBefore(800)(Pick)){
      	println(me+" picks up right fork");
        Pause;
        println(me+" eats"); Eat
        left!Drop; Pause; right!Drop; Pause
        println(me+" leaves")
      }
      else{
      	left!Drop; Pause; println(me+" leaves")
      }
    }
  }

  /** A single fork. */
  def fork(me: Int, left: ?[Command], right: ?[Command]) = proc("Fork"+me){
    serve(
      left =?=> {
        x => assert(x == Pick); val y = left?; assert(y == Drop)
      }
      |
      right =?=> {
        x => assert(x == Pick); val y = right?; assert(y == Drop)
      }
    )
  }
  
  var nophil = 0;
  /** A single butler who checks that no more than 4 philosophers are 
   *  simultaneously seated.*/
  def butler():PROC = proc {
    serve(
          (nophil < N-1 && sits ) =?=> {x => nophil += 1 }
          | (nophil == N-1 && leaves) =?=> {x => nophil -=1}
      ) 
  }
  

  /** Run the system. */
  def main(args : Array[String]) = { 

  	//Select problem
  	println("Select one of the solutions to be run:")
  	println("0. Original system.")
  	println("1. One right-handed philosopher.")
  	println("2. The butler problem.")
  	println("3. The waiting problem.")
    print("Please enter the test number: ")
    val test_no = readInt();

    test_no match {
      case 0 => {
        println("Run original system:");

        /** The complete system for N philosophers. */
  		def system = {
    		// Channels to pick up and drop the forks:
    		val philToLeftFork, philToRightFork = Array.fill(5)(OneOne[Command])
    		// philToLeftFork(i) is from Phil(i) to Fork(i);
    		// philToRightFork(i) is from Phil(i) to Fork((i-1)%N)
    		val allPhils = || ( 
      			for (i <- 0 until N)
      				yield phil(i, philToLeftFork(i), philToRightFork(i))
    			) 
    		val allForks = || ( 
      			for (i <- 0 until N) 
        			yield fork(i, philToRightFork((i+1)%N), philToLeftFork(i))
    			)
    		allPhils || allForks
 		}

        system ();
      }
      case 1 => {
        println("Run system which uses a right-handed philosopher:");

        /** The complete system for one right-handed philosopher. */
  		def system = {
    		// Channels to pick up and drop the forks:
    		val philToLeftFork, philToRightFork = Array.fill(5)(OneOne[Command])
    		// philToLeftFork(i) is from Phil(i) to Fork(i);
    		// philToRightFork(i) is from Phil(i) to Fork((i-1)%N)
    		val allPhils = || ( 
      			for (i <- 0 until N-1)
      				yield phil(i, philToLeftFork(i), philToRightFork(i))
			) || philright(N-1, philToLeftFork(N-1), philToRightFork(N-1))
    		val allForks = || ( 
      			for (i <- 0 until N) 
        			yield fork(i, philToRightFork((i+1)%N), philToLeftFork(i))
    		)
    		allPhils || allForks 
  		}

        system ();
      }
      case 2 => {
        println("Run system for the problem with a butler:");
        /** The complete system for the butler problem. */
  		def system = {
    		// Channels to pick up and drop the forks:
    		val philToLeftFork, philToRightFork = Array.fill(5)(OneOne[Command])
    		// philToLeftFork(i) is from Phil(i) to Fork(i);
    		// philToRightFork(i) is from Phil(i) to Fork((i-1)%N)
    		val allPhils = || ( 
      		for (i <- 0 until N)
      			yield philbut(i, philToLeftFork(i), philToRightFork(i))
    		) 
    		val allForks = || ( 
      			for (i <- 0 until N)
        			yield fork(i, philToRightFork((i+1)%N), philToLeftFork(i))
    		)
    		allPhils || allForks || butler
  		}

        system ();
      }
  	  case 3 => {
  	  	println("Run system for the problem with waiting times:");
        /** The complete system for the butler problem. */
  		def system = {
    		// Channels to pick up and drop the forks:
    		val philToLeftFork, philToRightFork = Array.fill(5)(OneOne[Command])
    		// philToLeftFork(i) is from Phil(i) to Fork(i);
    		// philToRightFork(i) is from Phil(i) to Fork((i-1)%N)
    		val allPhils = || ( 
      		for (i <- 0 until N)
      			yield philwait(i, philToLeftFork(i), philToRightFork(i))
    		) 
    		val allForks = || ( 
      			for (i <- 0 until N)
        			yield fork(i, philToRightFork((i+1)%N), philToLeftFork(i))
    		)
    		allPhils || allForks
  		}

        system ();
  	  }
    }
    exit();
  }
}
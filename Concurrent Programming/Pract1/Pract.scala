import io.threadcso._

object Pract {

  /** A single comparator, inputting on in0 and in1, and outputting on out0 ∗ (smaller value) and out1 (larger value). */
   def comparator(in0: ?[Int], in1: ?[Int], out0: ![Int], out1: ![Int ]): PROC = proc{
    var x, y = 0;
    
    repeat {
      //Read input channel
      (proc {x = in0?()} || proc {y = in1?()} ) ();

      //Compare the two numbers
      if(x < y) 
        (proc {out0!(x)} || proc {out1!(y)}) ();
      else 
        (proc {out0!(y)} || proc {out1!(x)}) ();
    }
    
    //Close channels
    out0.closeOut();
    out1.closeOut();
  }

  /** A sorting network for four values. */
  def sort4(ins: List[?[Int ]], outs: List [![ Int ]]): PROC = proc {
    require(ins.length == 4 && outs.length == 4)

    //Create 6 new channels for intermediate results
    var min02, max02, min13, max13, mid1, mid2 = OneOne [Int]

    //First step of comparation
    val p = comparator(ins(0), ins(2), min02, max02) || comparator (ins(1), ins(3), min13, max13);
    //Second step of comparation
    val q = comparator (min02, min13, outs(0), mid1) || comparator (max02, max13, mid2, outs(3));
    //Third step of comparation
    val r = comparator (mid1, mid2, outs(1), outs(2));

    repeat { run (p || q || r); }
    
    //Close channels
    for(out <- outs) out.closeOut();
    for(in <- ins) in.closeIn();
  }

  /** Insert a value input on in into a sorted sequence input on ins.
  ∗ Pre: ins.length = n && outs.length = n+1, for some n >= 1.
  ∗ If the values xs input on ins are sorted, and x is input on in, then a
  ∗ sorted permutation of x::xs is output on ys. **/
  def insert(ins: List[?[Int]], in: ?[Int], outs: List[![Int]]): PROC =
  {
    val n = ins.length; require(n >= 1 && outs.length == n+1)

    //Recursive pattern-matching on the list of input channels
    ins match {
      case in1::Nil => comparator(in1, in, outs(0), outs(1))

      case in1::in_rest => {
        val ch = OneOne[Int]
        comparator(in1, in, outs.head, ch) || insert(in_rest, ch, outs.tail)
      }
    }
    
  }

  /** Insertion sort. */
  def insertionSort(ins: List [?[Int]], outs: List [![Int]]): PROC = {
    val n = ins.length; require(n >= 2 && outs.length == n)

    //Recursive pattern matching on the list of input channels
    ins match {
      case in1::in2::Nil => comparator(in1, in2, outs(0), outs(1))

      case in1::ins_rest =>
        {
          val rest_sorted = List.fill(outs.length-1)(OneOne[Int])
          insertionSort(ins_rest, rest_sorted) || insert(rest_sorted, in1, outs)
        }
    }

  }


  /** Input random numbers for all the outs */
  def inputs(outs: List[![Int]], tests_nr:Int): PROC =
    proc {
      for (test <- 1 to tests_nr)
      {
        if(test>1) cycle?()

        val values = List.fill(outs.length)(scala.util.Random.nextInt(100))
        print("Input: ")
        for (i <- values.indices) print(values(i) + " ")
        print(" ---> ")
        for (i <- outs.indices) outs(i) ! values(i)
      }
      cycle?()
      for (ch <- outs) ch.closeOut()
    }

  /** Input sorted random numbers for all the outs */
  def inputs_sorted(outs: List[![Int]], tests_nr:Int): PROC =
    proc {
      for (test <- 1 to tests_nr) {
        if(test > 1) cycle?()
        val values = List.fill(outs.length)(scala.util.Random.nextInt(100)).sorted
        print("Input: ")
        for (i <- values.indices) print(values(i) + " ")
        print(" ---> ")
        for (i <- outs.indices) outs(i) ! values(i)
      }
      for (ch <- outs) ch.closeOut()
    }

  /** Inputs x on out */
  def input(out : ![Int], x:Int) : PROC = proc{while(true) out!x}

  /** Prints to console all the ins */
  def outputs(ins: List[?[Int]]): PROC =
    proc {
      repeat {
        var aux = new Array[Int](ins.size)
        for (i <- ins.indices) aux(i) = ins(i)?()
        for (i <- ins.indices) print(aux(i) + " ")
        println()
        cycle!()
      }
      for(in <- ins) in.closeIn()
    }

  val cycle = OneOne[Unit]

  def main(args : Array[String]) =
  {
    println("Choose one of the following questions: ")
    println("1. Comparator")
    println("2. Sort4")
    println("3. Insert")
    println("4. Insertion Sort")
    print("Input option number: ")
     //Set the question number that you want to test
    var question = Console.readInt
    print("Input number ot tests: ");
    //Number of tests to be made
    var nr_of_tests = Console.readInt 

    question match
    {
      case 1 => {
        println("Test question 1: Comparator")
        val ins = List.fill(2)(OneOne[Int])
        val outs = List.fill(2)(OneOne[Int])
        val system = inputs(ins, nr_of_tests) || comparator(ins(0), ins(1), outs(0), outs(1)) || outputs(outs)
        run(system)
      }
      case 2 => {
        println("Test question 2: Sort4")
        val ins = List.fill(4)(OneOne[Int])
        val outs = List.fill(4)(OneOne[Int])
        val system = inputs(ins, nr_of_tests) || sort4(ins, outs) || outputs(outs)
        run(system)
      }
      case 3 => {
        println("Test question 3: Insert")
        val ins = List.fill(9)(OneOne[Int])
        val outs = List.fill(10)(OneOne[Int])
        val ch_aux = OneOne[Int]
        val system = inputs_sorted(ins, nr_of_tests) || input(ch_aux, 50) || insert(ins, ch_aux, outs) || outputs(outs)
        run(system)
      }
      case 4 => {
        println("Test question 4: Insertion Sort")
        val ins = List.fill(10)(OneOne[Int])
        val outs = List.fill(10)(OneOne[Int])
        val system = inputs(ins, nr_of_tests) || insertionSort(ins, outs) || outputs(outs)
        run(system)
      }
    }
    exit()
  }
}



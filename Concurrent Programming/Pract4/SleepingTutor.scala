// Template for the Sleeping Tutor practical

import io.threadcso._

/** The trait for a Sleeping Tutor protocol. */
trait SleepingTutor{
  /** A tutor waits for students to arrive. */
  def tutorWait

  /** A student arrives and waits for the tutorial. */
  def arrive
  
  /** A student receives a tutorial. */
  def receiveTute

  /** A tutor ends the tutorial. */
  def endTeach
}

// =======================================================

import scala.util.Random

class SleepingTutorMonitor extends SleepingTutor{
  private var tutorAvailable = false
  private var tutorDone = false
  private var numberstud = 0 

  private val monitor = new Monitor

  private val tutorAvailableC, chairOccupiedC, tutorDoneC, studentLeftC= monitor.newCondition

  // use a "bench" of 2 people instead of the chair in the sleeping barber

  def tutorWait = monitor.withLock{
  	tutorAvailable = true
  	tutorAvailableC.signalAll() // signal the students
  	chairOccupiedC.await()	// wait for bothe students to arrive
  }

  def arrive = monitor.withLock {
	numberstud = numberstud + 1
	tutorAvailableC.await(tutorAvailable) // wait for tutor to be available
	if (numberstud == 2 ){
		chairOccupiedC.signalAll() // wake tutor up once both students arrived
	}
  }

  def receiveTute = monitor.withLock {
	if(!tutorDone) tutorDoneC.await() //wait for tutor to finish  
	tutorDone = false // prepare for next tutorial
	studentLeftC.signalAll() // signal students to leave
  }

  def endTeach = monitor.withLock {
	tutorDone = true
	tutorDoneC.signalAll() // wake up students
	studentLeftC.await() // wait for tutor to finish teaching
	numberstud = 0	// reset for next tutorial
	tutorAvailable = false
  }
	
}

object SleepingTutorSimulation{
  private val st: SleepingTutor = new SleepingTutorMonitor

  def student(me: String) = proc("Student"+me){
    while(true){
      Thread.sleep(Random.nextInt(2000))
      println("Student "+me+" arrives")
      st.arrive
      println("Student "+me+" ready for tutorial")
      st.receiveTute
      println("Student "+me+" leaves")
    }
  }

  def tutor = proc("Tutor"){
    while(true){
      println("Tutor waiting for students")
      st.tutorWait
      println("Tutor starts to teach")
      Thread.sleep(1000)
      println("Tutor ends tutorial")
      st.endTeach
      Thread.sleep(1000)
    }
  }

  def system = tutor || student("Alice") || student("Bob")

  def main(args: Array[String]) = system()
}


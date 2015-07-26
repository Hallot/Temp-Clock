import akka.actor.Actor

class Converter extends Actor {
  def receive = {
    case ConvertToCelsius(fTemp) =>
      val cTemp = (fTemp - 32) * 5 / 9
      sender ! Temperature(fTemp, cTemp)

    case ConvertToFahrenheit(cTemp) =>
      val fTemp = (cTemp * 9 / 5) + 32
      sender ! Temperature(fTemp, cTemp)
  }
}
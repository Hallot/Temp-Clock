import akka.actor.{ActorLogging, Actor}

class Display extends Actor with ActorLogging {
  def receive = {
    case Temperature(temperatureFahrenheit, temperatureCelsius) =>
      log.info(s"Temperature in Celsius: $temperatureCelsius ; Temperature in Fahrenheit: $temperatureFahrenheit from [${sender.path}]")
  }
}
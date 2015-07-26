import akka.actor.{Props, ActorSystem, Actor}

class Sensor extends Actor {
  val system = ActorSystem("Sensor")
  val tempConverter = system.actorOf(Props(new Converter), "TemperatureConverter")
  val display = system.actorOf(Props(new Display), "Display")

  val r = scala.util.Random
  def receive = {
    case Tick() =>
      // Create a random decimal temperature between [-20;80]
      val randTemp = r.nextFloat() * 100 - 20
      // Randomly choose the type of temperature (integer 0 or 1)
      // 0 is Fahrenheit, 1 is Celsius
      val randType = r.nextInt(1)
      if (randType == 0) {
        tempConverter ! ConvertToCelsius(randTemp)
      }
      else {
        tempConverter ! ConvertToFahrenheit(randTemp)
      }

    case Temperature(temperatureFahrenheit, temperatureCelsius) =>
      display ! Temperature(temperatureFahrenheit, temperatureCelsius)
  }
}
import akka.actor.{ActorLogging, Actor, ActorSystem, Props}

case class Tick()
case class Temperature(temperatureFahrenheit: Float, temperatureCelsius: Float)
case class ConvertToCelsius(tCelsius:Float)
case class ConvertToFahrenheit(tFahrenheit:Float)

object Clock extends App {
  val system = ActorSystem("Clock")

  val sensor1 = system.actorOf(Props(new Sensor), "Sensor1")
  val sensor2 = system.actorOf(Props(new Sensor), "Sensor2")

  while (true) {
    sensor1 ! Tick()
    sensor2 ! Tick()
    Thread.sleep(1000)
  }
}

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

class Display extends Actor with ActorLogging {
  def receive = {
    case Temperature(temperatureFahrenheit, temperatureCelsius) =>
      log.info(s"Temperature in Celsius: $temperatureCelsius ; Temperature in Fahrenheit: $temperatureFahrenheit from [${sender.path}]")
  }
}
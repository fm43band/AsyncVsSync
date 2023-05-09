package data

import cats.implicits._
import cats.instances.option._
import eu.timepit.refined.api._
import io.circe.Decoder.Result
import io.circe.parser._
import io.circe.{Decoder, Json, KeyDecoder}
import psf.model.Entities._
import scalaj.http.{Http, HttpOptions}

import java.time.Instant

object fr24dataLoader {

  def fetchData(baseUrl: String, paramsMap: Map[String, String]): Option[List[FlightTrackLog]] = {
    val result = Http(baseUrl).params(paramsMap)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(5000)).asString

    result.code match {
      case 200 => Some(result.body).map(t => transformToTrackingLogs(extractTrackingData(t)))
      case _ => None
    }
  }

  private def extractTrackingData(rawData: String): Result[Json] = {
    val json: Json = parse(rawData).getOrElse(Json.Null)

    // skip first 2 members
    json.hcursor.downField("full_count").delete
      .downField("version").delete
      .as[Json]
  }

  private def transformToTrackingLogs(logsRaw: Result[Json]): List[FlightTrackLog] = {
    logsRaw match {
      case Left(_) => List.empty[FlightTrackLog]
      case Right(value) =>
        val decoder = Decoder.decodeMap(KeyDecoder.decodeKeyString, Decoder.decodeJson)
        decoder.decodeJson(value)
          .map(t => t.values.flatMap(it => it.asArray))
          .map(toTrackingLogs)
          .getOrElse(List.empty[FlightTrackLog])
      //rezn.map(t => println(t.values.toString()))
    }
  }

  private def toTrackingLogs(jsonArr: Iterable[Vector[Json]]): List[FlightTrackLog] = {
    jsonArr.flatMap(t => for {
      cs <- t(16).asString.map(Callsign.from)
      ac <- t(8).asString.map(AircraftCode.from)
      lat <- t(1).asNumber.map(t => Latitude.from(t.toFloat))
      lng <- t(2).asNumber.map(t => Longitude.from(t.toFloat))
      alt <-  t(4).asNumber.map(t => Altitude.from(t.toInt.getOrElse(0)))
      spd <-  t(5).asNumber.map(t => Speed.from(t.toInt.getOrElse(0)))
      tm = Right(Instant.now)
      } yield (cs, ac, (lat, lng).mapN(GeographicalPosition), alt, spd, tm).mapN(FlightTrackLog)
    ).collect {
      case Right(value) => value
    }.toList
  }

  /*val runOnce = fetchData(baseUrl, paramsM)
      .map(t => transformToTrackingLogs(extractTrackingData(t)))


  println(runOnce.getOrElse(Nil)) */

}

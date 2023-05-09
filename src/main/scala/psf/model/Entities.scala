package psf.model

import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.boolean._
import eu.timepit.refined.collection.MaxSize
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric.Interval.Closed
import eu.timepit.refined.numeric._
//import shapeless.Nat._

import java.time.Instant

object Entities {

  type Callsign = String Refined MaxSize[8]
  object Callsign extends RefinedTypeOps[Callsign, String]
  type Direction = Int Refined Positive And Less[360]
  type Latitude = Float Refined Closed[W.`-90`.T, W.`90`.T]
  object Latitude extends RefinedTypeOps[Latitude, Float]
  type Longitude = Float Refined Closed[W.`-180`.T, W.`180`.T]
  object Longitude extends RefinedTypeOps[Longitude, Float]
  type Altitude = Int Refined (Positive Or Equal[W.`0`.T])
  object Altitude extends RefinedTypeOps[Altitude, Int]
  type Speed = Int Refined (Positive Or Equal[W.`0`.T])
  object Speed extends RefinedTypeOps[Speed, Int]
  type AircraftCode = String Refined MaxSize[5]
  object AircraftCode extends RefinedTypeOps[AircraftCode, String]


  case class GeographicalPosition(
                                   latitude: Latitude,
                                   longitude: Longitude
                                 )

  case class FlightTrackLog(
                             flightId: Callsign,
                             aircraftType: AircraftCode,
                             geolocation: GeographicalPosition,
                             altitude: Altitude,
                             speed: Speed,
                             //reportedDirection: Option[Direction],
                             reportedAt: Instant
                           )


}


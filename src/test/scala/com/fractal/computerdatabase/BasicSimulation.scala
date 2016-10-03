package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:5678") 

  val scn = scenario("Sanyi Transactions") 
    .exec(http("GET").get("/sanyi/transactions/"))
      .pause(1)
    .exec(http("PUT")
      .put("/sanyi/transactions/?id=12&id=13"))
    .pause(2)
    .exec(http("DELETE")
      .delete("/sanyi/transactions/12"))
    .pause(1)
    .exec(http("POST") 
      .post("/sanyi/transactions/13"))
  
  setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))
}

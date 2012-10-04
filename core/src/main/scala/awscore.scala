package aws.core

import play.api.libs.ws._
import play.api.libs.ws.WS._

import com.ning.http.client.Realm.{ AuthScheme, RealmBuilder }

object AWS {
  val awsVersion = "2009-04-15"
  lazy val key: String = scala.io.Source.fromFile(System.getProperty("user.home") + "/.awssecret").getLines.toList(0)
  lazy val secret: String = scala.io.Source.fromFile(System.getProperty("user.home") + "/.awssecret").getLines.toList(1)

}

case class AWSignatureCalculator(accessKeyId: String, secretAccessKey: String) extends SignatureCalculator {

  override def sign(request: WS.WSRequest): Unit = {
    request.setHeader("Authorization", signature(request))
  }

  private def signature(request: WS.WSRequest): String = {
    val tosign = request.method + "\n" +
      request.header("Content-Type").map(_ + "\n") +
      request.header("Date").map(_ + "\n")
    HmacSha1.calculate(tosign, secretAccessKey)
  }

}
package linky.logic

import java.security.MessageDigest
import java.util.Base64

import akka.http.scaladsl.model.Uri
import com.redis._
import linky.http.models.ShortenEntity


trait UrlShortener {
  private val redisClient = new RedisClient("localhost", 6379)
  private val baseUrl = "http://lin.ky/"
  private val prefix = "re"
  private val notFound = "http://lin.ky/404"


  def generateAndSaveShorterUrl(shortenerEntity: ShortenEntity, userId: String): String = {
    val urlDigest = MessageDigest.getInstance("SHA-1").digest((shortenerEntity.url + userId).getBytes).toString
    val urlEncoded = new String(Base64.getEncoder.encode(urlDigest.getBytes()))
    val shorterPath = prefix + urlEncoded
    redisClient.set(shorterPath, shortenerEntity.url)
    val shorterUrl = shortenerEntity.suggestedBase.getOrElse(baseUrl) + shorterPath
    shorterUrl
  }

  def getUrl(shorterUrl: String): Uri = Uri(redisClient.get(shorterUrl).getOrElse(notFound))
}

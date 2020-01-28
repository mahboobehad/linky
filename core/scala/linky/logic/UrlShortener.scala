package linky.logic

import java.security.MessageDigest

import com.redis._
import linky.http.models.ShortenEntity


trait UrlShortener {
  private val redisClient = new RedisClient("localhost", 6379)
  private val baseUrl = "http://lin.ky/"

  def generateAndSaveShorterUrl(shortenerEntity: ShortenEntity, userId: String): String = {
    val shorter = shortenerEntity.suggestedBase.getOrElse(baseUrl) + MessageDigest.getInstance("MD5").
      digest((shortenerEntity.url + userId).getBytes).toString
    saveShorterUrlToRedis(shortenerEntity.url, shorter)
    shorter
  }

  private def saveShorterUrlToRedis(url: String, shorterUrl: String) = redisClient.set(shorterUrl, url)

  def getUrl(shorterUrl: String): Option[String] = redisClient.get(shorterUrl)
}

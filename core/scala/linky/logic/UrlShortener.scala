package linky.logic

import java.security.MessageDigest

import com.redis._


trait UrlShortener {
  private val redisClient = new RedisClient("localhost", 6379)
  private val  baseUrl = "http://lin.ky/"

  def generateAndSaveShorterUrl(url: String, userId: String): String ={
    val shorter = baseUrl +  MessageDigest.getInstance("MD5").digest((url + userId).getBytes).toString
    saveShorterUrlToRedis(url, shorter)
    shorter
  }

  private def saveShorterUrlToRedis(url: String, shorterUrl: String) = redisClient.set(shorterUrl, url)
}

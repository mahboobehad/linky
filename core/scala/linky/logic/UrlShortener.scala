package linky.logic

import java.security.MessageDigest


trait UrlShortener {
  private val  baseUrl = "http://lin.ky/"
  def generateShorter(url: String): String = baseUrl +  MessageDigest.getInstance("MD5").digest(url.getBytes).toString
  def saveShorterUrlToRedis(key: String, value: String, url: String) = ???
}

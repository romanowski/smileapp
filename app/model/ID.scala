package model

/**
 * Created with IntelliJ IDEA.
 * User: krzysiek
 * Date: 09.06.13
 * Time: 01:29
 * To change this template use File | Settings | File Templates.
 */
trait ID[A] {
  def id: A
}

trait NameID extends ID[String] {
  def name: String
  def id = name
}

package model

import User._
import service.InMemoryServices

/**
 * Created with IntelliJ IDEA.
 * User: krzysiek
 * Date: 08.06.13
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
object Mock extends User("ala", "Ala maKota", "asas") with TaskFeed with FriendFeed {
  def friendFeed: List[TileRenderer] =
    """facet-bez-zdjecia-mowi.png
      |janka-badge-komplementu.png
      |janka-badge.png
      |janka-dzisiaj-mowi.png
      |janka-dzisiaj-usmiech.png
      |janka-komplement.png
      |ona-badge.png
      |ona-bez-zdjecia-mowi.png
      |ona-komplement.png
      |ona-komplement (1).png
      |on-badge-komplementu.png
      |on-dzisiaj-usmiech.png
      |rysiek-badge-komplement.png
      |rysiek-badge.png
      |rysiek-bez-zdjecia-mowi.png
      |rysiek-dzisiaj-usmiech.png
      |rysiek-komplement.png
      |TY.png""".stripMargin.split("\n").map(s => InMemoryServices.imagePath("friends/" + s)).map(MockTile.apply).toList

  "ala ma kota".split(" ").toList.map(MockTile)

  def taskFeed: List[TileRenderer] = "ala ma kota".split(" ").toList.map(MockTile)

}

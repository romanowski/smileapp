package model

import play.api.templates.Html;


trait TileRenderer {
  def headerString: String

  def image: Html

  def content: Html
}


case class MockTile(img: String) extends TileRenderer {
  def headerString: String = "ala"

  def image = views.html.image(img)

  def content = new Html(new StringBuilder ("ol"))
}
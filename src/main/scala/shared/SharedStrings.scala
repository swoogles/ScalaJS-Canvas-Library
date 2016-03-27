package shared

object SharedMessages {
  def itWorks = "It works! Now let's make it work nicely!"
}

object SharedURLs {
  val url_video_index = "/videos/index"
  val url_videos_by_tag_fragment = "/fragments/videos/tags"
  val url_discussions_by_tag_fragment = "/fragments/discussions/tags"
}

object DynamicCanvasClasses {
  val dynamic_items = "dynamic_items"
  val dynamic_doubts = dynamic_items + " " + "dynamic_doubts"
  val dynamic_revealed = dynamic_items + " " + "dynamic_revealed"
  
  val items_attribute = "data-items-id"
}


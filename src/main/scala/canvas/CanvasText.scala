package canvas

import org.scalajs.dom
import org.scalajs.dom.raw.Attr 
import org.scalajs.dom.raw.HTMLUListElement
import org.scalajs.dom.html
import dom.CanvasRenderingContext2D
import html.Canvas
import shared.DynamicCanvasClasses
import shared.CollectionFunctions.randomElement

import animation.RotatingPresenter
import animation.Revealer

object CanvasText {
  val exciting_text_color = "#E9633B"

  def drawTextLines ( wordList: List[String], context: CanvasRenderingContext2D, x: Int, y: Int, lineHeight: Int) =
    wordList.foldLeft(y) { (curY, curLine) =>
      context.fillText(curLine, x, curY)
      curY + lineHeight
    }

  def wrapText(context: CanvasRenderingContext2D, texts: List[String], maxWidth: Int, canvas: Canvas): List[String] = {
    texts flatMap { wrapText(context, _, maxWidth, canvas) }
  }

  def wrapText(context: CanvasRenderingContext2D, text: String, maxWidth: Int, canvas: Canvas): List[String] =
    text.split(" ").foldLeft[List[String]](Nil) { (wrappedWordsSoFar, curWord) =>
      wrappedWordsSoFar match {
        case Nil => List(curWord)
        case currentLine :: filledLines => {
          val testString = currentLine + " " + curWord
          val testWidth = context.measureText(testString).width
          if(testWidth > maxWidth) // Too wide, put word on new line.
            curWord :: currentLine :: filledLines
          else 
            testString :: filledLines
        }
      }
    }.reverse

  def configureBasic(canvas: Canvas, fontSize: Int = 25) = {
    val ctx = canvas.getContext("2d") .asInstanceOf[CanvasRenderingContext2D]
    canvas.width = canvas.parentElement.clientWidth
    ctx.fillStyle = exciting_text_color
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    canvas.style.backgroundColor = "black"
    ctx.font = s"${fontSize}px sans-serif"
  }


  def configureForScrollingText(canvas: Canvas, fontSize: Int = 25) 
    = configureBasic(canvas, fontSize)

  def configureForRevealedText(canvas: Canvas, dynamicCanvasHeight: Int, fontSize: Int = 25) = {
    canvas.height = dynamicCanvasHeight
    configureBasic(canvas, fontSize)
  }

  def getRelatedDataItems(attr: Attr, desiredAttrName: String): List[String] = {
    if ( attr.name == desiredAttrName) {
      val doubtUL = dom.document.getElementById(attr.value)
      val ul = doubtUL.asInstanceOf[HTMLUListElement]
      val ulChildren = ul.children(0).children
      val seq = for ( childId <- 0 to ulChildren.length -1 ) yield {
        ulChildren(childId).innerHTML.toString
      } 
      seq.toList
    } else {
      Nil
    }
  }

  def getItemsForCanvas(canvas: Canvas): List[String] =  {
    val results: Seq[List[String]] = 
      for ( j <- 0 to canvas.attributes.length -1) yield {
        getRelatedDataItems(canvas.attributes(j), DynamicCanvasClasses.items_attribute)
      }
    results.flatten.toList
  }


  def revealAttachedItemsOn(canvas: Canvas, attachedItemRetriever: Canvas=>List[String] = getItemsForCanvas): Unit = {
    val millisecondsPerRefresh = 100

    val attachedItems: List[String] = attachedItemRetriever(canvas)

    val ctx = canvas.getContext("2d") .asInstanceOf[CanvasRenderingContext2D]
    val finalWrappedWordList: List[String] = wrapText(ctx, attachedItems, canvas.width, canvas)
    val lineHeight = 25
    val dynamicCanvasHeight = calcCanvasHeight(lineHeight, finalWrappedWordList)

    configureForRevealedText(canvas, dynamicCanvasHeight)

    val revealer = new Revealer[String]( attachedItems, randomElement )
    dom.setInterval(() => Render.renderRevealed(canvas, revealer), millisecondsPerRefresh)
  }

  def scrollAttachedItemsOn(canvas: Canvas, attachedItemRetriever: Canvas=>List[String] = getItemsForCanvas): Unit = {
    val millisecondsPerRefresh = 100
    configureForScrollingText(canvas)

    val attachedItems: List[String] = attachedItemRetriever(canvas)

    val rotatingPresenter = new RotatingPresenter[String]( attachedItems, randomElement )
    dom.setInterval(() => Render.renderRotating(canvas, rotatingPresenter), millisecondsPerRefresh)
  }

  def calcCanvasHeight(lineHeight: Int, lines: List[String]): Int = {
    lines.length * lineHeight
  }

  def calcStartHeight(lineHeight: Int, lines: List[String], canvasHeight: Int): Int = {
    val totalHeight = lines.length * lineHeight
    canvasHeight / 2 - totalHeight / 2
  }


  def wrapTextAndDraw(context: CanvasRenderingContext2D, texts: List[String], x: Int, y: Int, maxWidth: Int, lineHeight: Int, canvas: Canvas) = {
    val wrappedWordLists: List[String] = wrapText(context, texts, maxWidth, canvas)

    val startHeight = calcStartHeight(lineHeight, wrappedWordLists, canvas.height)

    drawTextLines(wrappedWordLists, context, x, startHeight, lineHeight)
  }

  def wrapTextAndDrawStatic(context: CanvasRenderingContext2D, currentText: List[String], x: Int, y: Int, maxWidth: Int, lineHeight: Int, canvas: Canvas) = {
    val wrappedWordLists: List[String] = wrapText(context, currentText, maxWidth, canvas)
    drawTextLines(wrappedWordLists, context, x, y, lineHeight)
  }

}


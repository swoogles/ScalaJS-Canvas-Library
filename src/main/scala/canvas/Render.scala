package canvas

import org.scalajs.dom.html.Canvas
import org.scalajs.dom.CanvasRenderingContext2D

import animation.RotatingPresenter
import animation.Revealer
import animation.AnimatedDisplay

object Render {
  def clearAndDrawText(canvas: Canvas, text: String, opacity: Float) = {
    clear(canvas) 
    drawText(canvas, text, opacity)
  }

  def clear(canvas: Canvas) = {
    val ctx = canvas.getContext("2d") .asInstanceOf[CanvasRenderingContext2D]
    ctx.clearRect(
      0, 0, canvas.width, canvas.height
    )
  }

  type DrawingFunction = (CanvasRenderingContext2D, List[String], Int, Int, Int, Int, Canvas) => Int

  def drawCommon(canvas: Canvas, texts: List[String], opacity: Float, drawingFunction: DrawingFunction) = {
    val ctx = canvas.getContext("2d") .asInstanceOf[CanvasRenderingContext2D]

    ctx.save()
    ctx.globalAlpha = opacity

    var x = canvas.width / 2
    var y = 20
    drawingFunction(ctx, texts, x, y, (canvas.width-30), 25, canvas)

    ctx.restore()
  }
  
  def drawText(canvas: Canvas, text: String, opacity: Float) =
    drawTextListCentered(canvas, List(text), opacity)

  def drawTextListCentered(canvas: Canvas, texts: List[String], opacity: Float) =
    drawCommon(canvas, texts, opacity, CanvasText.wrapTextAndDraw _)

  def drawTextListStatic(canvas: Canvas, texts: List[String], opacity: Float) =
    drawCommon(canvas, texts, opacity, CanvasText.wrapTextAndDrawStatic _)

  type ListRenderer = (Canvas, List[String], Float) => Unit
  def renderAnimatedDisplay(canvas: Canvas, animatedDisplay: AnimatedDisplay[String], listRenderer: ListRenderer): Unit = {
    val opacity = animatedDisplay.opacity
    clear(canvas)
    listRenderer(canvas, animatedDisplay.displayItems, opacity)
    animatedDisplay.inc()
  }

  def renderRevealed(canvas: Canvas, revealer: Revealer[String]): Unit =
    renderAnimatedDisplay(canvas, revealer, drawTextListStatic _)

  def renderRotating(canvas: Canvas, rotatingPresenter: RotatingPresenter[String]): Unit =
    renderAnimatedDisplay(canvas, rotatingPresenter, drawTextListCentered _)

}



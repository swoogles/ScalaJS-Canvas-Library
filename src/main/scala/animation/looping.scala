package animation

class LoopingCounter(limit: Int) {
  var step = 0
  def inc() {
    step += 1
    if ( step == limit )
      step = 0
  }
  def isReset = (step == 0)
  def percentComplete = ( step.toFloat / limit)
}

trait AnimatedDisplay[T] {
  val items: List[T]
  def inc(): Unit
  def percentComplete: Float
  def opacity: Float
  def displayItems: List[T]
}

class RotatingPresenter[T] (
  val items: List[T], 
  val newItemPicker: List[T] => T, 
  val counter: LoopingCounter = new LoopingCounter(100)
) extends AnimatedDisplay[T] {
  var curItem = newItemPicker(items)
  def inc() {
    counter.inc()
    if ( counter.isReset ) {
      curItem = newItemPicker(items)
    }
  }
  def percentComplete: Float = counter.percentComplete
  def opacity = percentComplete
  def displayItems = List(curItem)
}

class Revealer[T] (
  val items: List[T], 
  val newItemPicker: List[T] => T, 
  val counter: LoopingCounter = new LoopingCounter(10)
) extends AnimatedDisplay[T] {
  var revealedItemsPrivate: List[T] = List()
  def revealedItems: List[T] = revealedItemsPrivate.reverse
  var remainingItems: List[T] = items
  def inc() {
    if ( counter.isReset ) {
      remainingItems match {
        case head :: tail => {
          revealedItemsPrivate = head :: revealedItemsPrivate
          remainingItems = tail
        }
        case Nil => {
        }
      }
    }
    counter.inc()
  }
  def percentComplete = counter.percentComplete
  def opacity = 1.0f
  def displayItems = revealedItems
}



package example

import com.github.ahnfelt.react4s._
import scala.concurrent.Future

object Main {
  def main(arguments: Array[String]): Unit = {
    val component = Component(MainComponent)
    ReactBridge.renderToDomById(component, "main")
  }
}

////

case class MainComponent() extends Component[NoEmit] {
  private val idState = State("")
  private val labels = Seq("tab1","tab2")
  private val activeTabState = State("tab1")

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Component(SearchComponent).withHandler(idState.set),
      E.hr(),
      Component(TabsComponent, labels, get(activeTabState)).withHandler(activeTabState.set),
      Component(SelfLoadingTabContent, TabProp(get(idState), get(activeTabState)))
    )
  }
}

////

case class SearchComponent() extends Component[String]{

  private val searchBoxText = State("")

  override def render(get: Get): ElementOrComponent = {
    E.span(
      E.input(
        A.placeholder("Enter Id..."),
        A.value(get(searchBoxText)),
        A.onChangeText(searchBoxText.set)
      ),
      E.button(
        Text("Find"),
        A.onClick { _ ⇒ emit(get(searchBoxText))}
      )
    )
  }
}

////

case class TabsComponent(labels: P[Seq[String]], active: P[String]) extends Component[String] {
  private val activeState = State.of(active)

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Tags(get(labels).map(renderTabButton(get, _)))
    )
  }

  private def renderTabButton(get: Get, id: String): Element = {
    val active = if (id == get(activeState)) " *" else ""
    E.button(
      A.onClick{ _ ⇒
        activeState.set(id)
        emit(id)
      },
      Text(id + active)
    )
  }
}

////

case class TabProp(id: String, kind: String)

case class SelfLoadingTabContent(prop: P[TabProp]) extends Component[NoEmit] {
  private val loader = Loader(this, prop) { prop ⇒
    if (prop.id.isEmpty) {
      Future.successful("")
    } else {
      println(s"load ${prop.kind}/${prop.id}")
      Future.successful(s"data for ${prop.kind}/${prop.id}")
    }
  }

  override def render(get: Get): ElementOrComponent =
    get(loader).map { data ⇒ E.div(Text(data)) }.getOrElse(E.div(Text("Nothing here")))
}


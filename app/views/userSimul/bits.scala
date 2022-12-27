package views.html
package userSimul

import lila.api.{ Context, given }
import lila.app.templating.Environment.{ given, * }
import lila.app.ui.ScalatagsTemplate.{ *, given }
import lila.common.paginator.Paginator
import lila.user.User

import controllers.routes

object bits:

  def layout(u: User, title: String, path: String, moreJs: Frag = emptyFrag)(
      body: Frag
  )(implicit ctx: Context) =
    views.html.base.layout(
      title = title,
      moreCss = cssTag("user-simul"),
      moreJs = moreJs
    ) {
      main(cls := "page-menu simul-list")(
        st.nav(cls := "page-menu__menu subnav")(
          a(cls := path.active("hosted"), href := routes.UserSimul.path(u.username, "hosted"))(
            "Hosted"
          ),
        ),
        div(cls := "page-menu__content simul-list__content")(body)
      )
    }

  private[userSimul] def setup(sim: lila.simul.Simul) =
    span(cls := List("setup" -> true, "rich" -> sim.variantRich))(
      sim.clock.config.show,
      " • ",
      sim.variants.map(_.name).mkString(", ")
    )

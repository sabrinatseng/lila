package views.html
package userSimul

import lila.api.{ Context, given }
import lila.app.templating.Environment.{ given, * }
import lila.app.ui.ScalatagsTemplate.{ *, given }
import lila.common.paginator.Paginator
import lila.user.User

import controllers.routes

object hosted:

  private val path = "hosted"

  def apply(u: User, pager: Paginator[lila.simul.Simul])(implicit ctx: Context) =
    bits.layout(
      u = u,
      title = s"${u.username} hosted simuls",
      path = path,
      moreJs = infiniteScrollTag
    ) {
      if (pager.nbResults == 0)
        div(cls := "box-pad")(u.username, " hasn't hosted any simul yet!")
      else
        div(cls := "box")(
          table(cls := "slist")(
            thead(
              tr(
                th(cls := "count")(pager.nbResults),
                th(colspan := 2)(h1(userLink(u, withOnline = true), " simuls")),
                th(trans.players())
              )
            ),
            tbody(cls := "infinite-scroll")(
              pager.currentPageResults.map { sim =>
                tr(cls := "paginated")(
                  simTd(sim),
                //   td(cls := "icon")(iconTag(tournamentIconChar(t))),
                //   views.html.tournament.finishedList.header(t),
                //   td(momentFromNow(t.startsAt)),
                //   td(cls := "winner")(
                //     t.winnerId.isDefined option userIdLink(t.winnerId, withOnline = false)
                //   ),
                  td(cls := "players text", dataIcon := "")(sim.pairings.size)
                )
              },
              pagerNextTable(pager, np => routes.UserSimul.path(u.username, path, np).url)
            )
          )
        )
    }

  private def simTd(sim: lila.simul.Simul) =
    td(cls := "header", colspan := 3)(
      a(href := routes.Simul.show(sim.id))(
        span(cls := "name")(sim.fullName),
        bits.setup(sim)
      )
    )

  private def simHost(sim: lila.simul.Simul)(implicit ctx: Context) =
    td(cls := "host")(
      userIdLink(sim.hostId.some, withOnline = false),
      ctx.pref.showRatings option frag(
        br,
        strong(sim.hostRating)
      )
    )
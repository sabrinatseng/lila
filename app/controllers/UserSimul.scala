package controllers

import lila.app.{ given, * }
import views.*

final class UserSimul(env: Env) extends LilaController(env):

  def path(username: UserStr, path: String, page: Int) =
    Open { implicit ctx =>
      Reasonable(page) {
        val userOption =
          env.user.repo.byId(username).map { _.filter(_.enabled.yes || isGranted(_.SeeReport)) }
        OptionFuResult(userOption) { user =>
          path match
            case "hosted" =>
              env.simul.api.byHostPager(user, page).map { pager =>
                Ok(html.userSimul.hosted(user, pager))
              }
            case _ => notFound
        }
      }
    }

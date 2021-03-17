package app.paperhands.config

case class ApiConfig(prefix: String) {
  def prefixPath(path: String) = s"$prefix$path"
}

object Config {
  val api = ApiConfig(
    "https://api.coderats.dev/paperhands"
  )
}

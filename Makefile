.SHELLFLAGS =
SHELL=./scripts/shell.sh

serve:
	live-server --port=5555 --watch="./index.html,./target/scala-2.13/scalajs-bundler/main/paperhands-ui-fastopt-bundle.js*"

# Run as a Docker container

```
# prepare
brew install socat
brew install xquartz

# restart computer

# run on single terminal
socat TCP-LISTEN:6000,reuseaddr,fork UNIX-CLIENT:\"$DISPLAY\"

# cd project_root
make docker-build
make docker-run
```

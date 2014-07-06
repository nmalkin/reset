# Reset

A simple reset timer, inspired by [xkcd #363](https://xkcd.com/363/).

![xkcd #363](http://imgs.xkcd.com/comics/reset.png)

The server and client are implemented with Clojure and ClojureScript, respectively.

## Run it yourself

Make sure you have [Leiningen](https://github.com/technomancy/leiningen), then run `./bin/build`. The server will run at http://127.0.0.1:8080/

There's also a Procfile included (as well as a system.properties file), in case you want to deploy to Heroku. (Note that you'll need to [enable WebSocket support](https://devcenter.heroku.com/articles/heroku-labs-websockets) in this case.)

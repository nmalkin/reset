(defproject reset "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2261"]
                 [http-kit "2.1.16"]
                 [ring/ring-core "1.3.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [compojure "1.1.8"]
                 [reagent "0.4.2"]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :main reset.server
  :min-lein-version "2.0.0"
  :uberjar-name "reset-standalone.jar"
  :profiles {:uberjar {:aot :all}}
  :cljsbuild {
    :builds [{:source-paths ["src"]
              :compiler {
                :output-dir "resources/public/js"
                :output-to "resources/public/js/main.js"
                :optimizations :none
                :pretty-print true
                :source-map true}}]})

(ns reset.server
  (:gen-class)
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [org.httpkit.server :as http]))

; The time of the last reset
(def start-time (atom nil))

(defn reset-clock!
  "Update the reset time to right now."
  []
  (reset! start-time (str (System/currentTimeMillis))))

; Generate unique keys
; (for identifying connections and, specifically, watches on the start time)
(def next-key (atom 0))
(defn get-key []
  (swap! next-key inc))

(defn ws-handler
  "Handle a client WebSocket connection"
  [req]
  (http/with-channel req channel
    (let [channel-key (get-key)]
      ; Tell the client the current reset time.
      (http/send! channel @start-time)

      ; Watch the reset time and notify client when it changes
      (add-watch start-time channel-key (fn [_key _ref old-state new-state]
                                          (http/send! channel new-state)))

      ; Stop watching if the client disconnects
      (http/on-close channel (fn [status]
                               (remove-watch start-time channel-key)))

      ; A message from the client means they want to reset the clock
      (http/on-receive channel (fn [data]
                                 (reset-clock!))))))

(defroutes app-routes
  (GET "/" [] (response/resource-response "index.html" {:root "public"}))
  (GET "/ws" [] ws-handler)
  (route/resources "/")
  (route/not-found "Not Found"))

(defn -main [& args]
  (reset-clock!)
  (http/run-server (site app-routes) {:port (Integer/parseInt (get (System/getenv) "PORT" 8080))}))

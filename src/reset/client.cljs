(ns reset.client
  (:require [reagent.core :as reagent]
            [reset.time :as time]))


; The address of the WebSocket we'll connect to
(def ws-address (str "ws://" js/window.location.host "/ws"))

; The state of the application
(def base-time (atom nil)) ; time when the last reset occurred
(def elapsed (reagent/atom nil)) ; milliseconds since the reset
(defn ^:dynamic trigger-reset "tell the server to trigger a reset" []) ; made available after connection is established

(defn connect
  "Establish and handle communication with server"
  []
  ; Connect to the WebSocket
  (let [socket (js/WebSocket. ws-address)]
    (doto socket
      ; Now that the connection is opened, update the "trigger reset" function
      ; to actually talk to the server.
      (aset "onopen" (fn []
                       (set! trigger-reset (fn []
                                             (.send socket "reset")))))

      ; A message from the server is (always) the new "base time."
      ; Update our local state when we receive it.
      (aset "onmessage" (fn [event]
                          (let [msg (.-data event)
                                new-time (js/parseInt msg)]
                            (reset! base-time new-time)))))))

(defn update-elapsed!
  "Update the time elapsed since the reset"
  []
  (reset! elapsed (- (js/Date.now) @base-time)))


(defn elapsed-display []
  (js/setTimeout update-elapsed! 100)
  [:div (if (nil? @elapsed)
          ""
          (time/format-time @elapsed))])

(defn button []
  [:input {:type "reset"
           :on-click trigger-reset}])

(defn app []
  [:div
   (elapsed-display)
   (button)])

(defn render-app []
  (reagent/render-component [app] (.-body js/document)))


(defn ^:export run []
  (render-app)
  (connect))

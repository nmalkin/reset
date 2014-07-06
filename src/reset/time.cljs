(ns reset.time)

(def day-in-millis (* 1000 60 60 24))
(def hour-in-millis (* 1000 60 60))
(def minute-in-millis (* 1000 60))
(def second-in-millis 1000)

(defn- millis-to-time [millis]
  (let [days (int (/ millis day-in-millis))
        remainder (- millis (* days day-in-millis))
        hours (int (/ remainder hour-in-millis))
        remainder (- remainder (* hours hour-in-millis))
        minutes (int (/ remainder minute-in-millis))
        remainder (- remainder (* minutes minute-in-millis))
        seconds (int (/ remainder second-in-millis))]
    [days hours minutes seconds]))

(defn format-time [millis]
  "Returns a string specifying the number of days, hours, minutes, and seconds that go into given milliseconds"
  (if-not (nil? millis)
    (let [[days hours minutes seconds] (millis-to-time millis)]
      (str days " days, " hours " hours, " minutes " minutes, " seconds " seconds"))))

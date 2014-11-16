(ns tnoda.karasumaclj-20141115
  (:require [clojure.core.async :refer [chan sliding-buffer >!! <!! timeout go-loop >! <! go]]
            [quil.core :as q]))

(def scale 5)

(def size 200)

(def c (chan (sliding-buffer 10000)))

(defn- draw
  []
  (dotimes [_ 1000]
    (let [[x y color] (<!! c)]
      (q/fill color)
      (q/rect (* x scale) (* y scale) scale scale))))

(defn -main
  [& args]
  (dotimes [x size]
    (dotimes [y size]
      (go-loop []
        (>! c [x y (rand-int 255)])
        (<! (timeout (rand-int 1000)))
        (recur))))
  (q/defsketch core-async-demo
    :size [(* size scale) (* size scale)]
    :draw draw))

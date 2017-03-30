(ns lunch-roulette.core
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(defn -main [& args]
  (println "Hello Kaitlin!"))

(set! *main-cli-fn* -main)

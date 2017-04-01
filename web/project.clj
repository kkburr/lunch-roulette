(defproject lunch-roulette "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [cljsjs/react "15.2.1-1"]
                 [cljsjs/react-dom "15.2.1-1"]
                 [reagent "0.6.1"]
                 [sablono "0.7.4"]
                 [org.omcljs/om "0.8.6"]]
  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.5.8"]]
  :clean-targets [:target-path "out"]
  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]
              :figwheel true
              :compiler {:main "lunch-roulette.core"
                         :source-map true}}]})

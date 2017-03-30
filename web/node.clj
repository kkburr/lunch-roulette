(require 'cljs.build.api)

(cljs.build.api/build "src"
  {:main 'lunch-roulette.core
   :output-to "main.js"
   :target :nodejs})

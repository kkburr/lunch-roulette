(require 'cljs.build.api)

(cljs.build.api/watch "src"
  {:main 'lunch-roulette.core
   :output-to "out/main.js"})

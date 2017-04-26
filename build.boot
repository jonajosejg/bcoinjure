(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.5.2" :scope "test"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "1.0.0-beta12")
(def +version+ (str +lib-version+ "-0"))

(def npm-project {'cljsjs/bcoin "bcoin"})

(task-options!
  pom {:project 'cljsjs/bcoin
       :version +version+
       :description "A Javascript Full-Node Implementation of Bitcoin"
       :url "http://bcoin.io"
       :license {"MIT"}})




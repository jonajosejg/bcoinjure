(def cljs-deps
   '[[adzerk/boot-reload "0.4.13" :scope "test"]
      [adzerk/boot-cljs "2.0.0"]
      [org.clojure/clojure "1.9.0-alpha14"]
      [org.clojure/clojurescript "1.9.293"]
      [adzerk/cljs-console "0.1.1"]
      [com.cognitect/transit-cljs   "0.8.239"] ; ClojureScript Wrapper for  JavaScript JSON
      [crisptrutski/boot-cljs-test "0.3.0" :scope "test"]
      [tolitius/boot-check "0.1.4"         :scope "test"]
      [cljs-http "0.1.42"]
      [com.taoensso/timbre "4.9.0-alpha1"] ; Pure Clojure/Script logging library
      ;; ----- Repl Dependencies -------
      [adzerk/boot-cljs-repl    "0.3.3"    :scope "test"] ;; latest release
      [org.clojure/tools.nrepl  "0.2.12"   :scope "test"]
])


(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}
  :dependencies (into cljs-deps))

(require
   '[adzerk.boot-cljs    :refer [cljs]]
   '[boot.util :as util]
   '[adzerk.boot-reload :refer [reload]]
   '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])



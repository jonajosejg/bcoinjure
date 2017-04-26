(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.5.2" :scope "test"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "1.0.0-beta12")


(def npm-project {'bcoin-org/bcoin "bcoin"})


(defn with-files!
  "Runs Middleware with filtered fileset and merges the result back into  complete fileset."
  [p middleware]
   (fn [next-handler]
     (fn [fileset]
       (let [merge-fileset-handler
         (fn [fileset']
	   (next-handler (commit! (assoc fileset :tree (merge (:tree fileset)
	     (:tree fileset'))))))
	handler (middleware merge-filset-handler)
	fileset (assoc fileset :tree  (reduce-kv
	                                (fn [tree path x]
					  (if (p x)
					    (assoc tree path x)
					    tree))
					(empty (:tree fileset))
					(:tree fileset)))]
	(handler fileset)))))


(deftask package-bcoin []
 (package-part
   {:extern-name "bcoin.ext.js"}))

(deftask package []
  (comp
   (package-bcoin)))




(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.5.2" :scope "test"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "1.0.0-beta.12")
(def +version+ (str +lib-version+ "-beta.12"))

(def npm-project {'bcoin-org/bcoin "bcoin"})

(task-options!
   pom {:project 'bcoin-org/bcoin
        :description "Javascript bitcoin library for node.js and browsers"
	:url "http://bcoin.io"})

(defn with-files!
  "Runs Middleware with filtered fileset and merges the result back into  complete fileset."
  [p middleware]
   (fn [next-handler]
     (fn [fileset]
       (let [merge-fileset-handler
         (fn [fileset']
	   (next-handler (commit! (assoc fileset :tree (merge (:tree fileset)
	     (:tree fileset'))))))
	handler (middleware merge-fileset-handler)
	fileset (assoc fileset :tree  (reduce-kv
	                                (fn [tree path x]
					  (if (p x)
					    (assoc tree path x)
					    tree))
					(empty (:tree fileset))
					(:tree fileset)))]
	(handler fileset)))))


(defn package-part [{:keys [extern-name namespace project dependencies requires]}]
  (with-files! (fn [x] (= extern-name (.getName (tmp-file x))))
   (comp
     (download :url (format "https://unpkg.com/%s@%s/dist/%s.js" (npm-project project)                   +lib-version+ (name project))
         :checksum (:min (get checksums project)))
     (download :url (format "https://unpkg.com/%s@%s/dist/%s.min.js"
     (npm-project project) +lib-version+ (name project))
         :checksum (:min (get checksums project)))
     (sift :move {(re-pattern (format "^%.js$" (name project)))
         (format "bcoin/%$s" (name project))})
     (sify :include #{#"^cljsjs"})
     (deps-cljs :name namespace :requires requires)
     (pom :project project :dependencies (or dependencies []))
     (show :fileset true)
     (jar))))


(deftask package-bcoin []
 (package-part
   {:extern-name "bcoin.ext.js"}))

(deftask package []
  (comp
   (package-bcoin)))

(defn md5sum [fileset name]
   (with-open [is (clojure.java.io/input-stream (tmp-file (tmp-get fileset name)))
               dis (java.security.DigestInputStream. is
	       (java.security.MessageDigest/getInstance "MD5"))]
	(#'cljsjs.boot-cljsjs.packaging/realize-input-stream! dis)
	(#'cljsjs.boot-cljsjs.packaging/message-digest->str (.getMessageDigest
	dis))))


(defn load-checksums
  "Task to create Checksums map for new Version"
  []
  (comp
    (reduce
      (fn [handler project]
       (comp handler
         (download :url  (format "https://unpkg.com/%s@%s/dist/%s.js" (npm-project
	 project) +lib-version+ (name project)))
	  (download :url (format "https://unpkg.com/%s@%s/dist/%s.min.js"
	  (npm-project project) +lib-version+ (name project)))))
  identity
   (keys checksums))
     (fn  [handler]
       (fn [fileset]
         (println (clojure.string/replace
           (with-out-str
	     (clojure.pprint/pprint (into {}
	         (map (juxt identity (fn [project]
		    {:dev (md5sum fileset (format "%s.js" (name project)))
		     :min (md5sum fileset (format "%s.min.js" (name
		     project)))}))
		       (keys checksums)))))
	#"bcoin" "'bcoin"))
	fileset))))

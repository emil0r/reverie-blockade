(ns reverie.blockade.middleware
  (:require [clojure.string :as str]
            [reverie.database :as db]
            [yesql.core :refer [defqueries]]))

(defqueries "reverie/blockade/queries/blockade.sql")

(defn- get-domain [referer]
  (if referer
    (last (re-find #".*://([^/:]+).*" referer))))

(defn- get-ip [request]
  (if-let [ip (get-in request [:headers "x-forwarded-for"])]
    (-> ip
        (str/split #",")
        first)
    (:remote-addr request)))

(defn wrap-blockade [handler & [opts]]
  (fn [request]
    (let [db (get-in request [:reverie :database])
          domain (get-domain (get-in request [:headers "referer"]))
          ip (get-ip request)
          blocked? (->> (db/query db sql-is-blocked?
                                  {:domain domain
                                   :ip ip})
                        first :blocked_p)]
      (if blocked?
        (do (when (:log? opts)
              (db/query! db
                         {:insert-into :blockade_stats
                          :values [{:ip ip
                                    :domain domain
                                    :useragent (get-in request [:headers "useragent"])}]}))
            {:status 403
             :body "Forbidden"
             :headers {"Content-Type" "text/plain"}})
        (handler request)))))

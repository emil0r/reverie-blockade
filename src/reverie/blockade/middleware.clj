(ns reverie.blockade.middleware
  (:require [clojure.core.memoize :as memo]
            [clojure.string :as str]
            [ez-database.core :as db]))

(defn- get-domain [referer]
  (if referer
    (last (re-find #".*://([^/:]+).*" referer))))

(defn- get-ip [request]
  (if-let [ip (get-in request [:headers "x-forwarded-for"])]
    (-> ip
        (str/split #",")
        first)
    (:remote-addr request)))

(defn- get-domains* [db]
  (let [domains (db/query db {:select [:domain :ip]
                              :from [:blockade_list]
                              :where [:= :active_p true]})]
    (->> domains
         (map (fn [{:keys [domain ip]}]
                [domain ip]))
         (flatten)
         (remove str/blank?)
         (into #{}))))

;; avoid hitting the database all the time
(def get-domains (memo/ttl get-domains* :ttl/threshold (* 60 1000)))

(defn wrap-blockade [handler & [opts]]
  (fn [{{db :database} :reverie :as request}]
    (let [domain (get-domain (get-in request [:headers "referer"]))
          ip (get-ip request)
          domains (get-domains db)
          blocked? (or (contains? domains domain)
                       (contains? domains ip))]
      (if blocked?
        (do (when (:log? opts)
              ;; this should go into a queue of some sort
              (db/query! db
                         {:insert-into :blockade_stats
                          :values [{:ip ip
                                    :domain domain
                                    :useragent (get-in request [:headers "useragent"])}]}))
            {:status 403
             :body "Forbidden"
             :headers {"Content-Type" "text/plain"}})
        (handler request)))))

(ns reverie.blockade.modules.blockade
  (:require [reverie.core :refer [defmodule]]
            [vlad.core :as vlad]))


(defmodule blockade
  {:name "Blockade"
   :interface? true
   :migration {:path "resources/migrations/modules/blockade/"
               :automatic? true}
   :actions #{:view :edit}
   :required-roles {:view #{:admin :staff}
                    :edit #{:admin :staff}}
   :template :admin/main
   :entities
   {:list {:name "Blockade list"
           :table :blockade_list
           :interface {:display {:title {:name "Title"
                                         :link? true
                                         :sort :t
                                         :sort-name :id}
                                 :domain {:name "Domain"
                                          :sort :d}
                                 :ip {:name "IP"
                                      :sort :ip}
                                 :active_p {:name "Active?"
                                            :sort :a}}
                       :default-order :id}
           :fields {:title {:name "Title"
                            :type :text
                            :max 255
                            :validation (vlad/attr [:title] (vlad/present))}
                    :domain {:name "Domain"
                             :type :text
                             :max 1024
                             :help "Block on domain name"}
                    :ip {:name "IP v4/v6"
                         :type :text
                         :max 39
                         :help "Block on ip v4 or ipv6 address"}
                    :active_p {:name "Active?"
                               :type :boolean
                               :default true}}
           :sections [{:fields [:title :domain :ip :active_p]}]}}})

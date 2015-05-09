(ns reverie.blockade.modules.blockade
  (:require [reverie.core :refer [defmodule]]
            vlad))


(defmodule blockade
  {:name "Blockade"
   :interface? true
   :migration {:path "src/reverie/blockade/modules/migrations/blockade/"
               :automatic? true}
   :actions #{:view :edit}
   :required-roles {:view #{:admin :staff}
                    :edit #{:admin :staff}}
   :template :admin/main
   :entities
   {:list {:name "Blockade list"
           :order :id
           :table :blockade_list
           :display [:title :domain :ip :active_p]
           :fields {:title {:name "Title"
                            :type :text
                            :max 255
                            :validation (vlad/present [:title])}
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

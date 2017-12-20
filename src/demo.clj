(ns demo
  (:require [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

;; code
						
(defn tomorrow [date]
  (time/plus date (time/days 1)))

(defn days-between [date1 date2]
  (time/in-days (time/interval date1 date2)))

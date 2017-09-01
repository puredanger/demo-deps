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

;; specs

(defn datetime? [d]
  (instance? org.joda.time.DateTime d))

(s/def ::datetime
  (s/with-gen datetime?
    #(gen/fmap coerce/from-long
		  (s/gen (s/int-in (coerce/to-long "1980-01-01") (coerce/to-long "2020-01-01"))))))

(s/fdef tomorrow
  :args (s/cat :date ::datetime)
  :ret ::datetime)

(s/fdef days-between
  :args (s/& (s/cat :date1 ::datetime :date2 ::datetime)
          (fn [{:keys [date1 date2]}] (time/before? date1 date2)))
  :ret nat-int?)

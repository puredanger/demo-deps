(ns test-demo
  (:require [demo :refer :all]
            [clojure.spec.test.alpha :as stest]))

(-> 'demo stest/enumerate-namespace stest/check stest/summarize-results)

demo-deps
========================================

This project demonstrates an example project and how to use `clj` and the `deps.edn` file
to manage a simple project. The code here is a single namespace with some simple functions.
The functions use the clj-time library.

The deps.edn file contains a basic setup and some common aliases:

```clojure
{
 :paths ["src"]

 :deps {
   clj-time {:mvn/version "0.14.2"}
 }

 :aliases {
   ;; test src
   :test {:extra-paths ["test"]}

   ;; test.check 
   :test {:extra-deps {org.clojure/test.check {:mvn/version "0.9.0"}}}

   ;; benchmarking
   :bench {:extra-deps {criterium {:mvn/version "0.4.4"}}}
 }
}
```

### Running a REPL

The primary project classpath includes the following:

* Clojure (from ~/.clojure/deps.edn)
* The local source directory
* clj-time, an external Maven dependency

To run a repl, call `clj` with no other arguments. This will create and cache the classpath (for later executions) and start the repl.

```shell
$ clj
Clojure 1.9.0
user=> (require '[demo :as demo] '[clj-time.core :as time])
nil
user=> (str (demo/tomorrow (time/now)))
"2017-09-02T20:50:56.413Z"
```

### Running tests

This project includes specs and a test namespace that run the `stest/check` generative test runner for those specs. Here's the test namespace (stored at test/test_demo.clj):

```clojure
(ns test-demo
  (:require [demo :refer :all]
            [clojure.spec.test.alpha :as stest]))

(-> 'demo stest/enumerate-namespace stest/check stest/summarize-results)
```

To run these tests you need to include two additional things on your classpath:

1) The test source directory (here, `test`)
2) The `test.check` library

These are added in the `:test` and `:check` aliases and can be added to the classpath by activating the aliases:

```shell
clj -R:check -C:test
```

However, we don't really want a REPL, we just want to run the tests. The `clj` script is running `clojure.main` and you can get the full rundown on available options with `clj -h`. One option is `-e` which can be used to run one or more expressions specified as an argument. That's sufficient for our needs - just loading the namespace is enough to run the tests. 

```shell
$ clj -R:check -C:test -e "(require 'test-demo) (shutdown-agents)"

{:sym demo/days-between}
{:sym demo/tomorrow}
```

One special case here is that `stest/check` runs tests in parallel using `pmap`, and thus activates the background thread pool. Those threads are kept alive for one minute by default - we call `shutdown-agents` to shutdown them down and allow the Clojure runtime to exit immediately.

### Benchmarking

If you want to benchmark some code, you might include an external library just for that use, like `criterium`. We've included an alias `:bench` to add this dependency.

```shell
$ clj -R:bench
Clojure 1.9.0
user=> (use 'criterium.core 'demo 'clj-time.core)
user=> (def d (clj-time.core/now))
user=> (quick-bench (tomorrow d))
Evaluation count : 20542836 in 6 samples of 3423806 calls.
             Execution time mean : 31.741978 ns
    Execution time std-deviation : 1.890868 ns
   Execution time lower quantile : 30.409056 ns ( 2.5%)
   Execution time upper quantile : 34.613113 ns (97.5%)
                   Overhead used : 1.854455 ns
```

### Creating and deploying artifacts

`clj` is designed to do two things: make classpaths and run Clojure programs. Thus, this is not something that `clj` does or will provide. We recommend using one of the many Clojure build tools like `Leiningen`, `Boot`, `Maven`, etc.

Copyright and License
========================================

Copyright (c) Alex Miller, 2017. All rights reserved.  The use and distribution terms for this software are covered by the Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can be found in the file epl-v10.html at the root of this distribution. By using this software in any fashion, you are agreeing to be bound bythe terms of this license.  You must not remove this notice, or any other, from this software.

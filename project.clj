(defproject om-tut2 "0.2.0-SNAPSHOT"
  :description "Almost Intermediate Tutorial updated for Om 0.7.0"
  :url "https://github.com/tmarble/om-tut2"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2280"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.7.0"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :jvm-opts ["-server" "-Xmx128m"] ;; optional

  :cljsbuild {
    :builds [{:id "om-tut2"
              :source-paths ["src"]
              :compiler {
                :output-to "om_tut2.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})

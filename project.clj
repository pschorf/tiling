(defproject tiling "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [selmer "0.8.4"]
                 [com.taoensso/timbre "4.0.2"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.67"]
                 [environ "1.0.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-session-timeout "0.1.0"]
                 [metosin/ring-middleware-format "0.6.0" :exclusions [org.clojure/tools.reader]]
                 [metosin/ring-http-response "0.6.3"]
                 [bouncer "0.3.3"]
                 [prone "0.8.2"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [ring-server "0.4.0"]
                 [ragtime "0.4.2"]
                 [org.postgresql/postgresql "9.3-1103-jdbc41"]
                 [yesql "0.5.0-rc3"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [org.clojure/clojurescript "0.0-3308"]
                 ]

  :min-lein-version "2.0.0"
  :uberjar-name "tiling.jar"
  :jvm-opts ["-server"]

;;enable to start the nREPL server when the application launches
  :env {:repl-port 7001}

  :main tiling.core

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.7"]
            [lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.3.7"]
            ]

  :aliases {"migrate" ["run" "-m" "tiling.db.migrate/migrate"]}

  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :figwheel true
                        :compiler {:output-to "resources/public/js/main.js"
                                   :output-dir "resources/public/js/out"}}]}
              
  

  
  :ring {:handler tiling.handler/app
         :init    tiling.handler/init
         :destroy tiling.handler/destroy
         :uberwar-name "tiling.war"}

  
  
  
  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             
             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.4.0"]
                        [pjstadig/humane-test-output "0.7.0"]
                        ]
         
         
         :repl-options {:init-ns tiling.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true
               :database-url "postgresql://postgres:@db/postgres"}}})

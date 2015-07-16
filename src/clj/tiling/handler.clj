(ns tiling.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes]]
            [tiling.routes.home :refer [home-routes]]
            [tiling.routes.files :refer [file-routes]]
            [tiling.routes.users :refer [user-routes]]
            [cider.nrepl :refer [cider-nrepl-handler]]
            [tiling.middleware :as middleware]
            [tiling.session :as session]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [clojure.tools.nrepl.server :as nrepl]))

(defonce nrepl-server (atom nil))

(defroutes base-routes
           (route/resources "/")
           (route/not-found "Not Found"))

(defn start-nrepl
  "Start a network repl for debugging when the :repl-port is set in the environment."
  []
  (when-let [port (env :repl-port)]
    (try
      (reset! nrepl-server (nrepl/start-server :port port :handler cider-nrepl-handler :bind "0.0.0.0"))
      (timbre/info "nREPL server started on port" port)
      (catch Throwable t
        (timbre/error "failed to start nREPL" t)))))

(defn stop-nrepl []
  (when-let [server @nrepl-server]
    (nrepl/stop-server server)))

(def timbre-config
  {:level (if (env :dev) :trace :info)
   :enabled? true
   :output-fn timbre/default-output-fn
   :appenders
   {:rotor (rotor/rotor-appender {:path "tiling.log"})
    :println
    {:enabled? true
     :async? false
     :min-level nil
     :output-fn :inherit
     :fn (fn [data]
           (let [{:keys [output-fn]} data
                 formatted-output (output-fn data)]
             (println formatted-output)))}}})

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config! timbre-config)

  (if (env :dev) (parser/cache-off!))
  (start-nrepl)
  ;;start the expired session cleanup job
  (session/start-cleanup-job!)
  (timbre/info "\n-=[ tiling started successfully"
               (when (env :dev) "using the development profile") "]=-"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "tiling is shutting down...")
  (stop-nrepl)
  (timbre/info "shutdown complete!"))

(def app
  (-> (routes
       (wrap-routes home-routes middleware/wrap-csrf)
       (wrap-routes file-routes middleware/wrap-csrf)
       (wrap-routes user-routes middleware/wrap-csrf)
        base-routes)
      middleware/wrap-base))

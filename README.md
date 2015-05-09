# reverie-blockade

Block clients based on ip or domain. Written specifically for spambots polluting google analytics and tools. This is a module and middleware for reverie/CMS.

## Usage

In your init file you need to load the namespace where the module is located.
```clojure
(require '[reverie.system :refer [load-views-ns])

;; code for initializing the system ...

  (load-views-ns 'reverie.blockade.modules
                 'your-project-name.objects
                 'your-project-name.templates
                 'your-project-name.apps
                 'your-project-name.endpoints
                 )

;; more code ..

```

You also need to pass the middleware to the server for inclusion.

```clojure

(require '[reverie.blockade.middleware :refer [wrap-blockade]])

;; in the function for initiating the system with component

  ;; more for the component system-map here
  :server (server/get-server {:server-options server-options
                              :run-server run-server
                              :stop-server stop-server
                              :dev? (not prod?)
                              :extra-handlers [;; more wraps before or after
                                               [wrap-blockade {:log? true}]]
                               ;; more options
                             })
  ;; and more here...

```

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

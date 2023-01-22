# Clojure koulutus 2023-01-14

## Links

- [Try Clojure](https://tryclojure.org) ClojureScript REPL on web
- [Clojure API](https://clojure.org/api/api) Official API docs
- [ClojureDocs](https://clojuredocs.org) Community docs with examples etc.
- [ClojureScript API](http://cljs.github.io/api/) ClojureScript API docs
- [Google Closure API](https://google.github.io/closure-library/api/) Commonly used Closure lib
- [Shadow-cljs](https://shadow-cljs.github.io/docs/UsersGuide.html) Shadow-cljs build tool docs

## Dataset

Da fuck I'm trying to do?

- Clojure philosophy, history
- https://tryclojure.org
  - "Hello"
  - (+ 1 2)
  - (+ 1 2 (\* 2 20) -1)
  - (js/alert "Greetings!")
  - (defn say-hi [name] (js/alert (str "Hi, " name)))
  - (say-hi "Jarppe")
- REPL
  - What REPL actually does
  - TODO: Steps to launch REPL
  - tryclojure steps on REPL
  -
  ```clojure
    (import '(javax.swing JOptionPane))
    (JOptionPane/showMessageDialog nil "Hello!")
  ```
- Scalars, functions, collections
  - string, char, number, boolean
  - call functions
  - make function
  - collections
- Immutability
  - why?
  - how?
- Seq, lazy-seq
  - pitfalls with lazyness
- macros, ->
- Map, filter, reduce

  - http-clj
  - try with "https://randomuser.me/api/"

```clojure
(require '[clj-http.client :as http])
(require '[clojure.pprint :refer [pprint]])
(require '[jsonista.core :as j])
(require '[clojure.string :as str])

(-> "https://randomuser.me/api/"
    (http/get {:accept       :json
                :query-params {"nat" "fi"}})
    :body
    (j/read-value j/keyword-keys-object-mapper)
    :results
    (first)
    (select-keys [:email :name])
    (update :name (comp (partial str/join " ") (juxt :first :last))))
```

fetch records
filter by genre
filter by date
map tracks
mapcat tracks
map track-length
reduce with +

Eli tarvitaan:
postgres image jossa datat valmiina
http rest image joka palvelee db datat json

testing!

https://adventofcode.com/2022/day/1

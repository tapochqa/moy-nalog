(ns moy-nalog.util
  (:require
    [java-time.api :as jt]
    
    [clojure.string :as str]))

(defn money-format
  [num]
  (java.lang.String/format java.util.Locale/US "%.2f" (to-array [(float num)])))


(defn make-timestamp
  [offset]
  (str (jt/offset-date-time (jt/zone-offset offset))))


(defn random-id
  [length]
  (-> (random-uuid)
    (str)
    (str/replace #"-" "")
    (subs 0 length)))
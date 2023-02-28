(ns moy-nalog.const)

(def ENDPOINT "https://lknpd.nalog.ru/api/v1")

(def USER-AGENT "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.192 Safari/537.36")

(def HEADERS
  {"accept" "application/json, text/plain, */*; q=0.01"
   "accept-language" "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7"
   "content-type" "application/json"
   "referer-policy" "strict-origin-when-cross-origin"})
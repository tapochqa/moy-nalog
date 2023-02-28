Обертка для апи [личного кабинета самозанятых](https://lknpd.nalog.ru). 

Умеет регистрировать чеки и получать информацию о пользователе. 

Авторизация на данный момент только по логину и паролю.

```clojure

; project.clj
:dependencies [[moy-nalog "0.1.0"]]

; core.clj
(:require [moy-nalog.core :as moy-nalog])

(def CONFIG {:login "340000000000" :password "..."}) 
  
(add-income CONFIG "Сведение" 4000)
(user-info CONFIG)

```
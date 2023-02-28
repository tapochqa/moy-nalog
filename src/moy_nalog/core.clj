(ns moy-nalog.core
  (:require
    
    [moy-nalog.const :as const]
    [moy-nalog.util :as util]
    
    [cheshire.core :as json]
    [org.httpkit.client :as http]
    
    [clojure.math :as math]
    [clojure.string :as str]))


(defn get-token
  [{:keys [login password]}]
  
  (let [device-id 
        (util/random-id 21)
        
        device-info
        {:sourceDeviceId device-id
         :sourceType "WEB"
         :appVersion "1.0.0"
         :metaDetails 
         {:userAgent const/USER-AGENT}}
        
        {:keys [body]}
        @(http/request
          {:method :post
           :url (str const/ENDPOINT "/auth/lkfl")
           
           :headers
           (assoc const/HEADERS "referer" "https://lknpd.nalog.ru")
           
           :body
           (json/generate-string
             
             {:username login
              :password password
              
              :deviceInfo 
              device-info})})
        
        {:keys [refreshToken]}
        (json/parse-string body true)
        
        
        {:keys [body]}
        @(http/request
          {:method :post
           :url (str const/ENDPOINT "/auth/token")
           
           :headers
           (assoc const/HEADERS "referer" "https://lknpd.nalog.ru/sales")
           
           :body
           (json/generate-string
             
             {:deviceInfo 
              device-info
              
              :refreshToken
              refreshToken})})
        
        {:keys [token] :as response}
        (json/parse-string body true)]
    
    token))


(defn api-request
  
  ([config api-method http-method]
   (api-request config api-method http-method nil))
  
  ([config api-method http-method payload]
    (let
      [{:keys [body]}
       @(http/request 
       
         {:url 
          (str const/ENDPOINT "/" (name api-method))
          
          :method 
          http-method
          
          :headers 
          (assoc const/HEADERS 
            "authorization" (str "Bearer " (get-token config))
            "referer" "https://lknpd.nalog.ru/sales/create")
          
          :body (json/generate-string payload)})]
      (json/parse-string body true))))


(defn add-income
  ([config title amount]
   (add-income config title amount nil))
  
  ([config title amount
         {:keys  [paymentType
                  ignoreMaxTotalIncomeRestriction
                  contactPhone
                  displayName
                  incomeType
                  inn
                  quantity
                  operationTime
                  timezone]
           :or   {timezone "+03:00"
                  paymentType "CASH"
                  ignoreMaxTotalIncomeRestriction false
                  incomeType "FROM_INDIVIDUAL"
                  quantity 1
                  operationTime (util/make-timestamp timezone)}}]
   
   {:pre [(contains? 
            #{"FROM_FOREIGN_AGENCY" "FROM_INDIVIDUAL" "FROM_LEGAL_ENTITY"}
            incomeType)
          (contains?
            #{"CASH" "WIRE"}
            paymentType)]}
   
   (api-request config :income :post
     
     {:paymentType paymentType
      :ignoreMaxTotalIncomeRestriction ignoreMaxTotalIncomeRestriction
      
      :client
      {:contactPhone contactPhone
       :displayName displayName
       :incomeType incomeType
       :inn inn}
      
      :requestTime (util/make-timestamp timezone)
      :operationTime operationTime
      :services 
      [{:name title
        :amount (util/money-format amount)
        :quantity quantity}]

      :totalAmount
      (util/money-format (* amount quantity))})))


(defn user-info
  [config]
  (api-request config :user :get))



(comment
  
  (def CONFIG {:login "340000000000" :password "..."}) 
  
  (add-income CONFIG "Сведение" 4000)
  (user-info CONFIG)
  
  )
(ns palikka.context-provider)

(defprotocol ContextProvider
  (provide-context [this]
    "Return context map that this component wishes to contribute"))

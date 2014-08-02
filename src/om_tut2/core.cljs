(ns om-tut2.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [goog.dom :as gdom]))

(enable-console-print!)

(def app-state
  (atom
    {:people
     [{:type :student :first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
      {:type :student :first "Alyssa" :middle-initial "P" :last "Hacker"
       :email "aphacker@mit.edu"}
      {:type :professor :first "Gerald" :middle "Jay" :last "Sussman"
       :email "metacirc@mit.edu" :classes [:6001 :6946]}
      ;; for professors :classes is a vector of :class/id
      {:type :student :first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
      {:type :student :first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
      {:type :professor :first "Hal" :last "Abelson" :email "evalapply@mit.edu"
       :classes [:6001]}]
     :classes ;; each class has a :class/id and :class/title
     [{:class/id :6001
       :class/title "The Structure and Interpretation of Computer Programs"}
      {:class/id :6946
       :class/title "The Structure and Interpretation of Classical Mechanics"}
      {:class/id :1806
       :class/title "Linear Algebra"}]}))

(defn middle-name [{:keys [middle middle-initial]}]
  (cond
    middle (str " " middle)
    middle-initial (str " " middle-initial ".")))

(defn display-name [{:keys [first last] :as contact}]
  (str last ", " first (middle-name contact)))

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn handle-change [e data edit-key owner]
  (om/transact! data edit-key (fn [_] (.. e -target -value))))

;; cb is the on-edit callback (a closure bound to the :class/id)
(defn end-edit [text owner cb]
  (om/set-state! owner :editing false)
  (cb text))

;; in the Intermediate tutorial this function is used to
;; persist changes.. here just print the update on the console
(defn on-edit [id title]
  (println "on-edit :class/id=" id " :class/title=" title))

;; Om editable component
(defn editable [data owner {:keys [edit-key on-edit] :as opts}]
  (reify
    om/IInitState
    (init-state [_]
      {:editing false})
    om/IRenderState
    (render-state [_ {:keys [editing]}]
      (let [text (get data edit-key)]
        (dom/li nil
          (dom/span #js {:style (display (not editing))} text)
          (dom/input
            #js {:style (display editing)
                 :value text
                 :onChange #(handle-change % data edit-key owner)
                 :onKeyDown #(when (= (.-key %) "Enter")
                               (end-edit text owner on-edit))
                 :onBlur #(when (om/get-state owner :editing)
                             (end-edit text owner on-edit))})
          (dom/button
            #js {:style (display (not editing))
                 :onClick #(om/set-state! owner :editing true)}
            "Edit"))))))

(defn student-view [student owner]
  (reify
    om/IRender
    (render [_]
      (dom/li nil (display-name student)))))

(defn professor-view [professor owner]
  (reify
    om/IRender
    (render [_]
      (dom/li nil
        (dom/div nil (display-name professor))
        (dom/label nil "Classes")
        (apply dom/ul nil
          (map
            (fn [class]
              (let [id (:class/id class)]
                (om/build editable class
                  {:opts {:edit-key :class/title
                          :on-edit #(on-edit id %)}})))
              (:classes professor)))))))

(defmulti entry-view (fn [person _] (:type person)))

(defmethod entry-view :student
  [person owner] (student-view person owner))

(defmethod entry-view :professor
  [person owner] (professor-view person owner))

;; return the :people from the app-state (including all class details)
(defn people [app]
  (->> (:people app)
    (mapv (fn [person]
            (if (:classes person)
              ;; for professors convert each :class/id into the class
              (update-in person [:classes]
                (fn [classes] ;; originally a vector of :class/id
                  ;; (println "update-in for " (:first person)
                  ;;   " classes=" classes)
                  (mapv
                    (fn [id]
                      ;; return the matching class for id
                      (first (filter #(= (:class/id %) id) (:classes app))))
                    classes)))
              ;; students are unchanged
              person)))))

(defn registry-view [app owner]
  (reify
    om/IRenderState
    (render-state [_ state]
      (dom/div #js {:id "registry"}
        (dom/h2 nil "Registry")
        (apply dom/ul nil
          (om/build-all entry-view (people app)))))))

(defn classes-view [app owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:id "classes"}
        (dom/h2 nil "Classes")
        (apply dom/ul nil
          (map
            (fn [class]
              (let [id (:class/id class)]
                (om/build editable class
                  {:opts {:edit-key :class/title
                          :on-edit #(on-edit id %)}})))
            (:classes app)))))))

(om/root registry-view app-state {:target (gdom/getElement "registry")})

(om/root classes-view app-state {:target (gdom/getElement "classes")})

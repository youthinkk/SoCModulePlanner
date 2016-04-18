;;;;;;;;;;;;;;;
;; TEMPLATES ;;
;;;;;;;;;;;;;;;

(deftemplate module    "Module Info"
    (slot code
        (type SYMBOL))
    (slot level
        (type INTEGER))
    (slot credits
        (type INTEGER)  
        (default 4))
    (slot type
    	(type SYMBOL)
    	(default Module))
    (slot status
    	(type SYMBOL)
    	(default unavailable))
    (slot semester              ; ; semester that plan to take
    	(type INTEGER)
    	(default 0))
    (slot minimum-semester      ; ; minimum semester to take
        (type INTEGER)
        (default 0))
    (multislot coreq  
        (type SYMBOL))
    (multislot prereq
    	(type SYMBOL))
    (multislot offer
        (type INTEGER)))
        
(deftemplate management
	(slot current-semester
		(type INTEGER))
	(slot number-of-module
		(type INTEGER)
		(default 0))
    (slot must-plan-number-module
        (type INTEGER)
        (default 40))
    (slot accumulative-credits
        (type INTEGER)
        (default 0))
	(slot preferred-module-amount  ; ; Preferred amount of modules to take per semester
		(type INTEGER)
		(default 5)))

(deftemplate prereq
    (slot code
        (type SYMBOL))
    (slot minimum-semester
        (type INTEGER))
    (slot fulfill
        (type SYMBOL)
        (default TRUE)))

;;;;;;;;;;;;;
;; GLOBALS ;;
;;;;;;;;;;;;;

(defglobal ?*elective-count* = 1)

;;;;;;;;;;;;;;;;;;
;; HELPER RULES ;;
;;;;;;;;;;;;;;;;;;

; ; Remove the prerequisite when it is fulfilled
(defrule remove-prereq
    (declare (salience 10))
    ?prereq <- (prereq (code ?code) (minimum-semester ?minimum-semester))
    ?module <- (module (code ?code-mod) (prereq $?prerequisites))
    (test (member$ ?code ?prerequisites))
    =>
    (bind ?index (member$ ?code ?prerequisites))
    (bind $?result (delete$ (create$ ?prerequisites) 1 1))
    (modify ?module (prereq ?result) (minimum-semester ?minimum-semester))
    (printout t "Remove prereq: " ?code " from " ?code-mod crlf))

; ; Shift to next semester
(defrule shift-semester
    (declare (salience 10))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (preferred-module-amount ?prefer))
    (or (test (eq ?number-of-module ?prefer)) (test (eq (mod ?current-semester 4) 3)) (test (eq (mod ?current-semester 4) 0)))
    =>
    (bind ?next-semester (+ ?current-semester 1))
    (modify ?management (current-semester ?next-semester) (number-of-module 0))
    (refresh shift-semester)
    (printout t "Shift to Semester " ?next-semester crlf))

; ; Mark the module available        
(defrule mark-available
    (declare (salience 9))
    ?module <- (module (code ?code) (status unavailable) (prereq))
    =>
    (modify ?module (status available))
    (printout t ?code " is available now" crlf))

(defrule program-end
    (declare (salience 20))
    ?management <- (management (must-plan-number-module ?must-modules) (accumulative-credits ?credits))
    (test (eq ?must-modules 0))
    (test (>= ?credits 160))
    =>
    (halt))

;;;;;;;;;;;;;;;;;;;;;
;; MODULE PLANNING ;;
;;;;;;;;;;;;;;;;;;;;;

; ; Plan module with offering at semester 1 only, with corequisite
(defrule plan-sem1-with-coreq
    (declare (salience 8))
    ?module1 <- (module (code ?code) (credits ?credits1) (minimum-semester ?minimum-semester1) (status available) (offer 1) (coreq ?coreq))
    ?module2 <- (module (code ?coreq) (credits ?credits2) (minimum-semester ?minimum-semester2) (status available) (offer 1))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?accumulative-credits))
    (test (eq (mod ?current-semester 4) 1))
    (test (< ?number-of-module 4))
    (test (< ?minimum-semester1 ?current-semester))
    (test (< ?minimum-semester2 ?current-semester))
    =>
    (bind ?accumulative-credits (+ ?accumulative-credits (+ ?credits1 ?credits2)))
    (modify ?module1 (status planned) (semester ?current-semester))
    (modify ?module2 (status planned) (semester ?current-semester))
    (modify ?management (number-of-module (+ ?number-of-module 2)) (must-plan-number-module (- ?must-modules 2)) (accumulative-credits ?accumulative-credits))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (assert (prereq (code ?coreq) (minimum-semester ?current-semester)))
    (printout t ?code " and " ?coreq " are planned" crlf))

; ; Plan module with offering at semester 2 only, with corequisite
(defrule plan-sem2-with-coreq
    (declare (salience 8))
    ?module1 <- (module (code ?code) (credits ?credits1) (minimum-semester ?minimum-semester1) (status available) (offer 2) (coreq ?coreq))
    ?module2 <- (module (code ?coreq) (credits ?credits2) (minimum-semester ?minimum-semester2) (status available) (offer 2))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?accumulative-credits))
    (test (eq (mod ?current-semester 4) 2))
    (test (< ?number-of-module 4))
    (test (< ?minimum-semester1 ?current-semester))
    (test (< ?minimum-semester2 ?current-semester))
    =>
    (bind ?accumulative-credits (+ ?accumulative-credits (+ ?credits1 ?credits2)))
    (modify ?module1 (status planned) (semester ?current-semester))
    (modify ?module2 (status planned) (semester ?current-semester))
    (modify ?management (number-of-module (+ ?number-of-module 2)) (must-plan-number-module (- ?must-modules 2)) (accumulative-credits ?accumulative-credits))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (assert (prereq (code ?coreq) (minimum-semester ?current-semester)))
    (printout t ?code " and " ?coreq " are planned" crlf))

; ; Plan module with offering at semester 1 and 2, with corequisite
(defrule plan-sem1-and-sem2-with-coreq
    (declare (salience 7))
    ?module1 <- (module (code ?code) (credits ?credits1) (minimum-semester ?minimum-semester1) (status available) (offer $?offer-semester1) (coreq ?coreq))
    ?module2 <- (module (code ?coreq) (credits ?credits2) (minimum-semester ?minimum-semester2) (status available) (offer $?offer-semester2))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?accumulative-credits))
    (and (test (member$ 1 ?offer-semester1)) (test (member$ 1 ?offer-semester1)))
    (and (test (member$ 1 ?offer-semester2)) (test (member$ 2 ?offer-semester2)))
    (test (eq (mod ?current-semester 4) 2))
    (test (< ?number-of-module 4))
    (test (< ?minimum-semester1 ?current-semester))
    (test (< ?minimum-semester2 ?current-semester))
    =>
    (bind ?accumulative-credits (+ ?accumulative-credits (+ ?credits1 ?credits2)))
    (modify ?module1 (status planned) (semester ?current-semester))
    (modify ?module2 (status planned) (semester ?current-semester))
    (modify ?management (number-of-module (+ ?number-of-module 2)) (must-plan-number-module (- ?must-modules 2)) (accumulative-credits ?accumulative-credits))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (assert (prereq (code ?coreq) (minimum-semester ?current-semester)))
    (printout t ?code " and " ?coreq " are planned " ?accumulative-credits crlf))

; ; Plan module with offering at semester 1 only, without corequisite
(defrule plan-sem1-no-coreq
    (declare (salience 6))
	?module <- (module (code ?code) (credits ?credits) (minimum-semester ?minimum-semester) (status available) (offer 1) (coreq))
	?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?accumulative-credits))
    (test (eq (mod ?current-semester 4) 1))
    (test (< ?number-of-module 5))
    (test (< ?minimum-semester ?current-semester))
	=>
	(modify ?module (status planned) (semester ?current-semester))
	(modify ?management (number-of-module (+ ?number-of-module 1)) (must-plan-number-module (- ?must-modules 1)) (accumulative-credits (+ ?accumulative-credits ?credits)))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (printout t ?code " is planned" crlf))

; ; Plan module with offering at semester 1 only, without corequisite
(defrule plan-sem2-no-coreq
    (declare (salience 6))
    ?module <- (module (code ?code) (credits ?credits) (minimum-semester ?minimum-semester) (status available) (offer 2) (coreq))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?accumulative-credits))
    (test (eq (mod ?current-semester 4) 2))
    (test (< ?number-of-module 5))
    (test (< ?minimum-semester ?current-semester))
    =>
    (modify ?module (status planned) (semester ?current-semester))
    (modify ?management (number-of-module (+ ?number-of-module 1)) (must-plan-number-module (- ?must-modules 1)) (accumulative-credits (+ ?accumulative-credits ?credits)))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (printout t ?code " is planned" crlf))

; ; Plan module with offering at semester 1 and 2, without corequisite
(defrule plan-sem1-and-sem2-no-coreq
    (declare (salience 5))
    ?module <- (module (code ?code) (credits ?credits) (minimum-semester ?minimum-semester) (status available) (offer $?offer-semester) (coreq))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?accumulative-credits))
    (test (member$ 1 ?offer-semester))
    (test (member$ 2 ?offer-semester))
    (or (test (eq (mod ?current-semester 4) 1)) (test (eq (mod ?current-semester 4) 2)))
    (test (< ?number-of-module 5))
    (test (< ?minimum-semester ?current-semester))
    =>
    (modify ?module (status planned) (semester ?current-semester))
    (modify ?management (number-of-module (+ ?number-of-module 1)) (must-plan-number-module (- ?must-modules 1)) (accumulative-credits (+ ?accumulative-credits ?credits)))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (printout t ?code " is planned" crlf))

; ; Allocate elective when there is nothing to take with existing modules that are not planned
(defrule plan-elective-with-available-module
    (declare (salience 1))
    (module (status available) (offer $?offer-semester))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?accumulative-credits))
    (and (test (not (eq 3 ?current-semester))) (test (not (eq 4 ?current-semester))))
    (test (not (member$ (mod ?current-semester 4) ?offer-semester)))
    =>
    (bind ?name (sym-cat UELECTIVE ?*elective-count*))
    (bind ?*elective-count* (+ ?*elective-count* 1))
    (assert (module (code ?name) (type UE) (status planned) (semester ?current-semester)))
    (modify ?management (number-of-module (+ ?number-of-module 1)) (accumulative-credits (+ ?accumulative-credits 4)))
    (refresh plan-elective-with-available-module)
    (printout t ?name " is planned" crlf))

; ; Allocate elective when there is nothing to take
(defrule plan-elective-with-no-must-module
    (declare (salience 1))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?accumulative-credits))
    (and (test (eq ?must-modules 0)) (test (< ?accumulative-credits 160)))
    =>
    (bind ?name (sym-cat UELECTIVE ?*elective-count*))
    (bind ?*elective-count* (+ ?*elective-count* 1))
    (assert (module (code ?name) (type UE) (status planned) (semester ?current-semester)))
    (modify ?management (number-of-module (+ ?number-of-module 1)) (accumulative-credits (+ ?accumulative-credits 4)))
    (refresh plan-elective-with-no-must-module)
    (printout t ?name " is planned" crlf))

(defrule plan-sip
    (declare (salience 15))
    ?module <- (module (code ?code) (credits ?credits) (minimum-semester ?minimum-semester) (status available))
    (or (test (eq ?code CP3200)) (test (eq ?code CP3202)))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?accumulative-credits))
    (test (eq (mod ?current-semester 4) 3))
    (test (< ?number-of-module 5))
    (test (< ?minimum-semester ?current-semester))
    (test (> ?accumulative-credits 70))
    =>
    (modify ?module (status planned) (semester ?current-semester))
    (modify ?management (number-of-module (+ ?number-of-module 1)) (must-plan-number-module (- ?must-modules 1)) (accumulative-credits (+ ?accumulative-credits ?credits)))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (printout t ?code " is planned" crlf))
;;;;;;;;;;;;;;;
;; Templates ;;
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
	(slot preferred-module-amount
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
    (test (eq ?number-of-module ?prefer))
    =>
    (bind ?next-semester (+ ?current-semester 1))
    (modify ?management (current-semester ?next-semester) (number-of-module 0))
    (printout t "Shift to Semester " ?next-semester crlf))

; ; Mark the module available        
(defrule mark-available
    (declare (salience 9))
    ?module <- (module (code ?code) (status unavailable) (prereq))
    =>
    (modify ?module (status available))
    (printout t ?code " is available now" crlf))

(defrule program-end
    (declare (salience 5))
    ?management <- (management (must-plan-number-module ?must-modules) (accumulative-credits ?credits))
    (test (eq ?must-modules 0))
    (test (>= ?credits 160))
    =>
    (halt))

;;;;;;;;;;;;;;;;;;;;;
;; MODULE PLANNING ;;
;;;;;;;;;;;;;;;;;;;;;

; ; Plan module with offering at semester 1 only, with corequisite
(defrule planned-sem1-with-coreq
    (declare (salience 8))
    ?module1 <- (module (code ?code) (minimum-semester ?minimum-semester1) (status available) (offer 1) (coreq ?coreq))
    ?module2 <- (module (code ?coreq) (minimum-semester ?minimum-semester2) (status available) (offer 1))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?credits))
    (test (eq (mod ?current-semester 4) 1))
    (test (< ?number-of-module 4))
    (test (< ?minimum-semester1 ?current-semester))
    (test (< ?minimum-semester2 ?current-semester))
    =>
    (bind ?number-of-module (+ ?number-of-module 2))
    (modify ?module1 (status planned) (semester ?current-semester))
    (modify ?module2 (status planned) (semester ?current-semester))
    (modify ?management (number-of-module ?number-of-module))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (assert (prereq (code ?coreq) (minimum-semester ?current-semester)))
    (printout t ?code " and " ?coreq " are planned" crlf)
    (printout t "Semester " ?current-semester " has planned for " ?number-of-module " module(s)" crlf))

; ; Plan module with offering at semester 2 only, with corequisite
(defrule planned-sem2-with-coreq
    (declare (salience 8))
    ?module1 <- (module (code ?code) (minimum-semester ?minimum-semester1) (status available) (offer 2) (coreq ?coreq))
    ?module2 <- (module (code ?coreq) (minimum-semester ?minimum-semester2) (status available) (offer 2))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?credits))
    (test (eq (mod ?current-semester 4) 2))
    (test (< ?number-of-module 4))
    (test (< ?minimum-semester1 ?current-semester))
    (test (< ?minimum-semester2 ?current-semester))
    =>
    (bind ?number-of-module (+ ?number-of-module 2))
    (modify ?module1 (status planned) (semester ?current-semester))
    (modify ?module2 (status planned) (semester ?current-semester))
    (modify ?management (number-of-module ?number-of-module))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (assert (prereq (code ?coreq) (minimum-semester ?current-semester)))
    (printout t ?code " and " ?coreq " are planned" crlf)
    (printout t "Semester " ?current-semester " has planned for " ?number-of-module " module(s)" crlf))

; ; Plan module with offering at semester 1 and 2, with corequisite
(defrule planned-sem1-and-sem2-with-coreq
    (declare (salience 7))
    ?module1 <- (module (code ?code) (minimum-semester ?minimum-semester1) (status available) (offer $?offer-semester1) (coreq ?coreq))
    ?module2 <- (module (code ?coreq) (minimum-semester ?minimum-semester2) (status available) (offer $?offer-semester2))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?credits))
    (and (test (member$ 1 ?offer-semester1)) (test (member$ 1 ?offer-semester1)))
    (and (test (member$ 1 ?offer-semester2)) (test (member$ 2 ?offer-semester2)))
    (test (eq (mod ?current-semester 4) 2))
    (test (< ?number-of-module 4))
    (test (< ?minimum-semester1 ?current-semester))
    (test (< ?minimum-semester2 ?current-semester))
    =>
    (bind ?number-of-module (+ ?number-of-module 2))
    (modify ?module1 (status planned) (semester ?current-semester))
    (modify ?module2 (status planned) (semester ?current-semester))
    (modify ?management (number-of-module ?number-of-module))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (assert (prereq (code ?coreq) (minimum-semester ?current-semester)))
    (printout t ?code " and " ?coreq " are planned" crlf)
    (printout t "Semester " ?current-semester " has planned for " ?number-of-module " module(s)" crlf))

; ; Plan module with offering at semester 1 only, without corequisite
(defrule planned-sem1-no-coreq
    (declare (salience 6))
	?module <- (module (code ?code) (minimum-semester ?minimum-semester) (status available) (offer 1) (coreq))
	?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?credits))
    (test (eq (mod ?current-semester 4) 1))
    (test (< ?number-of-module 5))
    (test (< ?minimum-semester ?current-semester))
	=>
    (bind ?number-of-module (+ ?number-of-module 1))
	(modify ?module (status planned) (semester ?current-semester))
	(modify ?management (number-of-module ?number-of-module))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (printout t ?code " is planned" crlf)
    (printout t "Semester " ?current-semester " has planned for " ?number-of-module " module(s)" crlf))

; ; Plan module with offering at semester 1 only, without corequisite
(defrule planned-sem2-no-coreq
    (declare (salience 6))
    ?module <- (module (code ?code) (minimum-semester ?minimum-semester) (status available) (offer 2) (coreq))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?credits))
    (test (eq (mod ?current-semester 4) 2))
    (test (< ?number-of-module 5))
    (test (< ?minimum-semester ?current-semester))
    =>
    (bind ?number-of-module (+ ?number-of-module 1))
    (modify ?module (status planned) (semester ?current-semester))
    (modify ?management (number-of-module ?number-of-module))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (printout t ?code " is planned" crlf)
    (printout t "Semester " ?current-semester " has planned for " ?number-of-module " module(s)" crlf))

; ; Plan module with offering at semester 1 and 2, without corequisite
(defrule planned-sem1-and-sem2-no-coreq
    (declare (salience 5))
    ?module <- (module (code ?code) (minimum-semester ?minimum-semester) (status available) (offer $?offer-semester) (coreq))
    ?management <- (management (current-semester ?current-semester) (number-of-module ?number-of-module) (must-plan-number-module ?must-modules) (accumulative-credits ?credits))
    (test (member$ 1 ?offer-semester))
    (test (member$ 2 ?offer-semester))
    (or (test (eq (mod ?current-semester 4) 1)) (test (eq (mod ?current-semester 4) 2)))
    (test (< ?number-of-module 5))
    (test (< ?minimum-semester ?current-semester))
    =>
    (bind ?number-of-module (+ ?number-of-module 1))
    (modify ?module (status planned) (semester ?current-semester))
    (modify ?management (number-of-module ?number-of-module))
    (assert (prereq (code ?code) (minimum-semester ?current-semester)))
    (printout t ?code " is planned" crlf)
    (printout t "Semester " ?current-semester " has planned for " ?number-of-module " module(s)" crlf))



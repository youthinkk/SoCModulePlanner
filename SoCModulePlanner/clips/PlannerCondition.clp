; ; Templates
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
    (slot semester
    	(type INTEGER)
    	(default 0))
    (multislot coreq
        (type SYMBOL))
    (multislot prereq
    	(type SYMBOL))
    (multislot offer
        (type INTEGER)))
        
(deftemplate semester
	(slot current 
		(type INTEGER))
	(slot number-of-module
		(type INTEGER)
		(default 0))
	(slot preferred-module-amount
		(type INTEGER)
		(default 5)))

(deftemplate prereq
    (slot code
        (type SYMBOL))
    (slot fulfill
        (type SYMBOL)
        (default TRUE)))

; ; MODULES
; ; (defmodule PLAN (import MAIN ?ALL))

; ; Mark the module available        
(defrule mark-available
	?module <- (module (code ?code) (status unavailable) (prereq))
	=>
	(modify ?module (status available))
	(printout t ?code " is available now" crlf))

; ; Mark the module as planned
(defrule planned-sem1-no-coreq
	?module <- (module (code ?code) (status available) (offer 1) (coreq))
	?semester <- (semester (current ?current) (number-of-module ?number-of-module))
    (test(eq (mod ?current 4) 1))
    (test(< ?number-of-module 5))
	=>
    (bind ?number-of-module (+ ?number-of-module 1))
	(modify ?module (status planned) (semester 1))
	(modify ?semester (number-of-module ?number-of-module))
    (assert (prereq (code ?code)))
    (printout t ?code " is planned now" crlf)
    (printout t "Semester" ?current " has planned for " ?number-of-module " module(s)" crlf))





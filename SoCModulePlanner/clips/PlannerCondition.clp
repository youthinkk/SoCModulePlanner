; ; Module
(deftemplate module    "Module Info"
    (slot code
        (type SYMBOL))
    (slot level
        (type INTEGER))
    (slot credits
        (type INTEGER)  
        (default 4))
    (slot score
        (type INTEGER)   
        (default -1))
    (multislot history
        (type SYMBOL)))
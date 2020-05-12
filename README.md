# Corover-NLP
NLP engine to extract intent entity and subject from sentences

use http://localhost:8080/nlp/instant
body:
{
"question":"How to book train ticket ?"
}
Response :
[
    {
        "intent": "book/VB", #verb - VB
        "entity": "ticket/NN", #noun - NN
        "intentParent": null,
        "entityChild": "train/NN", - subject
        "compounds": []
    }
]

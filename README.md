
How an MLP works:
https://en.wikipedia.org/wiki/Multilayer_perceptron


This Neural network is designed for next word prediction. This model is designed to predict the next word in a sentence given a set context window. For example, given the sentence "Hello, my name is" the model should predict "trace". With a context window of 4, the model can see 4 previous words to make its prediction, ie "hello my name is". Based on training, it learns word patterns and associations. 

*Training Example* 

Input sentence: "hello, my name is trace" 

Pre-trained Prediction: "hello my name is [name]"

After 100 training epochs, the model learns: 

"hello my name is [trace]".

It can also learn from larger amounts of text. 

*Sample input*

Eagerly, a girl named Kim went hiking with her friends. They found a secret passageway under a big rock. Curious, they crawled through it and entered a magical world where all the animals talked. They learned that the animals had their own struggles but always found a way to be happy. 

*Sample output generated after 100 epochs*

eagerly a girl named kim [.] [hiking] [with] [her] [friends] [.] [they] [found] [a] [secret] [passageway] [under] [a] [big] [rock] [.] [curious] [they] [crawled] [through] [it] [and] [entered] [a] [magical] [world] [where] [all] [the] [animals] [talked] [.] [they] [learned] [that] [the] [animals] [had] [their] [own] [struggles] [but] [always] [found] [a] [way] [to] [be] [happy] 

 
As you can see, the model is good, but not perfect. It can acheive about 96% accuracy on trained data.  












This Neural network is designed for next word prediction. This model is designed to predict the next word in a sentence given a set context window. For example, given the sentence "Hello, my name is" the model should predict "trace". With a context window of 4, the model can see 4 previous words to make its prediction, ie "hello my name is". Based on training, it learns word patterns and associations. 

*Training* 

The general training flow is:

Raw Input (Strings) -> Encodings -> Feed forward -> Prediction -> Backpropagate error

Words are hashed and encoded into binary before being fed to the input layer as 1 dimensional vectors. For example, "Hi" turns into a binary representation of [1,0,0,1]. 
The model then updates its inputs to correspond the the given input word(s). If context window is 4, the input will be 4 concanated binaries of each word.
The model then computes its prediction (next word binary) by multiply the inputs by each weight and passed through a combination of activation functions including tanh and sigmoid with a softmax on the output layer.
The correct prediction is then given to the network, and the network trains via backpropagating the error through the network.

More detailed description of how MLP's work : https://en.wikipedia.org/wiki/Multilayer_perceptron



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


*Model Limitations*

While this model can accurately mimic training data, generally it is not the most ideal model choice for Natural Language Processing. This is mostly due to limited context- MLP's rely on fixed inputs, meaning they have no "memory". Obviously this is a big shortfall in language prediction, as only seeing a handful of words is not always sufficient. This is why models like RRN's, LSTM's, and transformers are preffered, because they have "memory" components. This is what transformer based LLM's like ChatGPT use.








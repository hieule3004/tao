[TOC](../../../../../../../../../README.md)
| [Part 2](../eightwords/README.md)
| [Part 3](../../../../../../../resources/README.md)

## Part 1: Using I Ching to help design event generating algorithm


### Motivation

Storytelling game, population simulation, or anything that simulate progress require a good content generation process.
This could be done manually, like cast a dice and read the result from a lookup table of events; or algorithmically<sup>
1</sup> with computer aid.

Something I feel unsatisfied about these methods is that they are separated. Manual rules are often quick to come up
with and easy to use but quite limited, while algorithms are the opposite - they scale well but too hard for human to
reason about. More importantly, not only do they not play well with each other, neither help with the previous step of
designing the process - to determine the assets used for creating content.

For the rest of this post, I will introduce concept of I Ching and how it could help elevate these concerns.

### I Ching

I Ching<sup>2</sup> (or Yi Jing) is an ancient Chinese divination text. This method of divination, which is rooted from
Taoism, is still popular to this day in countries influenced by Chinese culture.

To summarise, the divination method involve asking a question, then use a randomise process (counting stack or tossing
coins) to generate an oracle (a changing hexagram). That result can be interpreted to unfold the following about the
question: the current situation, should action be taken, the future situation where action taken. In addition, one can
gain even more knowledge looking at related diagrams from that result.

You can look at this very detailed blog post<sup>2</sup> (even ignore examples) to gain basic idea about I Ching.

#### The structure

<div>

[//]: # (<img alt="P1" src="https://designviz.osu.edu/iching/images/figure1.png" width="50%" height="50%" style="background-color: white; display: block; margin-left: auto; margin-right: auto;" />)
</div>

At its core, I Ching is a binary system. Start with 2 forms: Yin - broken line and Yang - full line or 1 line, we can 
build up 4 phenomenas (2 lines), 8 trigrams (3 lines) and 64 hexagrams (6 lines).

The profoundness of I Ching comes from the fact that, each of these value are assigned some images and meaning, and one 
can compose or decompose them to get deeper meaning. For example, hexagram 001000 hexagram named Enthusiasm, made of 
2 trigrams lower triagram 000 represent Earth, upper triagram 100 represent Thunder. The image of thunder striking earth
is invigorating and naturally enthusiastic. 

#### The divination

As summarised above, 

### References

[1] https://www.scaleyourapp.com/procedural-generation-a-comprehensive-guide-in-simple-words/

[2] https://twodreams.us/blog/strategic-divination-using-the-i-ching

---

### Motivation

If you have tried to create a table

- One of the harder part of world building is procedural generation
    - Especially on how to start
- Enter I Ching
    - Brief relation Taoism with Asian

### Why I Ching

#### The structure

- At its core, I Ching is a binary system (2, 4, 8, 64)
    - Thus easy to understand and implement
- The catch is that each number is more than a number
    - Each in their own category: Form, Phenomena, Symbol, Hexagram, etc.
    - Have their own name, image, and meaning e.g. 111 trigram represent heaven, 001000 hexagram mean Enthusiasm, etc
    - Bonus: associated with other part of Taoism ecosystem e.g. 5 Elements

#### The divination

- By some method of randomisation (yarrow stack, coin toss, dice cast):
    - generate a hexagram capable of changing to another
    - from an interpretation rule, get the judgement and its meaning
    - use to determine the current situation, should action be taken, the future situation if action taken
    - Vary on interpretation rule, but all have ~400 result combinations
- Thus is a good framework for generating event
    - scalable: number system scale by adding digit (2, 4, 8, 64, 384)
    - flexible: the set of value and meaning is fixed, but divination is random
        - can generate both randomly or algorithmically
        - can assign one own interpretation of meaning
# CFGtoCNF
1.INTRODUCTION
A context-free grammar is in Chomsky Normal Form (CNF) if every production is of the 
form 
A → BC 
A → a 
where a donates a terminal and A, B, C donate variables where neither B or C is the start variable. 
In addition, there is a production 
S → ε 
if and only if ε belongs to the language.
Step 1. Eliminate start symbol from RHS.
If start symbol S is at the RHS of any production in the grammar, create a new production as:
S0->S
where S0 is the new start symbol.
Step 2. Eliminate null, unit and useless productions.
If CFG contains null, unit or useless production rules, eliminate them. You can refer the this 
article to eliminate these types of production rules.
Step 3. Eliminate terminals from RHS if they exist with other terminals or non-terminals. e.g,; 
production rule X->xY can be decomposed as:
X->ZY
Z->x
Step 4. Eliminate RHS with more than two non-terminals.
e.g,; production rule X->XYZ can be decomposed as:
X->PZ
P->XY
2. Problem Definition
In this assignment, Context Free Grammar (from CFG.txt) converted to Chomsky Normal 
Form. It saved in CFG.txt to debug folder, did not write the path name in code. Then it should write 
each eliminate state on the screen. At the end CNF should be given to the user.
3. Pseudo Codes
def removeEpselon():
 # define iterators to travel in map
Iterator = cfgMap.entrySet().iterator();
 # Iterate through each production rule in the CFG
while itrerator hasNext:
 # If the production rule has the symbol "€" (epselon), which represents the empty string
 if "€" in production.right:
 # If the production rule has more than one symbol, remove the epselon symbol
 if len(production.right) > 1:
 cnfProductions.add(new Production(production.left, [symbol for symbol in production.right if symbol 
!= "€"]))
 # If the production rule has only one symbol (the epselon symbol), skip it
 else:
 continue
 # and the production rules in CNF without the epselon symbol
def eliminateUnitProduction():
 # Initialize a set to store the non-terminals that have unit production
 unitNonTerminals = set()
 # define iterators to travel in map
Iterator = cfgMap.entrySet().iterator();
 # Iterate through each production rule in the CFG
while itrerator hasNext:
 # If the production rule has only one non-terminal symbol on the right-hand side, add it to the set
 if len(production.right) == 1 and production.right[0] in cfg.nonTerminals:
 unitNonTerminals.add(production.left)
 # Initialize a list to store the new production rules in CNF
 cnfProductions = []
 # Iterate through each production rule in the CFG
 for production in cfg.productions:
 # If the left-hand side of the production rule is not in the set of non-terminals with unit production, add 
the production rule to the list
 if production.left not in unitNonTerminals:
 cnfProductions.add(production)
 # If the left-hand side of the production rule is in the set of non-terminals with unit production
 else:
 # Iterate through each production rule in the CFG
 for prod in cfg.productions:
 # If the left-hand side of the production rule is the non-terminal on the right-hand side of the unit 
production rule
 if prod.left == production.right[0]:
 # Add a new production rule with the left-hand side of the unit production rule and the right-hand 
side of the inner production rule
 cnfProductions.add(new Production(production.left, prod.right))
 # and the production rules in CNF without unit production
def eliminateTerminals():
 # Initialize a asciii number to generate unique non-terminal names
 int asciiBegin = 71; //G
 # Initialize a map to store the new non-terminals and their corresponding terminals
 tempList = {}
 # Iterate through each production rule in the CFG
 While itr.hasNext:
 # Iterate through each symbol in the production rule
 for i in range(len(production.right)):
 symbol = production.right[i]
 # If the symbol is a terminal, create a new non-terminal for it and add it to the map
 if symbol in cfg.terminals:
 newNonTerminal = f"T{counter}"
 counter += 1
 terminalMap[newNonTerminal] = symbol
 production.right[i] = newNonTerminal
 # Initialize a list to store the new production rules in CNF
 cnfProductions = []
 # Iterate through each production rule in the CFG
 for production in cfg.productions:
 # If the production rule has at least two symbols on the right-hand side, add it to the list
 if len(production.right) >= 2:
 cnfProductions.add(production)
 # If the production rule has only one symbol on the right-hand side
 else:
 # Add a new production rule with the left-hand side of the original rule and the corresponding terminal 
from the map
def removeThreeTerminal():
 # Initialize a asciii number to generate unique non-terminal names
 int asciiBegin = 71; //G
 # Initialize a keyList to store the keys of the map
 tempList = {}
 # Iterate through each production rule in the CFG
 While itr.hasNext:
 # If the production rule has three or more terminal symbols on the right-hand side
 if len(production.right) >= 3 and all(symbol in cfg.terminals for symbol in production.right):
 # Initialize a list to store the new production rules for the three or more terminal symbols
 newProductions = []
 # Iterate through the terminal symbols in the production rule
 for i in range(len(production.right) - 2):
 # Create a new non-terminal for each pair of terminal symbols
 newNonTerminal = f"T{ asciiBegin }"
 asciiBegin += 1
 # Add a new production rule with the new non-terminal and the pair of terminal symbols
 newProductions.append(new Production(newNonTerminal, [production.right[i], production.right[i + 
1]]))
 # Add a new production rule with the left-hand side of the original production rule and the last two 
terminal symbols and the last new non-terminal
newProductions.append(new Production(production.left, [production.right[-2], production.right[-1], 
newNonTerminal]))
# Add the new production rules to the list of CNF production rules
cnfProductions.extend(newProductions)
# If the production rule does not have three or more terminal symbols, add it to the list
else:
cnfProductions.add(production)
 
5. REFERENCES
• https://www.geeksforgeeks.org/converting-context-free-grammarchomsky-normal-form/
• https://github.com/sanand34/CFG-to-CNF
• https://cyberzhg.github.io/toolbox/cfg2cn

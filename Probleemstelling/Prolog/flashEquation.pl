%%%%%%%%%%%%%%%%%%%%
% Start function
%%%%%%%%%%%%%%%%%%%%

start(Eq,Answer):- 
	expression(E1,1), 
	operand(O), 
	expression(E2,1),  
	append(E1,O,T), 
	append(T,E2,Eq),
	solveEQ(Eq,Answer).

%%%%%%%%%%%%%%%%%%%%%%
% Expressies 
% uitwerkingen
%%%%%%%%%%%%%%%%%%%%%%

expression([1],_). % Invullen expressie
expression([2],_).
expression([3],_).
expression(Eq,D):- % E -> E O E
	D<3,
	plus(D,1,NewD),
	expression(E1,NewD), 
	operand(O), 
	expression(E2,NewD),  
	append(E1,O,T), 
	append(T,E2,Eq).

%%%%%%%%%%%
% Operands
%%%%%%%%%%%

operand([+]).
operand([*]).
operand([-]).
operand([/]).

%%%%%%%%%
% Solve
%%%%%%%%%

solve([+],[E1],[E2],Answer):- 
	plus(E1,E2,Answer).
solve([-],[E1],[E2],Answer):- 
	minus(E1,E2,Answer).
solve([*],[E1],[E2],Answer):- 
	multiply(E1,E2,Answer).
solve([/],[E1],[E2],Answer):- 
	division(E1,E2,Answer).

%%%%%%%%%%%%%%%%%%%%%%
% Solve Equation:
% *1 niet toegelaten
%%%%%%%%%%%%%%%%%%%%%%

solveEQ([Answer],Answer).	
%solveEQ([_,*,1|_], _):- fail.
solveEQ([E1,O,E2|Rest], Answer):-
	( 	((=(O,*),((E2 == 1); (E1 == 1))) ;(=(O,/),(E2 == 1)))
		->
		fail;
		
	solve([O],[E1],[E2], Result),
	append([Result],Rest, NewList),
	solveEQ(NewList, Answer)).
	
%%%%%%%%%%%%%%%%%%%%%%%
% BasicFunctions	
%%%%%%%%%%%%%%%%%%%%%%%

plus(A, B, C) :- C is A + B.
minus(A, B, C) :- C is A - B.
multiply(A, B, C) :- C is A * B.
division(A, B, C) :- C is A / B.
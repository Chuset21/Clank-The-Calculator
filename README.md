# Clank The Calculator

A calculator bot for Discord that can take in arithmetic expressions and evaluate them.

## How to Use

Just mention Clank anywhere in your message for him to attempt to evaluate the expression.

## Supported Operators, functions and constants

- Unary '__+__': +5.1
- Unary '__-__': -5.1  
  <br>

- Addition '__+__': 3 + 5.1
- Subtraction '__-__': 3 - 5.1
- Multiplication '__*__': 3 * 5.1
- Division '__/__': 3 / 5.1  
  <br>

- Exponentiation '__^__': 3 ^ 5.1
- Factorial '__!__': 3!  
  <br>

- Square root '__sqrt__': sqrt(5.1)
- Sine '__sin__': sin(5.1)
- Cosine '__cos__': cos(5.1)
- Tangent '__tan__': tan(5.1)
- Arc sine '__arcsin__': arcsin(0.8)
- Arc cosine '__arccos__': arccos(0.8)
- Arc tangent '__arctan__': arctan(0.8)
- Log base 10 '__log__': log(100)
- Log of any base '__log__': log(8, 2)
- Natural log '__ln__': ln(5.1)
- Fibonacci sequence '__fib__': fib(3)  
  <br>

Supported constants:
- PI
- e

__Extra Information__:

- For Factorial operator and Fibonacci function if given a non-integer number the value will be rounded to the closest
  integer value, i.e: 2.4 -> 2, 2.5 -> 3.
- Whitespace is optional.
- Case is ignored.
- Brackets respect the rules of orders of operation, as does everything else, i.e: 2 * (2 + 3) -> 10.
- All trigonometric functions deal with degrees.
- For the log of any base (log), pass the base as the second argument. If given only one argument, log will default to using base 10.

## User Defined Constants
How to declare your own constant:  
Give it the constant name that you would like to set (must only contain letters) and then give it an expression after the '='.  
- const = e * 2 / ln(e)  

How to use your declared constant:
- const * 5.1

__Extra Information__:

- The names of the declared constants are case-sensitive, 'x' != 'X'.
- It can only store up to __100__ user defined constants. If the user tries to add another constant after this point, it will delete the oldest declared constant and add the new constant.

## Dependencies

This bot is made using [JDA](https://github.com/DV8FromTheWorld/JDA), an API made for Discord bot development in Java.
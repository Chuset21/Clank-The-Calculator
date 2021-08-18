# Clank The Calculator

A calculator bot for Discord that can take in arithmetic expressions and evaluate them.

## How to Use

Just mention Clank anywhere in your message for him to attempt to evaluate the expression.

### Supported Operators, functions and constants

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
- Log base e '__log__': log(5.1)
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
- While brackets are not needed for functions, if not used the bot will only calculate the first value after the
  function name, i.e: fib3 + 3 -> 5, but fib(3 + 3) -> 8.

## Dependencies

This bot is made using [JDA](https://github.com/DV8FromTheWorld/JDA), an API made for Discord bot development in Java.
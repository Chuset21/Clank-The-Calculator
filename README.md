# Clank The Calculator

A calculator bot for Discord that can take in arithmetic expressions and evaluate them.

## How to Use

Just mention Clank anywhere in your message for Clank to attempt to evaluate the expression.

### Supported Operators and functions

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
- Fibonacci sequence '__fib__': fib(3)

__Extra Information__:

- For Factorial operator and Fibonacci function if given a non-integer number the value will be rounded to the closest
  integer value, i.e: 2.4 -> 2, 2.5 -> 3.
- Whitespace is optional.
- Brackets respect the rules of orders of operation
- While brackets are not needed for functions, if not used the bot will only calculate the first value after the
  function name, i.e: fib3 + 3 -> 5, but fib(3 + 3) -> 8.

## Dependencies

This bot is made using [JDA](https://github.com/DV8FromTheWorld/JDA), an API made for Discord bot development in Java.
# Clank The Calculator

A calculator bot for Discord that can take in arithmetic expressions and evaluate them.

## How to Use

Make sure to prefix your message with "__$$__" for Clank to attempt to evaluate the expression.

### Supported Operators and functions

I am going to include examples for all the operators and functions, however I will skip the "$$" prefix here for
clarity.

- Unary '__+__': +5.1
- Unary '__-__': -5.1


- Addition '__+__': 3 + 5.1
- Subtraction '__-__': 3 - 5.1
- Multiplication '__*__': 3 * 5.1
- Division '__/__': 3 / 5.1


- Exponentiation '__^__': 3 ^ 5.1
- Factorial '__!__': 3!


- Square root '__sqrt__': sqrt(5.1)
- Sine '__sin__': sin(5.1)
- Cosine '__cos__': cos(5.1)
- Tangent '__tan__': tan(5.1)
- Fibonacci sequence '__fib__': fib(3)

__Note__: For Factorial operator and Fibonacci function if given a non-integer number the value will be rounded to the
closest integer value, i.e: 2.4 -> 2, 2.5 -> 3.

## Dependencies

This bot is made using [JDA](https://github.com/DV8FromTheWorld/JDA), an API made for Discord bot development in Java.
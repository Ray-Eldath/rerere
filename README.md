# rerere

Super naive regular expression evaluator.

This implements a super naive regular expression (RE) with no extensions. We know that some wild extensions to RE actually make it *not* regular, and those extensions cannot be implemented in this framework. For others, they are mostly a shorthand abbreviation of a longer regular expression.

This is a practice project of algorithms presented in Chapter 2 of [Engineering a Compiler](https://www.amazon.com/Engineering-Compiler-Keith-D-Cooper-dp-0128154128/dp/0128154128/). EaC _was_ the go-to book for learning compiler, now this role has been replaced by [Essentials of Compilation](https://github.com/IUCompilerCourse/Essentials-of-Compilation). The main difference is that EaC is much longer than EoC, and you should always refer to the former one when you need to learn more about subjects not covered in EoC (like scanning and parsing techniques, or codegen).

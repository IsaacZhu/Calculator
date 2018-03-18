# SE计算器——递归下降产生式

1.是否图形化？ 如果图形化，UI由需求来设计，要设计成怎么样的UI？ 如果是图形化，是按键输入还是支持键盘输入？
(1)设计UI界面
    a.菜单设计 
    b.两个界面设计
(2)输入：a.表达式:完整表达式输入
            b.按键输入(完整表达式)
2.基本功能支持：加、减、乘 *、除 / 、等于、括号
3.数据范围？ double
输入长度？ 不限范围
浮点运算？ yes
4.高级运算功能支持？  
(1)根号: sqrt(x)
(2)幂:  x^a （-max ~ +max）
(3)三角运算: sin(x)
(4)lg及ln: lg(x)
(5) pai & e
(6)阶乘 : x!
5.进制转换
(1)二进制、八进制、十进制、十六进制 转换
(2)按位与、或、取反运算
(3)加、减、乘、除
6.高级实用功能支持？  AC C M
7.模块：(1)UI  ->  txt (2)输入分析(3)计算(4)结果显示
8.error :
(1)范围错误：溢出、越界、除数不为0、log等等的范围处理
(2)表达式有误： 未知字符、连续的符号等等



##科学计算器

```
expression	->	addition_expr + addition_expr
			|	addition_expr - addition_expr

addition_expr	->	mul_expr * mul_expr
			|		mul_expr / mul_expr

mul_expr	->	factor
			|	sqrt( expression )
			|	factor ^ factor
			|	factor !
			|	sin( expression )
			|	cos( expression )
			|	(- factor)

factor		->	( expression )
			|	pi
			|	e
			|	unsigned int
			|	unsigned float
```



## 程序员计算器

```
expression	->	or_expr

or_expr		->	xor_expr
			|	or_expr '|' xor_expr

xor_expr	->  and_expr
			|	xor_expr '^' and_expr

and_expr	->	additive_expr
			|	and_expr '&' additive_expr

additive_expr	->	mul_expr
				|	additive_expr '+' addition_expr

mul_expr	->	not_expr
			|	mul_expr '*' not_expr
			|	mul_expr '/' not_expr

not_expr	->	prime_expr
			|	'!' prime_expr

prime_expr	->	factor
			|  '-'factor

factor		-> (expression)
			|	unsigned int
```


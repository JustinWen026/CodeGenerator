Mermaid Class Diagram Code Generator
Introduction | 介紹
This is a simple code generator that converts Mermaid class diagrams into corresponding Java source code. Mermaid is a JavaScript-based diagramming and charting tool that renders Markdown-inspired text definitions to create diagrams. This project focuses on generating Java class code from Mermaid's class diagram syntax.

這是一個簡單的代碼生成器，它將 Mermaid 類圖轉換為相應的 Java 源代碼。Mermaid 是一種基於 JavaScript 的圖表工具，可以將類似 Markdown 的文本定義轉換為圖表。本項目專注於從 Mermaid 類圖語法生成 Java 類代碼。

Features | 特性
Converts Mermaid class diagram syntax into Java classes

Supports class attributes and methods (both getters and setters)

Follows Java's CamelCase naming convention

Outputs a .java file for each class in the Mermaid diagram

將 Mermaid 類圖語法轉換為 Java 類

支持類的屬性和方法（包括 getter 和 setter）

遵循 Java 的 CamelCase 命名規則

為 Mermaid 圖中的每個類生成一個 .java 文件

Requirements | 系統要求
Java 8 or above

Basic knowledge of Mermaid class diagram syntax

Java 8 及以上

基本的 Mermaid 類圖語法知識

Usage | 使用方法
Step 1: Define a Mermaid Class Diagram | 步驟一：定義 Mermaid 類圖
Write a Mermaid class diagram using the following syntax:

使用以下語法撰寫 Mermaid 類圖：

mermaid
複製程式碼
classDiagram
    class Person {
        -int age
        -String name
        +setName(String name) void
        +getName() String
    }
Step 2: Run the Code Generator | 步驟二：運行代碼生成器
Run the Java program and provide the input file that contains the Mermaid class diagram:

運行 Java 程式，並提供包含 Mermaid 類圖的輸入文件：

bash
複製程式碼
java CodeGenerator tc1
Step 3: Generated Java Code | 步驟三：生成的 Java 代碼
The program will output a .java file for each class defined in the Mermaid diagram. For example, the above Mermaid diagram will generate a Person.java file containing:

程式將為 Mermaid 圖中定義的每個類生成一個 .java 文件。例如，上面的 Mermaid 圖將生成一個 Person.java 文件，內容如下：

java
複製程式碼
class Person {
    private int age;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
Input Syntax | 輸入語法
Each line in the Mermaid class diagram corresponds to a string.

The diagram starts with classDiagram and contains class definitions, attributes, and methods.

Class members are distinguished by their syntax, where methods contain parentheses () and attributes do not.

Supported access modifiers are + for public and - for private.

Mermaid 類圖中的每一行對應於一個字符串。

圖表以 classDiagram 開頭，包含類定義、屬性和方法。

類的成員根據語法區分，方法帶有括號 ()，屬性則沒有。

支持的訪問修飾符為 + 表示 public，- 表示 private。

Output | 輸出
A .java file is generated for each class in the Mermaid diagram.

Each class contains the defined attributes and methods, following the specified syntax and indentation rules.

For non-getter and non-setter methods, default return values (0 for int, "" for String, false for boolean) are provided when necessary.

每個 Mermaid 類圖中的類都會生成一個 .java 文件。

每個類包含定義的屬性和方法，遵循指定的語法和縮排規則。

對於非 getter 和 setter 的方法，當需要時提供默認返回值（int 為 0，String 為 ""，boolean 為 false）。

Notes | 注意事項
Only one argument for the test case file name is accepted.

Make sure to follow the CamelCase naming convention when generating code.

Extra spaces in the Mermaid input should be ignored.

測試用例文件名只接受一個參數。

生成代碼時請確保遵循 CamelCase 命名規則。

Mermaid 輸入中的多餘空格應忽略。

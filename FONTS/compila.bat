@echo off

REM set argC=0
REM for %%x in (%*) do Set /A argC+=1
REM if NOT argC == 1 goto :error

if /i %1 == TestAnd goto :and
if /i %1 == Driver goto :driver
if /i %1 == TestDocument goto :document
if /i %1 == TestDocumentsSet goto :documentsset
if /i %1 == TestExpression goto :expression
if /i %1 == TestExpressionsSet goto :expressionsset
if /i %1 == TestInternalDocument goto :internaldocument
if /i %1 == TestLiteral goto :literal
if /i %1 == TestNot goto :nota
if /i %1 == TestOr goto :or
if /i %1 == TestPair goto :pair

REM :error
echo:
echo USAGE
echo compila.bat test-a-executar
echo:
echo Opcions per test-a-executar:
echo Driver
echo TestDocument
echo TestDocumentsSet
echo TestExpression
echo TestExpressionsSet
echo TestInternalDocument
echo TestLiteral
echo TestAnd
echo TestNot
echo TestOr
echo TestPair
echo: 
echo Nota: Nomes es recompilara el driver interactiu! 
goto :EOF

REM === INSTRUCCIONS PER RECOMPILAR EL PROJECTE ===

REM === AND ===
javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/And ./test/domain/expressions/TestAnd.java

:and
java -cp "../EXE/And;./../lib/junit-4.12.jar;./../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestAnd
goto :EOF

REM === DRIVER INTERACTIU ===
:driver
javac -cp "." -d ../EXE/CtrlDomain ./test/domain/DriverCtrlDomain.java

java -cp ../EXE/CtrlDomain test.domain.DriverCtrlDomain
goto :EOF

REM === DOCUMENT ===
javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/Document test/domain/documents/TestDocument.java

:document
java -cp "../EXE/Document;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.documents.TestDocument
goto :EOF

REM === DOCUMENTSSET ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/DocumentSet test/domain/documents/TestDocumentsSet.java

:documentsset
java -cp "../EXE/DocumentsSet;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.documents.TestDocumentsSet
goto :EOF

REM === EXPRESSION ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/Expression test/domain/expressions/TestExpression.java

:expression
java -cp "../EXE/Expression;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestExpression
goto :EOF

REM === EXPRESSIONSSET ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/ExpressionsSet test/domain/expressions/TestExpressionsSet.java

:expressionsset
java -cp "../EXE/ExpressionsSet;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestExpressionsSet
goto :EOF

REM === INTERNALDOCUMENT ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/InternalDocument test/domain/documents/TestInternalDocument.java

:internaldocument
java -cp "../EXE/InternalDocument;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.documents.TestInternalDocument
goto :EOF

REM === LITERAL ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/Literal test/domain/expressions/TestLiteral.java

:literal
java -cp "../EXE/Literal;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestLiteral
goto :EOF

REM === NOT ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/Not test/domain/expressions/TestNot.java

:nota
java -cp "../EXE/Not;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestNot
goto :EOF

REM === OR ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/Or test/domain/expressions/TestOr.java

:or
java -cp "../EXE/Or;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestOr
goto :EOF

REM === PAIR ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/Pair test/domain/util/TestPair.java

:pair
java -cp "../EXE/Pair;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.util.TestPair
goto :EOF
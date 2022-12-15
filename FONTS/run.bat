@echo off

set argC=0
for %%x in (%*) do Set argC=1
if %argC% == 0 goto :propy

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

echo:
echo: USAGE
echo: El fitxer run.bat rep 0 o 1 parametres. Cap parametre llencara l'aplicacio sencera. Un parametre especificara un test a executar
echo: run.bat test-a-executar
echo:
echo: Opcions per test-a-executar:
echo: Driver
echo: TestDocument
echo: TestDocumentsSet
echo: TestExpression
echo: TestExpressionsSet
echo: TestInternalDocument
echo: TestLiteral
echo: TestAnd
echo: TestNot
echo: TestOr
echo: TestPair
echo: 
echo: Nota: Nomes es recompilara el driver interactiu! 
goto :EOF

REM === COMANDA DE RECOMPILACIO I EXECUCIO DEL SISTEMA ===

javac -cp ".;../lib/forms_rt.jar" -d ../EXE/App -encoding UTF-8 ./main/presentation/Main.java

:propy
cd ..
java -jar EXE/JocDeProvesApp/JocDeProves.jar
cd FONTS
goto :EOF

REM === INSTRUCCIONS PER RECOMPILAR EL PROJECTE ===

REM === AND ===
javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/And ./test/domain/expressions/TestAnd.java

:and
echo Running test for And...
java -cp "../EXE/TestsJUnit/And;./../lib/junit-4.12.jar;./../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestAnd
goto :EOF

REM === DRIVER INTERACTIU ===
:driver
echo Recompiling Driver...
javac -cp "." -d ../EXE/TestsJUnit/CtrlDomain ./test/domain/DriverCtrlDomain.java

echo Running Driver...
java -cp ../EXE/TestsJUnit/CtrlDomain test.domain.DriverCtrlDomain
goto :EOF

REM === DOCUMENT ===
javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/Document test/domain/documents/TestDocument.java

REM java -jar ../EXE/TestsJUnit/CtrlDomain/TestCtrlDomain.jar

:document
echo Running test for Document...
java -cp "../EXE/TestsJUnit/Document;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.documents.TestDocument
goto :EOF

REM === DOCUMENTSSET ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/DocumentSet test/domain/documents/TestDocumentsSet.java

:documentsset
echo Running test for DocumentsSet...
java -cp "../EXE/TestsJUnit/DocumentsSet;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.documents.TestDocumentsSet
goto :EOF

REM === EXPRESSION ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/Expression test/domain/expressions/TestExpression.java

:expression
echo Running test for Expression...
java -cp "../EXE/TestsJUnit/Expression;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestExpression
goto :EOF

REM === EXPRESSIONSSET ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/ExpressionsSet test/domain/expressions/TestExpressionsSet.java

:expressionsset
echo Running test for ExpressionSet...
java -cp "../EXE/TestsJUnit/ExpressionsSet;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestExpressionsSet
goto :EOF

REM === INTERNALDOCUMENT ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/InternalDocument test/domain/documents/TestInternalDocument.java

:internaldocument
echo Running test for InternalDocument...
java -cp "../EXE/TestsJUnit/InternalDocument;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.documents.TestInternalDocument
goto :EOF

REM === LITERAL ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/Literal test/domain/expressions/TestLiteral.java

:literal
echo Running test for Literal...
java -cp "../EXE/TestsJUnit/Literal;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestLiteral
goto :EOF

REM === NOT ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/Not test/domain/expressions/TestNot.java

:nota
echo Running test for Not...
java -cp "../EXE/TestsJUnit/Not;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestNot
goto :EOF

REM === OR ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/Or test/domain/expressions/TestOr.java

:or
echo Running test for Or...
java -cp "../EXE/TestsJUnit/Or;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.expressions.TestOr
goto :EOF

REM === PAIR ===

javac -cp ".;../lib/junit-4.12.jar;../hamcrest-core-1.3.jar" -d ../EXE/TestsJUnit/Pair test/domain/util/TestPair.java

:pair
echo Running test for Pair...
java -cp "../EXE/TestsJUnit/Pair;../lib/junit-4.12.jar;../lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.domain.util.TestPair
goto :EOF